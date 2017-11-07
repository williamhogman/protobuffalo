(set-env! :repositories #(conj % ["jitpack" "https://jitpack.io"]))
(set-env! :repositories #(conj % ["clojars" "http://clojars.org/repo"]))
(set-env!
 :resource-paths #{"src"}
 :dependencies '[[com.rpl/specter "1.0.3"]
                 [org.flatland/protobuf "0.8.2-SNAPSHOT"]
                 [com.github.zensum/webhook-proto "0.1.1"]
                 [org.xeustechnologies/jcl-core "2.8"]
                 [com.stuartsierra/component "0.3.2"]
                 [ring "1.6.3"]
                 [compojure "1.6.0"]
                 [ring-jetty-component "0.3.1"]
                 [reloaded.repl "0.2.4"]])

(require 'protobuffalo.core)
(deftask run []
  (with-pass-thru _
    (protobuffalo.core/-main)))
