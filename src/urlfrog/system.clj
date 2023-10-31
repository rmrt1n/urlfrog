(ns urlfrog.system
  (:require [aero.core :as aero]
            [integrant.core :as ig]
            [ring.adapter.jetty :as server]
            [xtdb.api :as xt]
            [urlfrog.handler :as handler]
            [clojure.java.io :as io]))

(defn app [db]
  (handler/routes db))

(defmethod ig/init-key :server/http [_ {:keys [port handler]}]
  (println "server started on port: " port)
  (server/run-jetty handler {:port port :join? false}))

(defmethod ig/init-key :handler/ring [_ {:keys [db]}]
  (app db))

(defmethod ig/init-key :db.xtdb/node [_ {:keys [dir]}]
  (letfn [(kv-store [dir]
            {:kv-store {:xtdb/module 'xtdb.rocksdb/->kv-store
                        :db-dir (io/file dir)
                        :sync? true}})]
    (xt/start-node
     {:xtdb/tx-log (kv-store (str dir "/tx-log"))
      :xtdb/document-store (kv-store (str dir "/doc-store"))
      :xtdb/index-store (kv-store (str dir "/index-store"))})))

(defmethod ig/halt-key! :server/http [_ server]
  (.stop server))

(defmethod ig/halt-key! :db.xtdb/node [_ xtdb-node]
  (.close xtdb-node))

(defmethod aero/reader 'ig/ref [_opts _tag value]
  (ig/ref value))

(def config (aero/read-config (io/resource "config.edn")))

(defn -main [& _args]
  (ig/init config))
