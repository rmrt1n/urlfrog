(ns urlfrog.routes.shorten
  (:require [xtdb.api :as xt]
            [hiccup2.core :as h]
            [ring.util.response :as res]
            [clojure.math :as math]
            [clojure.java.io :as io]))

(defn slug [n]
  (let [charset "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"]
    (loop [n n
           acc ""]
      (if (zero? n)
        acc
        (recur (quot n 62) (str acc (get charset (rem n 62))))))))

(defn put-url [node url slug]
  (xt/submit-tx node
                [[::xt/put {:xt/id (java.util.UUID/randomUUID)
                            :short-url/long-url url
                            :short-url/slug slug
                            :short-url/created-at (java.util.Date.)}]])
  (xt/sync node))

(defn shorten [req]
  (let [url (-> req :form-params (get "url"))
        node (:db req)
        slug (slug (rand (dec (math/pow 62 7))))
        existing (xt/q (xt/db node)
                       '{:find [slug]
                         :in [long-url]
                         :where [[url :short-url/long-url long-url]
                                 [url :short-url/slug slug]]}
                       url)
        res-url (str (-> req :headers (get "host"))
                     "/"
                     (if (empty? existing)
                       (do (put-url node url slug) slug)
                       (first (first existing))))]
    (res/response
     (str
      (h/html
       [:div {:style {:padding "1rem"
                      :background-color "#bbf7d0"
                      :border-radius "var(--border-radius)"}}
        [:p
         [:strong "Here's your short URL:"]]
        [:a#short-url {:href (str "http://" res-url) :target "_blank"} res-url]
        [:button {:style "margin-left: .5rem" :onclick "copyToClipboard()"}
         (h/raw (slurp (io/resource "public/svg/clipboard-copy.svg")))
         "Copy to clipboard"]])))))
