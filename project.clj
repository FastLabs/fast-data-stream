(defproject fast-data-stream "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/core.async "0.4.474"]
                 [alaisi/postgres.async "0.8.0"]]
  :main ^:skip-aot fast-data-stream.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev     {:source-paths ["src" "test" "dev"]
                       :dependencies [[org.clojure/test.check "0.9.0"]]}})
