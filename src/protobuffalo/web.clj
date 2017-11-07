(ns protobuffalo.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [protobuffalo.loader :refer [encode-proto decode-proto]]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :refer [wrap-stacktrace-web]]
            [clojure.edn :as edn]))

(defn- file->bytes [stream]
  (with-open [xout (java.io.ByteArrayOutputStream.)]
    (io/copy stream xout)
    (.toByteArray xout)))

(defn- stream->str [stream]
  (String. (file->bytes stream)))

(defn- mk-routes [loader]
  (routes
   (GET "/healthz" [] "top kek")
   (GET "/ready" [] "top kek")
   (GET "/echo/:foo" request (str request))
   (POST "/decode/:cls" {body :body {cls :cls} :route-params} [cls body]
         (if (seq cls)
           (decode-proto loader cls (file->bytes body))
           "no class!"))
   (POST "/encode/:cls" {body :body {cls :cls} :route-params}
         (if (seq cls)
           (String. (encode-proto loader cls (edn/read-string (stream->str body))))
           "no class!"))))

(defrecord Web [loader]
  component/Lifecycle
  (start [this]
    (assoc this :handler
           (-> (mk-routes loader)
               wrap-stacktrace-web)))
  (stop [this]
    (assoc this :app nil)))

(defn new-web []
  (map->Web {}))
