(ns protobuffalo.core
  (:require [protobuffalo.system :refer [new-system]]
            [com.stuartsierra.component :as component]))

(def jars ["https://jitpack.io/com/github/zensum/scheduler-proto/f73b532/scheduler-proto-f73b532.jar"])

(defn -main []
  (-> (new-system 3000 nil jars)
      (component/start)
      :jetty
      :server
      (.join)))
