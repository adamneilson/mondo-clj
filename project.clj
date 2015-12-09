(defproject mondo-clj "0.1.9"
  :description "A lib to hook into Mondo bank API"
  :url "https://getmondo.co.uk"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot mondo-clj.core
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.5.0"]])



