(ns oxford-web-app.views.contents
  (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Hello Hiccup"]])

(defn article-form []
  (form-to {} [:get "/add"]
           [:div {:class "well"}
            [:h1 {:class "text-info"} "Oxford dictionary"]
            [:div {:class "row"
                   }
             [:row {:action "/check"}
              [:div {:class "col-lg-12"}
               (text-area {:class "form-control" :rows 20 :placeholder "Paste your text here"} "body")]]
             ]
            [:hr]
            (submit-button {:class "btn btn-lg btn-success btn-block"} "submit")
            ]

           ))

(defn not-found []
  [:div
   [:h1 {:class "info-warning"} "Page Not Found"]
   [:p "There's no requested page. "]
   (link-to {:class "btn btn-primary"} "/" "Take me to Home")])
