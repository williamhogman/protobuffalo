(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [protobuffalo.system :refer [new-system]]))

(reloaded.repl/set-init! #(new-system 3000 nil "jars-to-load"))
