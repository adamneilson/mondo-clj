(ns mondo-clj.core-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [mondo-clj.core :refer :all]
            [cheshire.core :refer :all]))



(facts "Core get-access-token tests"
       (let [settings (-> (env :mondo-test-credentials)
                          (slurp)
                          (parse-string true))
             access-token (-> (get-access-token {:grant-type "password" 
                                      :client-id (:client-id settings)
                                      :client-secret (:client-secret settings)
                                      :username (:username settings)
                                      :password (:password settings)})
                   :access-token)]
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
               => true)
         ))

(facts "Core refresh-access-token tests"
       (fact "refresh access token"
             (-> (refresh-access-token nil)
                 :success?)
             => false))

