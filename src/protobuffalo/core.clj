(ns protobuffalo.core
  (:require [protobuffalo.system :refer [new-system]]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))

(defn -main []
  (-> (new-system (or (Integer/parseInt (:port env)) 80) (:jitpack-token env) (or (:jars-file env) "jars-to-load"))
      (component/start)
      :jetty
      :server
      (.join)))
