(ns protobuffalo.system
  (:require
   [ring.component.jetty :refer [jetty-server]]
   [protobuffalo.web :refer [new-web]]
   [protobuffalo.loader :refer [new-loader]]
   [protobuffalo.dep-loader :refer [new-deploader]]
   [com.stuartsierra.component :as component]))

(defn new-system [port repos deps]
  (-> (component/system-map
       :web (new-web)
       :dep-loader (new-deploader repos deps)
       :loader (new-loader)
       :jetty (jetty-server {:port port}))
      (component/system-using
       {:loader {:dep-loader :dep-loader}
        :web {:loader :loader}
        :jetty {:app :web}})))
