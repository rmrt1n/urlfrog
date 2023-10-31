(ns urlfrog.routes.base
  (:require [ring.util.response :as res]
            [urlfrog.templates :as templates]))

(defn index [_]
  (res/response
   (str
    (templates/base-layout
     [:h1 "Create a short URL with UrlFrog"]
     [:div.card {:style "max-width: 36rem"}
      [:form {:hx-post "/shorten"
              :hx-target "#result"
              (keyword "hx-on::after-request") "if (event.detail.successful) this.reset()"}
       [:div
        [:label {:for "url"} "Paste your URL here"]
        [:input {:id "url"
                 :name "url"
                 :required true
                 :type "url"
                 :size 32
                 :placeholder "https://example.com"}]]
       [:button.btn-primary {:style "display: block; margin-top: .5rem;"} "Shorten URL"]]
      [:div#result]]))))
