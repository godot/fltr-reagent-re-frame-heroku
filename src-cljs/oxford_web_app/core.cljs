(ns oxford-web-app.core
  (:require [clojure.string :as string]
            [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [oxford-web-app.handlers]
            [oxford-web-app.subs]
            [oxford-web-app.views :as bs]
            [re-frame.core :refer [dispatch
                                   dispatch-sync
                                   subscribe]]
            ))


(enable-console-print!)

(defn article-list
  []
  (let [articles (subscribe [:my-articles])]
    (fn []
      [:div
       (for [article @articles] ^{:key (:id article)} [article-box article])])))


(defn article-box [article]
  [:div.row
   [:hr]
   [:div.col-md-8
    [article-box-panel article]]])

(defn article-box-panel []
  (let [display-mode (reagent/atom :text)]
    (fn [{:keys [id title url] :as article}]
      [bs/panel
       [:span title
        [:div.button-group.pull-right
         [bs/small-button {:on-click #(dispatch [:analyze-text id])} "analyze"]
         (if (not-empty (:highlighted article))
           [bs/small-button {:on-click #(reset! display-mode :highlighted)} "oxford-3000"])
         [bs/small-button {:on-click #(reset! display-mode :text)} "original"]
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
       [:button.btn.btn-primary {:on-click #(dispatch [:save-article (:article @doc)])} "Add article"]
       ])))

(defn page []
  [:div
   [:div.row [article-list]]
   [:div.row [form]]
   ])

;; Render the root component
(defn start []
  (dispatch-sync [:initialize])
  (reagent/render-component  [page] (.getElementById js/document "root")))


(fw/start {
  ;; configure a websocket url if you are using your own server
  ;; :websocket-url "ws://localhost:3449/figwheel-ws"

  ;; optional callback
  :on-jsload (fn [] (print "reloaded"))

  ;; The heads up display is enabled by default
  ;; to disable it:
  ;; :heads-up-display false

  ;; when the compiler emits warnings figwheel
  ;; blocks the loading of files.
  ;; To disable this behavior:
  ;; :load-warninged-code true

  ;; if figwheel is watching more than one build
  ;; it can be helpful to specify a build id for
  ;; the client to focus on
  ;; :build-id "example"
})

(start)
