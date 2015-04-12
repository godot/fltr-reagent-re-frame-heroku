(ns oxford-web-app.views.contents
  (:use [hiccup.form]
        [hiccup.element :only (link-to)]))

(defn index []
  [:div {:id "content"}
   [:h1 {:class "text-success"} "Hello Hiccup"]])

(defn oxford [dict]
           [:div
            [:h1 {:class "text-info"} "Oxford dictionary"]
            [:table {:class "table table.bordered table.hovered"}
             [:thead
              [:tr
               [:th "word"]
               [:th "type"]
               [:th "url"]]]
             (for [w dict]
               (let [{:keys [word url type]} w]
                 [:tr
                  [:td word ]
                  [:td type ]
                  [:td (link-to url "oxford") ]]
                 ))]])

(defn summary [text]
           [:div
            [:h1 {:class "text-info"} "Oxford dictionary"]
            [:div {:class "well"}(:text text)]
            [:div {:class "well"}(:highlighted text)]
            [:table {:class "table table.bordered table.hovered"}
             [:thead
              [:tr
               [:th "orig"]
               [:th "stemm"]
               [:th "oxford"]]]
             (for [word (:stemm text)]
               (let [{:keys [orig stemm oxford?]} word]
                 [:tr {:class (if oxford? "success" "danger")}
                  [:td orig ]
                  [:td stemm ]
                  [:td oxford? ]]
                 ))]])

(defn article-form []
  (form-to {} [:get "/check"]
           [:div {:class "well"}
            [:h1 {:class "text-info"} "Oxford dictionary"]
            [:div {:class "row"}
             [:row
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
