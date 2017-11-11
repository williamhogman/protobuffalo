(ns protobuffalo.loader
  (:use [flatland.protobuf.core :as pb]
        [com.stuartsierra.component :as component]
        [protobuffalo.reflect :refer [classname->pb]]
        [protobuffalo.dep-loader :refer [get-loader]]
        [clojure.java.io :refer [reader]]))

(defprotocol ProtoCoder
  (decode-proto [this clsname bytes])
  (encode-proto [this clsname x])
  (reload [this]))

(defn- name->buftype [loader clsname]
  (classname->pb (get-loader (:dep-loader loader)) clsname))

(defrecord LoaderContext [dep-loader]
  component/Lifecycle
  (start [this]
    (assoc this :dep-loader dep-loader))
  (stop [this] this)
  ProtoCoder
  (decode-proto [this clsname bytes]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-load buftype bytes)))
  (encode-proto [this clsname x]
    (if-let [buftype (name->buftype this clsname)]
      (pb/protobuf-dump buftype x)))
  (reload [this]
    this))

(defn new-loader []
  (map->LoaderContext {}))
