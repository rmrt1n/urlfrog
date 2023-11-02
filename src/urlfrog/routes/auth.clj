(ns urlfrog.routes.auth
  (:require
   [ring.util.response :as res]
   [urlfrog.templates :as templates]))

(defn sign-in [_req]
  (res/response
   (str
    (templates/base-layout
     [:h1 "Sign up"]
     [:form.space-y-2 {:method "post"
                       :action "/sign-up"
                       :style "max-width: 28rem"}
      [:div
       [:label {:for "email"} "Email"]
       [:input#email {:name        "email"
                      :required    true
                      :type        "email"
                      :placeholder "johndoe@example.com"}]]
      [:button "Sign in"]]))))
