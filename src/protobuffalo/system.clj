(ns protobuffalo.system
  (:require
   [ring.component.jetty :refer [jetty-server]]
   [protobuffalo.web :refer [new-web]]
   [protobuffalo.loader :refer [new-loader]]
   [com.stuartsierra.component :as component]))

(defn new-system [port jitpack-token jars]
  (-> (component/system-map
       :web (new-web)
       :loader (new-loader jitpack-token jars)
       :jetty (jetty-server {:port port}))
      (component/system-using
       {:web {:loader :loader}
        :jetty {:app :web}})))
