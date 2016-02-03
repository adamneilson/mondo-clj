(ns mondo-clj.util-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [mondo-clj.util :refer :all]))



(facts "Util tests"

       (fact "Checks for not nil"
             (not-nil? "xxxx") => true
             (not-nil? ["xxxx"]) => true
             (not-nil? []) => true
             (not-nil? nil) => false)

       (fact "make a map clojure shaped"
             (keywordize-map {"a" "a"}) => {:a "a"}
             (keywordize-map {"a_a" "a"}) => {:a-a "a"}
             (keywordize-map {"a a" "a"}) => {:a-a "a"}
             (keywordize-map {}) => {}
             (keywordize-map []) => {}
             (keywordize-map "xxx") => {}
             (keywordize-map nil) => {})


       (fact "make a map mondo shaped"
             (underscore-keys {"a" "a"}) => {:a "a"}
             (underscore-keys {"a-a" "a"}) => {:a_a "a"}
             (underscore-keys {"a a" "a"}) => {:a_a "a"}
             (underscore-keys nil) => {})


       (fact "remove nils from map"
             (remove-nils {:a nil :b "b" :c "c"}) => {:b "b" :c "c"}
             (remove-nils {:a "a" :b "b" :c "c"}) => {:a "a" :b "b" :c "c"}
             (remove-nils {:a nil :b nil :c nil}) => {}
             (remove-nils nil) => {})

             
       (fact "Is the value between the high and low vals (inclusive)."
             (is-between? 10 1 100) => true
             (is-between? 10 -100 100) => true
             (is-between? 100000 1 1000000) => true
             (is-between? 0.001 0 1) => true
             (is-between? -100 1 100) => false
             (is-between? "xxx" 1 100) => false
             (is-between? "xxx" nil 100) => false
             (is-between? "xxx" 1 nil) => false
             (is-between? nil 1 100) => false)


       (fact "Do any pre-processing or transformations on map."
             (pre-process-vals {:a "a"}) => {:a "a"}
             (pre-process-vals {"a" "a"}) => {:a "a"}
             (pre-process-vals {:a "   a    "}) => {:a "a"}
             (pre-process-vals {}) => {}
             (pre-process-vals nil) => {})

       
       (fact "hex-colour parser"
             (hex-colour "#aaax") => "#AAA"
             (hex-colour "xxx#aaaxxx") => "#AAA"
             (hex-colour "#xaax") => nil?
             (hex-colour nil) => nil?
             (hex-colour "#AAA") => "#AAA"
             (hex-colour "#AAAAAA") => "#AAAAAA"
             (hex-colour "#aAaAaA") => "#AAAAAA"
             (hex-colour "#bbbbbb") => "#BBBBBB")


       (fact "Convert a clojure instant to a zulu formatted date string"
             (instant-to-zulu #inst "2000-01-01T00:00:00.000-00:00") => "2000-01-01T00:00:00Z"
             (instant-to-zulu "xxx") => nil?
             (instant-to-zulu nil) => nil?)


       (fact "Convert a zulu formatted date string to a clojure instant"
             (zulu-to-instant "2000-01-01T00:00:00Z") => #inst "2000-01-01T00:00:00.000-00:00"
             (zulu-to-instant "2000-00-01T00:00:00Z") => #inst "1999-12-01T00:00:00.000-00:00"
             (zulu-to-instant "foobar") => (throws Exception)
             (zulu-to-instant nil) => nil?)


       (fact "Recursively merges maps. If keys are not maps, the last value wins."
             (deep-merge {:a "a"} {:b "b"}) => {:a "a" :b "b"}
             (deep-merge {:a "a"} "foo" "bar") => "bar"
             (deep-merge {:a "a"} "foo" nil) => nil?
             (deep-merge nil) => nil?
             (deep-merge true) => true?
             (deep-merge "foo" "bar" {:a "a"} ) => {:a "a"})


       (fact "Round a double to the given precision (number of significant digits)"
             (round2 1.0001 2) => 1.0
             (round2 -1.0001 2) => -1.0
             (round2 1.0001 4) => 1.0001
             (round2 1 4) => 1.0
             (round2 -1 4) => -1.0
             (round2 nil 4) => 0.0
             (round2 "xxx" 4) => 0.0
             (round2 "xxx" "xxx") => 0.0
             (round2 1 nil) => 0.0)

       
       (fact "coerce-to-double-monetary-amount"
             (coerce-to-double-monetary-amount 1) => 0.01
             (coerce-to-double-monetary-amount 500) => 5.0
             (coerce-to-double-monetary-amount 50000) => 500.0
             (coerce-to-double-monetary-amount -500) => -5.0
             (coerce-to-double-monetary-amount "xxx") => nil?
             (coerce-to-double-monetary-amount nil) => nil?
             (coerce-to-double-monetary-amount []) => nil?
             (coerce-to-double-monetary-amount [1 2 3]) => nil?)
       
       )



