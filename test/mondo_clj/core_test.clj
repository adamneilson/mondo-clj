(ns mondo-clj.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [mondo-clj.core :refer :all]
            [cheshire.core :refer :all]))



(facts "Core get-access-token tests"
       (let [access-token (-> (get-access-token {:grant-type "password" 
                                      :client-id (:mondo_test_client_id env)
                                      :client-secret (:mondo_test_client_secret env)
                                      :username (:mondo_test_username env)
                                      :password (:mondo_test_password env)})
                   :access-token)]
         (println ":client-id" (:mondo_test_client_id env))
         (println ":client-secret" (:mondo_test_client_secret env))
         (println ":username" (:mondo-test-username env))
         (println ":password" (:mondo_test_password env))

         (fact "get a token from the api - wrong grant-type - missing fields"
               (-> (get-access-token {:grant-type "token"}) 
                   :success?)
               => false)

         (fact "get a token from the api - correct grant-type - missing fields"
               (-> (get-access-token {:grant-type "password"}) 
                   :success?)
               => false)

         (fact "get a token from the api - correct grant-type"
               access-token =not=> nil)
         
         (fact "Check who I am"
               (-> (whoami access-token)
                  :authenticated)
               => true)))

(facts "Core refresh-access-token tests"
       (fact "refresh access token"
             (-> (refresh-access-token nil)
                 :success?)
             => false))

