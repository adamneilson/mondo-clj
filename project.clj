(defproject mondo-clj "0.1.11"
  :description "A lib to hook into Mondo bank API"
  :url "https://getmondo.co.uk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot mondo-clj.core
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.5.0"]]
  :dev-dependencies [[midje "1.8.3"  :exclusions [org.clojure/clojure]]
                     [lein-midje "3.2"]
                     [environ "1.0.2"]]
  :profiles {:uberjar {:omit-source true
                       :env {:production true}
                       :aot :all}
             :production {}
             :dev {:global-vars {*warn-on-reflection* true}
                   :dependencies [[midje "1.8.3" :exclusions [org.clojure/clojure]]
                                  [lein-midje "3.2"]
                                  [environ "1.0.2"]]
                   :env {:dev true}}})



