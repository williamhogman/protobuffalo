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

(defn read-jars-file [jars]
  (line-seq (reader jars)))

(defn- reload [loader-context]
  (let [jars (read-jars-file (:jars-file loader-context))
        loader (create-loader (:jitpack-token loader-context) jars)]
    (assoc loader-context :loader loader)))

(defrecord LoaderContext [jitpack-token jars-file]
  component/Lifecycle
  (start [this]
    (reload this))
  (stop [this]
    (assoc this :loader nil))
  ProtoCoder
  (decode-proto [this clsname bytes]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-load buftype bytes)))
  (encode-proto [this clsname x]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-dump buftype x))))

(defn new-loader [jitpack-token jars-file]
  (map->LoaderContext {:jars-file jars-file :jitpack-token jitpack-token}))
