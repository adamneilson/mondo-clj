(ns mondo-clj.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [mondo-clj.core :refer :all]
            [mondo-clj.util :refer :all]
            [cheshire.core :refer :all]))



(facts "Core get-access-token tests"
       (let [access-token (-> (get-access-token {:grant-type "password" 
                                                 :client-id (:mondo-test-client-id env)
                                                 :client-secret (:mondo-test-client-secret env)
                                                 :username (:mondo-test-username env)
                                                 :password (:mondo-test-password env)})
                              :access-token)]

         (fact "get a token from the api - correct grant-type"
               access-token =not=> nil)

         (fact "get a token from the api - wrong grant-type - missing fields"
               (-> (get-access-token {:grant-type "token"}) 
                   :success?)
               => false)

         (fact "get a token from the api - correct grant-type - missing fields"
               (-> (get-access-token {:grant-type "password"}) 
                   :success?)
               => false)


         (fact "Check who I am"
               (-> (whoami access-token) :authenticated) => true
               (-> (whoami "fake-access-token") :authenticated) => nil?)

         
         (fact "refresh access token"
               (-> (refresh-access-token nil) :success?) => false)


         (fact "Returns a list of accounts owned by the currently authorised user."
               (-> (list-accounts access-token)
                   (:accounts)
                   (first)
                   (keywordize-map)
                   (:id)
                   (subs 0 8)) => "acc_0000"
               (-> (list-accounts "fake-access-token")
                   (:accounts)
                   (first)
                   (:id)) => nil?)


         (fact "Returns balance information for a specific account."
               (let [account-id (-> (list-accounts access-token)
                                    (:accounts)
                                    (first)
                                    (keywordize-map)
                                    (:id))]
                 (-> (read-balance access-token account-id)
                     (:balance)) => number?))



         (fact "List transactions that were made against a specific account."
               (let [account-id (-> (list-accounts access-token)
                                    (:accounts)
                                    (first)
                                    (keywordize-map)
                                    (:id))]
                 (-> (list-transactions {:access-token access-token
                                         :account-id account-id})
                     :transactions
                     (first)
                     (keywordize-map)
                     (:id)
                     (subs 0 5)) => "tx_00"
                 (-> (list-transactions {:access-token "fake-access-token"
                                         :account-id account-id})
                     :transactions
                     (first)
                     (keywordize-map)
                     (:id)) => nil?
                 (-> (list-transactions {:access-token access-token
                                         :account-id "fake-account-id"})
                     :transactions
                     (first)
                     (keywordize-map)
                     (:id)) => nil?
                 (-> (list-transactions {})
                     :transactions
                     (first)
                     (keywordize-map)
                     (:id)) => nil?
                 (-> (list-transactions nil)
                     :transactions
                     (first)
                     (keywordize-map)
                     (:id)) => nil?))))


