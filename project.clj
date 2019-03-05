(defproject my-cloruje-study "0.1.0-SNAPSHOT"
  :description "Service from 'my finances' - responsible to process banks accounts information"
  :url "http://https://github.com/grmelo/my-cloruje-study"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure          "1.10.0"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [io.pedestal/pedestal.route   "0.5.5"]
                 [io.pedestal/pedestal.jetty   "0.5.5"]
                 [org.clojure/data.json        "0.2.6"]
                 [com.datomic/datomic-free      "0.9.5703"]
                 [marco-fiset/lein-datomic     "0.2.1"]
                 [org.slf4j/slf4j-simple       "1.7.25"]]
  :resource-paths ["config", "resources"]
  :main ^{:skip-aot true} my-cloruje-study.core
  :target-path "target/%s"
  :datomic {:schemas ["resources/schema" ["schema.edn" "seed-data.edn"]]}
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}
             :uberjar {:aot :all}})
