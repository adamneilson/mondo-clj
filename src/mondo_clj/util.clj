(ns mondo-clj.util)



(defn keywordize-map [m] (reduce (fn [m [k v]] 
                                   (assoc m (-> k
                                                (str)
                                                (clojure.string/trim)
                                                (clojure.string/replace #"_" "-")
                                                (keyword)) v)) {} m))


(defn underscore-keys [m] (reduce (fn [m [k v]] 
                                   (assoc m (-> k
                                                (name)
                                                (clojure.string/trim)
                                                (clojure.string/replace #"-" "_")
                                                (keyword)) v)) {} m))


(defn remove-nils 
  "Removes all nil valued kv pairs from a map" 
  [m]
  (apply dissoc m (for [[k v] m :when (nil? v)] k)))




(defn prepare-map [m]
  (-> m
      (remove-nils)
      (underscore-keys)))




(def not-nil? (complement nil?))


(defn is-valid-txn-id? "Does the format of the txn id look valid?" [transaction-id]
  (not-nil? (re-find #"tx\_[A-Za-z0-9]*" transaction-id)))


(defn hex-colour "doc-string" [s]
  (when (string? s)
    (re-find #"\#[a-fA-F0-9]{3,6}" s)))


(defn pre-process-vals 
  "Do any pre-processing or transformations on map." 
  [m]
  (reduce (fn [m [k v]] 
            (assoc m k (if (string? v) 
                         (clojure.string/trim v)
                         v))) {} m))

(defn is-between? 
  "Is the value between the high and low vals (inclusive)." 
  [value low high]
  (and (>= value low)
       (<= value high)))


(defn instant-to-zulu "doc-string" [inst]
  (if (= (type inst) java.util.Date)
    (.format (java.text.SimpleDateFormat. 
               "yyyy-MM-dd'T'HH:mm:ss'Z'") inst)
    inst))

(defn zulu-to-instant "doc-string" [zulu]
  (when (seq zulu)
    (.parse (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss") 
            (-> zulu (clojure.string/split #"\.") first))))


(defn deep-merge
  "Recursively merges maps. If keys are not maps, the last value wins."
  [& vals]
  (if (every? map? vals)
    (apply merge-with deep-merge vals)
    (last vals)))


(defn round2
  "Round a double to the given precision (number of significant digits)"
  [d precision]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))


(defn coerce-to-double-monetary-amount 
  "doc-string" 
  [i]
  (println "coerce-to-double-monetary-amount: " i)
  (when (not (nil? i))
    (-> i
      (* 0.01)
      (round2 2))))
