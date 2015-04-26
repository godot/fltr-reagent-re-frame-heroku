(ns oxford-web-app.core
  (:require [clojure.string :as string]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [reagent-forms.core :refer [bind-fields]]
            [oxford-web-app.handlers]
            [oxford-web-app.subs]
            [oxford-web-app.views :as bs]
            [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]])
  (:import goog.History))

(defn article-list
  []
  (let [articles (subscribe [:my-articles])]
    (fn []
      [:div
       (for [article @articles] ^{:key (:id article)} [article-listing-item article])])))


(defn article-box
  [article]
  [:div
   [:hr]
   [:div
    [article-box-panel article]]])

(defn article-listing-item
  [{:keys [id title url text]}]
  [bs/panel
   [:span
    title
    [:div.button-group.pull-right
     [:a.btn.btn-xs.btn-default {:href (article-path {:id id})} "details"]]
    ]
   text url])

(defn article-box-panel []
  (let [display-mode (reagent/atom :text)]
    (fn [{:keys [id title url] :as article}]
      [bs/panel
       [:span title
        [:div.button-group.pull-right
         [bs/small-button {:class (when (= :highlighted @display-mode) "active") :on-click #(dispatch [:analyze-text id display-mode])} "oxford-3000"]
         [bs/small-button {:class (when (= :text @display-mode) "active") :on-click #(reset! display-mode :text)} "original"]
         ]
        ]
       [:pre [bs/unsafe-html (@display-mode article)]]

       url])))

(def form-template
  [:div
   (bs/form-row "text" [:textarea.form-control {:field :textarea :id :article.text}])
   (bs/form-row "title" [:input.form-control {:field :text :id :article.title}])
   (bs/form-row "url" [:input.form-control {:field :text :id :article.url}])])

(defn form []
  (let [doc (atom {})]
    (fn []
      [:div.form-horizontal
       [:div.page-header [:h1 "Article Form"]]
       [bind-fields form-template doc]
       [:button.btn.btn-primary {:on-click #(dispatch [:save-article doc])} "Add article"]
       ])))

(defn system-messages
  []
  (let [messages (subscribe [:system-messages])
        message (first @messages)]
    (when (not-empty message)
      [:span
       [:button.btn.btn-xs.btn-default.pull-right {:on-click #(dispatch [:clear-system-messages])} "clear"]
       [:div.alert.alert-success message]])))

(defn page-with-navigation
  [content]
  [:div.container
   [nav]
   [system-messages]
   content
   ])

(defn articles-page []
  [page-with-navigation
   [:div
    [:div [article-list]]]
   ])

(defn article-form []
  [page-with-navigation
   [:div [form]]
   ])

(defn article-page
  [{:keys [id]}]
  (let [article (subscribe [:article id])]
    (fn []
      [page-with-navigation
       [:span
        [:h2 "article details page"]
        [article-box @article]]
       ])))


(defn current-page []
  [(session/get :current-page) (session/get :params)])


;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute articles-path "/articles" []
  (session/put! :current-page #'articles-page))

(secretary/defroute new-article-path "/articles/new" []
  (session/put! :current-page #'article-form))

(secretary/defroute article-path "/articles/:id" {:as params}
  (session/put! :current-page #'article-page)
  (session/put! :params params))

(defn redirect-to
  [resource]
  (secretary/dispatch! resource)
  (.setToken (History.) resource))

(secretary/defroute "*" []
  (redirect-to "/articles"))

;; -------------------------

(defn nav
  []
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.nabar-header [:a.navbar-brand "Foreign Language Text Reader ver. 0.0.1"]]
    [:ul.nav.navbar-nav
     [:li
      [:a {:href (new-article-path)} "new article"]]
     [:li
      [:a {:href (articles-path)} "articles"]]]]])


;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))
;; -------------------------

;; Render the root component
(defn init!
  []
  (hook-browser-navigation!)
  (dispatch-sync [:initialize])
  (reagent/render-component  [current-page] (.getElementById js/document "root")))
