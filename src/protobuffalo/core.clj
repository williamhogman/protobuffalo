(ns protobuffalo.core
  (:require [protobuffalo.system :refer [new-system]]
            [com.stuartsierra.component :as component]))

(defn -main []
  (-> (new-system)
      (component/start)))
