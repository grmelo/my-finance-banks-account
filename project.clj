(defproject my-finance-banks-account "0.1.0-SNAPSHOT"
  :description "Service from 'my finances' - responsible to process banks accounts information"
  :url "http://https://github.com/grmelo/my-my-finance-banks-account"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [io.pedestal/pedestal.route   "0.5.5"]
                 [io.pedestal/pedestal.jetty   "0.5.5"]
                 [org.clojure/data.json        "0.2.6"]
                 [org.slf4j/slf4j-simple       "1.7.25"]]
  :main ^{:skip-aot true} my-finance-banks-account.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
