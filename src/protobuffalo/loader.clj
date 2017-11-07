(ns protobuffalo.loader
  (:use [flatland.protobuf.core :as pb]
        [com.stuartsierra.component :as component]
        [protobuffalo.jcl :refer [create-loader]]
        [protobuffalo.reflect :refer [classname->pb]]))

(defprotocol ProtoCoder
  (decode-proto [this clsname bytes])
  (encode-proto [this clsname x]))

(defn- name->buftype [loader clsname]
  (classname->pb (:loader loader) clsname))

(defrecord LoaderContext [jitpack-token jars]
  component/Lifecycle
  (start [this]
    (let [loader (create-loader jitpack-token jars)]
      (assoc this :loader loader)))
  (stop [this]
    (assoc this :loader nil))
  ProtoCoder
  (decode-proto [this clsname bytes]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-load buftype bytes)))
  (encode-proto [this clsname x]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-dump buftype x))))

(defn new-loader []
  (map->LoaderContext
   {:jars ["https://jitpack.io/com/github/zensum/scheduler-proto/f73b532/scheduler-proto-f73b532.jar"]}))
