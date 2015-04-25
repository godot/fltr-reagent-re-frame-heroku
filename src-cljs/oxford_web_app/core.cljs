(ns oxford-web-app.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as string]
            [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [oxford-web-app.articles.core :as articles]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
            [ajax.core :refer [GET POST]]
            ))


;; html elements

(defn bs-row [label input]
  [:div.row.form-group
   [:div.col-md-2 [:label label]]
   [:div.col-md-5 input]])

(defn bs-panel [title text footer]
  [:div.panel.panel-default
   [:div.panel-heading
    [:strong title]]
   [:div.panel-body text]
   [:div.panel-footer footer]])

(enable-console-print!)

;;re-frame stuff

(def initial-state
  {
   :my-articles articles/all
   :my-dictionary  [] })

;; handlers
(register-handler
 :initialize
 (fn
   [db _]
   (merge db initial-state)))

(register-handler
 :analyze-article
 (fn
   [db [_ id]]
   (let [txt (get-in db [:my-articles id])]
     (POST "/api/check"
        {:params {:body (:text txt)}
         :handler #(dispatch [:article-analyzed id %1])
         :error-handler error-handler
         :format :json
         :response-format :json
         :keywords? true})
     (assoc db :loading? true)
     )))

(register-handler
 :article-analyzed
 (fn
   [db [_ id response]]
   (update-in db [:my-articles id] merge (select-keys  response [:highlighted]))
   ))

(register-handler
 :save-article
 (fn
   [db [_ article]]
   (let
       [articles (:my-articles db)
        next-id (count articles)
        article (assoc article :id next-id :key next-id)
        ]
     (when (not-empty (:text article))
       (assoc-in db [:my-articles next-id] article)))))

;;subscribers
(register-sub
 :my-articles
 (fn
   [db]
   (reaction (vals (:my-articles @db)))))


;; other
(defn article-list
  []
  (let [all-articles (subscribe [:my-articles])]
    (fn []
      [:div
       (for [article @all-articles] ^{:key (:id article)} [article-box article])])))

(defn article-box []
  (let [display-mode (reagent/atom :text)]
    (fn [{:keys [id title url] :as article}]
      [:div.row
       [:hr]
       [:div.col-md-8
        [bs-panel
         [:span (str id ". " title)
          [:div.button-group.pull-right
;;           (if (empty? (:highlighted article)))
           [:button.btn.btn-xs.btn-success {:on-click #(dispatch [:analyze-article id])} "analyze"]
           (if (not-empty (:highlighted article))
             [:button.btn.btn-xs.btn-primary {:on-click #(reset! display-mode :highlighted)} "oxford-3000"])
           [:button.btn.btn-xs.btn-primary {:on-click #(reset! display-mode :text)} "original"]
           ]
          ] ;;title
         [:div.body [:pre {:dangerouslySetInnerHTML {:__html (@display-mode article)}}]]

         url]]

       ]))
    )

(def form-template
  [:div
   (bs-row "text" [:textarea.form-control {:field :textarea :id :article.text}])
   (bs-row "title" [:input.form-control {:field :text :id :article.title}])
   (bs-row "url" [:input.form-control {:field :text :id :article.url}])])

(defn form []
  (let [doc (atom {})]
    (fn []
      [:div.form-horizontal
       [:div.page-header [:h1 "Article Form"]]
       [bind-fields form-template doc]
       [:button.btn.btn-primary {:on-click #(dispatch [:save-article (:article @doc)])} "save article"]
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
