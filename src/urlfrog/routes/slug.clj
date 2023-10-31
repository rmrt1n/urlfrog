(ns urlfrog.routes.slug
  (:require [xtdb.api :as xt]
            [ring.util.response :as res]))

(defn slug [req]
  (let [slug (-> req :uri (subs 1))
        node (:db req)
        existing (xt/q (xt/db node)
                       '{:find [long-url]
                         :in [slug]
                         :where [[e :slug slug]
                                 [e :long-url long-url]]}
                       slug)]
    (if (empty? existing)
      (res/not-found "not found")
      ;; use 302 here instead of 301 to disable permanenent cache by browsers
      (res/redirect (first (first existing))))))
