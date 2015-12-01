(ns mondo-clj.api
  (:require 
    [org.httpkit.client :as http]
    [cheshire.core :refer :all]))


;(def api-url "https://api.getmondo.co.uk")
(def api-url "https://production-api.gmon.io")



(def basic-request-options {:user-agent "mondo-clj-lib"
                            :headers {"X-Api-Version" "unknown"}
                            :keepalive 3000          ; Keep the TCP connection for 3000ms
                            :timeout 1000      ; connection timeout and reading timeout 1000ms
                            :filter (http/max-body-filter (* 1024 1000)) ; reject if body is more than 1000k
                            :insecure? false ; Need to contact a server with an untrusted SSL cert?
                            :follow-redirects false})

(defn- keywordize-map [m] (reduce (fn [m [k v]] 
                                    (assoc m (-> k
                                                 (str)
                                                 (clojure.string/trim)
                                                 (clojure.string/replace #"_" "-")
                                                 (keyword)) v)) {} m))

(defn- deep-merge
   "Recursively merges maps. If keys are not maps, the last value wins."
   [& vals]
   (if (every? map? vals)
     (apply merge-with deep-merge vals)
     (last vals)))



(defn get-error [error-code]
  (cond
    (= error-code 200) {:success? true :error-code 200 :error-name "OK" :error-body "All is well."}
    (= error-code 400) {:success? false :error-code 400 :error-name "Bad Request" :error-body "Your request has missing parameters or is malformed."}
    (= error-code 401) {:success? false :error-code 401 :error-name "Unauthorized" :error-body "Your request is not authenticated."}
    (= error-code 403) {:success? false :error-code 403 :error-name "Forbidden" :error-body "Your request is authenticated but has insufficient permissions."}
    (= error-code 405) {:success? false :error-code 405 :error-name "Method Not Allowed" :error-body "You are using the incorrect HTTP verb. Double check whether it should be POST/GET/DELETE/etc."}
    (= error-code 404) {:success? false :error-code 404 :error-name "Page Not Found" :error-body "The endpoint requested does not exist."}
    (= error-code 406) {:success? false :error-code 406 :error-name "Not Acceptable" :error-body "Your application does not accept the content format returned according to the Accept headers sent in the request."}
    (= error-code 429) {:success? false :error-code 429 :error-name "Too Many Requests" :error-body "Your application is exceeding its rate limit. Back off, buddy. :p"}
    (= error-code 500) {:success? false :error-code 500 :error-name "Internal Server Error" :error-body "Something is wrong on our end. Whoopsie."}
    (= error-code 504) {:success? false :error-code 504 :error-name "Gateway Timeout" :error-body "Something has timed out on our end. Whoopsie."}
    :else (throw (Throwable. "An unknown error has occurred."))))
    


(defn GET 
  ([uri access-token] (GET uri access-token {}))
  ([uri access-token params]
   (let [url (str api-url uri)
         options (merge basic-request-options {:url url
                                               :method :get
                                               :oauth-token (when (seq access-token) access-token)
                                               :query-params params})
         {:keys [status body error]} @(http/get url options)]
     (if error
       (get-error status)
       (-> (decode body)
           (assoc :status (get-error status))
           (keywordize-map))))))




(defn POST 
  ([uri access-token] (POST uri access-token {}))
  ([uri access-token params]
   (let [url (str api-url uri)
         options (merge basic-request-options {:url url
                                               :method :post
                                               :oauth-token (when (seq access-token) access-token)
                                               :form-params params})
         {:keys [status body error]} @(http/post url options)]
     (if error
       (get-error status)
       (-> (decode body)
           (assoc :status (get-error status))
           (keywordize-map))))))


(defn PATCH [uri access-token metadata]
  (let [url (str api-url uri)
        ;"metadata[$key1]=$value1" 
        options (merge basic-request-options 
                       {:url url
                        :method :patch
                        :oauth-token (when (seq access-token) access-token)
                        :form-params metadata})
        {:keys [status body error]} @(http/post url options)]
    (if error
      (get-error status)
      (-> (decode body)
          (assoc :status (get-error status))
          (keywordize-map)))))



