(ns protobuffalo.system
  (:require
   [ring.component.jetty :refer [jetty-server]]
   [protobuffalo.web :refer [new-web]]
   [protobuffalo.loader :refer [new-loader]]
   [com.stuartsierra.component :as component]))

(defn new-system []
  (-> (component/system-map
       :web (new-web)
       :loader (new-loader)
       :jetty (jetty-server {:port 3000}))
      (component/system-using
       {:web {:loader :loader}
        :jetty {:app :web}})))
