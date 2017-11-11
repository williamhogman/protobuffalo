(ns protobuffalo.system
  (:require
   [ring.component.jetty :refer [jetty-server]]
   [protobuffalo.web :refer [new-web]]
   [protobuffalo.loader :refer [new-loader]]
   [protobuffalo.dep-loader :refer [new-deploader]]
   [com.stuartsierra.component :as component]
   [aero.core :refer [read-config]]))

(defn configure [system]
  (let [config (read-config "config.edn")]
    (merge-with merge system config)))

(defn new-system []
  (-> (component/system-map
       :web (new-web)
       :dep-loader (new-deploader)
       :loader (new-loader)
       :jetty (jetty-server {}))
      configure
      (component/system-using
       {:loader {:dep-loader :dep-loader}
        :web {:loader :loader}
        :jetty {:app :web}})))
