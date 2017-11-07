(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [protobuffalo.system :refer [new-system]]))

(def jars ["https://jitpack.io/com/github/zensum/scheduler-proto/f73b532/scheduler-proto-f73b532.jar"])

(reloaded.repl/set-init! #(new-system 3000 nil jars))
