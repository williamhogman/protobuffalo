(ns protobuffalo.core

  )

(comment
  (let [loader (map->LoaderContext {:jitpack-token "foo" :jars []})]
    (component/start loader))

  (def jars ["https://jitpack.io/com/github/zensum/scheduler-proto/f73b532/scheduler-proto-f73b532.jar"])
  (def cl (load-jars jars))
  ;; we could possible get stuff out of this
  (.getLoadedClasses cl)

  (def clsname "se.zensum.scheduler_proto.Scheduler$Task")
  (pb/protobuf-dump (pb/protobuf (classname->pb cl clsname) :id 1 :key 123 :body "foobar")))
