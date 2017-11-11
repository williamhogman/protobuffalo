(ns protobuffalo.core
  (:require [protobuffalo.system :refer [new-system]]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))

(defn -main []
  (-> (new-system (or (Integer/parseInt (:port env)) 80) [["jitpack" "https://jitpack.io"]] '[[com.github.zensum/scheduler-proto "master"]])
      (component/start)
      :jetty
      :server
      (.join)))
