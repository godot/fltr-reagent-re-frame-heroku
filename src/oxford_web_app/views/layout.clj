(ns oxford-web-app.views.layout
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn application [title & content]
  (html5 {:lang "en"}
         [:head
          [:title title]
          (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css")
          (include-css "css/style.css")
          [:body
           [:div {:class "container" :id "root"}
            [:h1 "oxford-web-app.views.layout" ]
            content ]

           (include-js "https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js")
           (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js")
           (include-js "js/out/goog/base.js")
           (include-js "js/app.js")
           [:script
            "console.log('start script');"
            "goog.require('oxford_web_app.core');"
            ]

           ]]))
