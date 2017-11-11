(ns protobuffalo.core
  (:require [protobuffalo.system :refer [new-system]]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]))

(defn -main []
  (-> (new-system)
      (component/start)
      :jetty
      :server
      (.join)))
