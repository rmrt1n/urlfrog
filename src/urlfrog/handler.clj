(ns urlfrog.handler
  (:require [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as params]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as res]
            [xtdb.api :as xt]
            [urlfrog.routes.base :refer [index]]
            [urlfrog.routes.shorten :refer [shorten]]
            [urlfrog.routes.slug :refer [slug]]))

(defn all [req]
  (let [node (:db req)]
    (res/response
     (str (xt/q (xt/db node)
                '{:find [url (pull url [*])]
                  :where [[url :xt/id _]
                          [url :short-url/slug]]})))))

(def middleware-db
  {:name ::db
   :compile (fn [{:keys [db]} _]
              (fn [handler]
                (fn [req]
                  (handler (assoc req :db db)))))})

(defn routes [db]
  (ring/ring-handler
   (ring/router
    [["/"        {:get index}]
     ["/shorten" {:post shorten}]
     ["/all"     {:get all}]
     ["/:slug"   {:get slug}]]
    {:data {:db db
            :middleware [params/parameters-middleware
                         wrap-keyword-params
                         middleware-db]}
     :conflicts nil})
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (res/not-found "Not found")
      :method-not-allowed (-> (res/response "Not allowed") (res/status 405))
      :not-acceptable (-> (res/response "Not acceptable") (res/status 406))}))))
