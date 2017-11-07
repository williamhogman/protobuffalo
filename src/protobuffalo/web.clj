(ns protobuffalo.web
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [protobuffalo.loader :refer [encode-proto decode-proto]]
            [clojure.java.io :as io]
            [ring.middleware.stacktrace :refer [wrap-stacktrace-web]]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.edn :as edn]))

(defn pp-str [x]
  (with-out-str (clojure.pprint/pprint x)))

(defn- file->bytes [stream]
  (with-open [xout (java.io.ByteArrayOutputStream.)]
    (io/copy stream xout)
    (.toByteArray xout)))

(defn- stream->str [stream]
  (String. (file->bytes stream)))

(def buffalo "Buffalo buffalo Buffalo buffalo buffalo buffalo Buffalo buffalo")

(defn plain-text [text]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body text})

(defn- mk-routes [loader]
  (routes
   (GET "/" []
        {:status 200
         :headers {}
         :body (io/file "public/index.html")})
   (GET "/healthz" [] buffalo)
   (GET "/ready" [] buffalo)
   (POST "/encode-form" [cls edn]
         (if (seq cls)
           (plain-text (String. (encode-proto loader cls (edn/read-string edn))))
           "no class!"))
   (POST "/decode-form" [cls protobuf]
         (if (seq cls)
           (plain-text (pp-str (decode-proto loader cls protobuf)))
           "no class!"))
   (POST "/decode/:cls" {body :body {cls :cls} :route-params} [cls body]
         (if (seq cls)
           (pp-str (decode-proto loader cls (file->bytes body)))
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
               wrap-params
               wrap-stacktrace-web)))
  (stop [this]
    (assoc this :app nil)))

(defn new-web []
  (map->Web {}))
