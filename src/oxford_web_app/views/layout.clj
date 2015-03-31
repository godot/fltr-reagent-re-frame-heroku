(ns oxford-web-app.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css")
          (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js")
          (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js")
          (include-js "js/script.js")

          [:body
           [:div {:class "container"}
            [:h1 "oxford-web-app.views.layout" ]
            content ]]]))
