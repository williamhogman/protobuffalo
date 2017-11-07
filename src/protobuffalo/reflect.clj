(ns protobuffalo.reflect
  (:import
   (flatland.protobuf PersistentProtocolBufferMap
                      PersistentProtocolBufferMap$Def
                      PersistentProtocolBufferMap$Def$NamingStrategy
                      Extensions)
   (com.google.protobuf GeneratedMessage
                        CodedInputStream
                        Descriptors$Descriptor)))

;; Hacky function to create protobuf's that works w/ current Clojure
(defn- mkpbdef [descriptor]
  (let [naming-strategy PersistentProtocolBufferMap$Def/convertUnderscores
        size-limit 67108864]
    (PersistentProtocolBufferMap$Def/create descriptor naming-strategy size-limit)))

(defn- invoke-static-nullary [cls method]
  (let [mtd (.getMethod cls method (make-array java.lang.Class 0))]
    (.invoke mtd nil (make-array Object 0))))

(defn- class->pb [cls]
  (if cls
    (mkpbdef (invoke-static-nullary cls "getDescriptor"))))

(defn classname->pb [loader name]
  (let [cls (.loadClass loader name true)]
    (class->pb cls)))
