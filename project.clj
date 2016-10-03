(defproject forget-about-it "0.1.0-SNAPSHOT"
  :description "Nothing to see here yet."
  :url "https://github.com/decomputed/forget-about-it"
  :license {:name "GPL3"
            :url "https://www.gnu.org/licenses/gpl-3.0-standalone.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot forget-about-it.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
