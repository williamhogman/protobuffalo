(set-env! :repositories #(conj % ["jitpack" "https://jitpack.io"]))
(set-env! :repositories #(conj % ["clojars" "http://clojars.org/repo"]))
(set-env!
 :resource-paths #{"src"}
 :dependencies '[[com.rpl/specter "1.0.3"]
                 [org.flatland/protobuf "0.8.2-SNAPSHOT"]
                 [com.github.zensum/webhook-proto "0.1.1"]
                 [com.stuartsierra/component "0.3.2"]
                 [ring "1.6.3"]
                 [compojure "1.6.0"]
                 [ring-jetty-component "0.3.1"]
                 [reloaded.repl "0.2.4"]
                 [environ "1.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [com.cemerick/pomegranate "1.0.0"]])

(require 'protobuffalo.core)
(deftask run []
  (with-pass-thru _
    (protobuffalo.core/-main)))

(deftask uberjar
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace #{'protobuffalo.core})
   (uber)
   (jar :file "project.jar" :main 'protobuffalo.core)
   (sift :include #{#"project.jar"})
   (target)))
