(ns user
  (:require [integrant.repl :as ig-repl]
            [clojure.tools.namespace.repl :as repl]
            [urlfrog.system :as system]))

(ig-repl/set-prep! (fn [] system/config))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(repl/set-refresh-dirs "src" "resources/public")
(go)

(comment
  (go)
  (halt)
  (reset)
  (reset-all))
