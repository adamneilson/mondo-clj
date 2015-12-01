(ns mondo-clj.core
  (:require 
    [mondo-clj.util :refer :all]    
    [mondo-clj.api :as api]))





;;================================================
;;
;;  AUTHORIZATION
;;
;;================================================


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
         (prepare-map)
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
         (prepare-map)
         (api/POST "/oauth2/token"))
    (api/get-error 400)))



(defn whoami 
  "To get information about an access token, you can call the 
  /ping/whoami endpoint." 
  [access-token]
  (if (not-nil? access-token)
    (api/GET "/ping/whoami" access-token)
    (api/get-error 400)))



;;================================================
;;
;;  ACCOUNTS
;;
;;================================================


(defn list-accounts 
  "Returns a list of accounts owned by the currently authorised 
  user." 
  [access-token]
  (if (not-nil? access-token)
    (let [result (api/GET "/accounts" access-token)]
      (assoc-in result [:accounts] 
                (map (fn [acc] 
                       (update-in acc [:created] zulu-to-instant))
                     (:accounts result))))
    (api/get-error 400)))




;;================================================
;;
;;  BALANCE
;;
;;================================================

(defn read-balance
  "Returns balance information for a specific account."
  [access-token account-id]
  (if (every? not-nil? [access-token account-id])
    (let [result (->> {:account-id account-id}
                      (pre-process-vals)
                      (prepare-map)
                      (api/GET "/balance" access-token))]
      (if (and (contains? result :balance)
               (contains? result :spend-today))
        (-> result
            ; coerce the balance & spend-today to doubles
            (update-in [:balance] coerce-to-double-monetary-amount)
            (update-in [:spend-today] coerce-to-double-monetary-amount))
        ; we didn't get the balance in the result
        result))
    (api/get-error 400)))

       




;;================================================
;;
;;  TRANSACTIONS
;;
;;================================================



(defn get-transaction 
  "Get a specific txn" 
  [access-token transaction-id]
  (if (and (every? not-nil? [access-token transaction-id])
           (is-valid-txn-id? transaction-id))
    (-> (api/GET (format "/transactions/%s" transaction-id) access-token {"expand[]" "merchant"})
        (update-in [:transaction :created] zulu-to-instant)
        (update-in [:transaction :merchant :created] zulu-to-instant)
        (update-in [:transaction :account-balance] coerce-to-double-monetary-amount)
        (update-in [:transaction :amount] coerce-to-double-monetary-amount)
        )
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
        (deliver opts (prepare-map {:account-id account-id
                                    :limit limit 
                                    :before before-zulu}))
        ; test for txn ID
        (if (and (string? since)
                 (is-valid-txn-id? since))
          (deliver opts (prepare-map {:account-id account-id
                                      :limit limit 
                                      :since since}))

          ;however if it's an instant then we may want to have 
          ;a timeframe with the before instant as well...
          (deliver opts (prepare-map {:account-id account-id
                                      :limit limit 
                                      :since since-zulu 
                                      :before before-zulu}))))

      ; covert all zulu times to instants
      (let [result (api/GET "/transactions" access-token @opts)]
        (-> result
            (assoc-in [:transactions] (map (fn [tx] (update-in tx [:created] zulu-to-instant)) (:transactions result)))
            (assoc-in [:transactions] (map (fn [tx] (update-in tx [:account-balance] coerce-to-double-monetary-amount)) (:transactions result)))
            (assoc-in [:transactions] (map (fn [tx] (update-in tx [:amount] coerce-to-double-monetary-amount)) (:transactions result)))
            )))
    (api/get-error 400)))


(comment 
  (prepare-map {:access-token "0000000000000000"
                       :account-id "account_id"
                       :limit [100 0] ;[per-page page-number]
                       :since #inst "2015-11-26T00:00:00.000-00:00"})
)





(defn annotate-transaction 
  "Set a metadata-map key's value as empty to delete it" 
  [access-token transaction-id metadata-map]
  (if (and (every? not-nil? [access-token transaction-id])
           (is-valid-txn-id? transaction-id)
           (> (count metadata-map) 0))
    (-> (api/PATCH (format "/transactions/%s" transaction-id) 
                   access-token 
                   (reduce 
                     (fn [m [k v]] 
                       (assoc m (format "metadata[%s]" (name k)) v))
                     {} metadata-map))
        (update-in [:transaction :created] zulu-to-instant)
        (update-in [:transaction :account-balance] coerce-to-double-monetary-amount)
        (update-in [:transaction :amount] coerce-to-double-monetary-amount))
    (api/get-error 400)))





;;================================================
;;
;;  FEED
;;
;;================================================



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
                         (remove-nils)
                         (prepare-map))]
      (api/POST "/feed" 
                access-token 
                (prepare-map {:account-id account-id
                              :type type
                              :params parameters
                              :url url})))
    (api/get-error 400)))

(comment (prepare-map {:access-token "0000000000000000"
                       :account-id "account_id"
                       :type "basic"
                       :params {:title "My custom item"
                                :image-url "www.example.com/image.png"
                                :background-color "#FCF1EE"
                                :body-color "#FCF1EE"
                                :title-color "#333"
                                :body "Some body text to display"}
                       :url "http://aan.io"}))




;;================================================
;;
;;  WEBHOOKS
;;
;;================================================

(defn register-webhook [access-token account-id url] 
  (if (every? not-nil? [access-token account-id url])
    (api/POST "/webhooks" 
              access-token 
              (prepare-map {:account-id account-id
                            :url url})))
  (api/get-error 400))


(defn list-webhooks [access-token account-id] 
  (if (every? not-nil? [access-token account-id url])
    (api/GET "/webhooks" 
             access-token 
             (prepare-map {:account-id account-id})))
  (api/get-error 400))



(defn delete-webhook [access-token webhook-id] 
  (if (every? not-nil? [access-token webhook-id])
    (api/DELETE (format "/webhooks/%s" webhook-id) access-token))
  (api/get-error 400))






;;================================================
;;
;;  ATTACHMENTS
;;
;;================================================

(defn upload-attachment [access-token file-name file-type]
  (if (every? not-nil? [access-token file-name file-type])
    (api/POST "/attachment/upload" 
              access-token 
              (prepare-map {:file-name file-name
                            :file-type file-type})))
  (api/get-error 400))



(defn register-attachment [access-token external-id file-url file-type]
  (if (and (every? not-nil? [access-token external-id file-name file-type])
           (is-valid-txn-id? external-id))
    (-> (api/POST "/attachment/register" 
                  access-token 
                  (prepare-map {:external-id external-id
                                :file-url file-url
                                :file-type file-type}))
        (update-in [:attachment :created] zulu-to-instant))
    (api/get-error 400)))

    

(defn deregister-attachment [access-token id]
  (if (every? not-nil? [access-token id])
    (api/POST "/attachment/deregister" 
              access-token 
              (prepare-map {:id id}))
    (api/get-error 400)))






