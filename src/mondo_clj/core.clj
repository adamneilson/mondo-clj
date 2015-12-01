(ns mondo-clj.core
  (:require 
    [mondo-clj.util :refer :all]    
    [mondo-clj.api :as api]))





(defn get-access-token 
  "An access token is tied to both your application (the client) and 
  an individual Mondo user and is valid for several hours.

  Returns a map:

  {:access-token \"access_token\",
  :client-id \"client_id\",
  :expires-in 21600,
  :refresh-token \"refresh_token\",
  :token-type \"Bearer\",
  :user-id \"user_id\"}

  NB: Your client may only have one active access token at a time, per 
  user. Acquiring a new access token will invalidate any other token 
  you own for that user." 
  [{:keys [grant-type
           client-id
           client-secret
           username
           password]
    :or {grant-type "password"}}]
  ;check that the required fields are populated
  (if (every? not-nil? [grant-type client-id client-secret username password])
    (->> {:grant-type grant-type
          :client-id client-id
          :client-secret client-secret
          :username username
          :password password}
         (pre-process-vals)
         (api/POST "/oauth2/token"))
    (api/get-error 400)))



(defn refresh-access-token 
  "To limit the window of opportunity for attackers in the 
  event an access token is compromised, access tokens expire 
  after 6 hours. To gain long-lived access to a user’s account,
  it’s necessary to \“refresh\” your access when it expires using 
  a refresh token. Only \“confidential\” clients are issued refresh 
  tokens – \“public\” clients must ask the user to re-authenticate.

  Returns:

  {
  :access_token \"access_token_2\"
  :client_id \"client_id\"
  :expires_in 21600
  :refresh_token \"refresh_token_2\"
  :token_type \"Bearer\"
  :user_id \"user_id\"
  }

  Refreshing an access token will invalidate the previous token, if 
  it is still valid. Refreshing is a one-time operation." 
  [{:keys [grant-type
           client-id
           client-secret
           refresh-token]
    :or {grant-type "refresh_token"}}]
  (if (every? not-nil? [grant-type client-id client-secret refresh-token])
    (->> {:grant-type grant-type
          :client-id client-id
          :client-secret client-secret
          :refresh-token refresh-token}
         (pre-process-vals)
         (api/POST "/oauth2/token"))
    (api/get-error 400)))



(defn whoami 
  "To get information about an access token, you can call the 
  /ping/whoami endpoint." 
  [access-token]
  (if (not-nil? access-token)
    (api/GET "/ping/whoami" access-token)
    (api/get-error 400)))



(defn list-accounts 
  "Returns a list of accounts owned by the currently authorised 
  user." 
  [access-token]
  (if (not-nil? access-token)
    (api/GET "/accounts" access-token)
    (api/get-error 400)))



(defn get-transaction 
  "Get a specific txn" 
  [access-token transaction-id]
  (if (and (every? not-nil? [access-token transaction-id])
           (is-valid-txn-id? transaction-id))
    (api/GET (format "/transactions/%s" transaction-id) access-token {"expand[]" "merchant"})
    (api/get-error 400)))



(defn list-transactions 
  "List transactions that were made against a specific account. " 
  [{:keys [access-token
           account-id
           limit
           since
           before]
    :or {limit 100
         before (java.util.Date.)}}]
  (if (and (every? not-nil? [access-token account-id])
           (is-between? limit 0 100))
    (let [opts (promise)
          before-zulu (instant-to-zulu before)
          since-zulu (instant-to-zulu since)]

      ; 'since' is a weird one as it can be either an 
      ; instant OR a txn ID. Without the API to hit this
      ; is a bit of guess work at the moment. Pagination
      ; does feel a little clunky...

      ; is it set?
      (if (nil? since)
        ; moot point
        (deliver opts {:account-id account-id
                       :limit limit 
                       :before before-zulu})
        ; test for txn ID
        (if (and (string? since)
                 (is-valid-txn-id? since))
          (deliver opts {:account-id account-id
                         :limit limit 
                         :since since})

          ;however if it's an instant then we may want to have 
          ;a timeframe with the before instant as well...
          (deliver opts {:account-id account-id
                         :limit limit 
                         :since since-zulu 
                         :before before-zulu})))

      (api/GET "/transactions" access-token @opts))
    (api/get-error 400)))

(comment {:access-token "0000000000000000"
          :account-id "account_id"
          :limit [100 0] ;[per-page page-number]
          :since #inst "2015-11-26T00:00:00.000-00:00"})




(defn annotate-transaction 
  "Set a metadata-map key's value as empty to delete it" 
  [access-token transaction-id metadata-map]
  (if (and (every? not-nil? [access-token transaction-id])
           (is-valid-txn-id? transaction-id)
           (> (count metadata-map) 0))
    (api/PATCH (format "/transactions/%s" transaction-id) 
               access-token 
               (reduce 
                 (fn [m [k v]] 
                   (assoc m (format "metadata[%s]" (name k)) v))
                 {} metadata-map))
    (api/get-error 400)))

(comment {:key1 "val1" :key2 "val2"})







(defn create-feed-item
  "Inject an item/event into an accounts feed"
  [{:keys [access-token
           account-id
           type
           params
           url]
    :or {type "basic"}}]
  (if (and (every? not-nil? [access-token account-id type params])
           (every? not-nil? (vals (select-keys params [:title :image-url]))))
    (let [parameters (-> (select-keys params [:title
                                              :image-url
                                              :background-color
                                              :body-color
                                              :title-color
                                              :body])
                         (assoc :background-color (hex-colour (:background-color params)))
                         (assoc :body-color (hex-colour (:body-color params)))
                         (assoc :title-color (hex-colour (:title-color params)))
                         (remove-nils))]
      (api/POST "/feed" 
                access-token 
                (remove-nils {:account-id account-id
                              :type type
                              :params parameters
                              :url url})))
    (api/get-error 400)))

(comment {:access-token "0000000000000000"
          :account-id "account_id"
          :type "basic"
          :params {:title "My custom item"
                   :image-url "www.example.com/image.png"
                   :background-color "#FCF1EE"
                   :body-color "#FCF1EE"
                   :title-color "#333"
                   :body "Some body text to display"}
          :url "http://aan.io"})






  (defn register-webhook [] nil)
  (defn delete-webhook [] nil)
  (defn list-webhooks [] nil)

  (defn upload-attachment [] nil)
  (defn register-attachment [] nil)
  (defn deregister-attachment [] nil)




