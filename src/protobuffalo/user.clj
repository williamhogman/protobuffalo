(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [protobuffalo.system :refer [new-system]]))


(reloaded.repl/set-init! #(new-system 3000 ["jitpack" "https://jitpack.io"] [[com.github.zensum/scheduler-proto "master"]]))
