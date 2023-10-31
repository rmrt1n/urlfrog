(ns urlfrog.templates
  (:require [hiccup2.core :as h]))

(defn base-layout [& children]
  (h/html
   [:head
    [:title "UrlFrog"]
    [:meta {:charset "utf-8"
            :name "viewport"
            :content "width=device-width, initial-scale=1.0"}]]
   [:link
    {:rel "stylesheet"
     :href "https://www.unpkg.com/modern-normalize@2.0.0/modern-normalize.css"}]
   [:link {:rel "stylesheet" :href "/css/clam.css"}]
   [:link {:rel "stylesheet" :href "/css/style.css"}]
   [:script {:src "https://unpkg.com/htmx.org@1.9.6"}]
   [:script {:src "/js/script.js"}]
   [:body
    [:header
     [:nav
      [:a {:href "/" :style "display: flex; align-items: center; gap: .5rem"}
       [:img {:src "/imgs/logo.png" :alt "logo" :width 28}] "UrlFrog"]
      [:div
       [:a.btn {:href "/sign-in"} "Sign in"]
       [:a.btn.btn-primary {:href "/sign-up"} "Sign up"]]]]
    [:main
     children]]))
