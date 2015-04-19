(ns oxford-web-app.core
  (:require [clojure.string :as string]
            ;; [figwheel.client :as fw]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [oxford-web-app.articles.core :as articles]
            ))

(enable-console-print!)

(defn row [label input]
  [:div.row.form-group
   [:div.col-md-2 [:label label]]
   [:div.col-md-5 input]])

(defn article-list []
  [:div
   (for [w (:my-articles @app-state)] [display-article w])]
  )


(defn bs-panel [title body footer]
  [:div.panel.panel-default
   [:div.panel-heading
    [:strong title]]
   [:div.panel-body
    [:pre body]]
   [:div.panel-footer footer]]
  )

(defn analyze-text [article]
  (println article)
  ;; (POST "/api/check"
  ;;       {:params {:body (:body article)}
  ;;        :handler (fn [response]
  ;;                   (swap! app-state assoc-in [:my-articles (:id article) :body] (:highlighted response)))
  ;;        :error-handler error-handler
  ;;        :format :json
  ;;        :response-format :json
  ;;        :keywords? true})
  )


(defn display-article [article]
  (let [
        {:keys [id body title url]} article
        analyze-button [:button.btn.btn-xs.btn-success.pull-right {:on-click #(analyze-text article)} "analyze"]
        title [:span (str id ". " title) analyze-button]
        ]

    [:div.row
     [:hr]
     [:div.col-md-8
      [bs-panel title body url]
      [:pre (:highlighted article)]
      ]
     [:div.col-md-4  [bs-panel "oxford" "oxford description" "url"]]
     [:div.col-md-4  [bs-panel "oxford" "oxford description" "url"]]
     [:div.col-md-4  [bs-panel "oxford" "oxford description" "url"]]
     ]
    ))

(def app-state
  (atom {
         :my-articles articles/all
         :my-dictionary  [] }))

(defn analyze-text! [text]  (println text) )

(defn save-article [article]
  (println (:body (:article @article)))
  (println article)
  (let [id (count (:my-articles @app-state))]
    (println id)
    (when (not-empty (:body (:article @article)))
      (swap! app-state update-in [:my-articles] conj (assoc (:article @article) :id id))
      (reset! article {}))))

(def form-template
  [:div
   (row "body" [:textarea.form-control {:field :textarea :id :article.body}])
   (row "title" [:input.form-control {:field :text :id :article.title}])
   (row "url" [:input.form-control {:field :text :id :article.url}])])

(defn form []
  (let [doc (atom (:default-person @app-state))]
    (fn []
      [:div.form-horizontal
       [:div.page-header [:h1 "Article Form"]]
       [bind-fields form-template doc]
       [:button.btn.btn-primary {:on-click #(save-article doc)} "save article"]
       ])))

(defn page []
  [:div
   [:div.row [article-list]]
   [:div.row [form]]
   ])

;; Render the root component
(defn start []
  (reagent/render-component  [page] (.getElementById js/document "root")))


;; (fw/start {
;;   ;; configure a websocket url if you are using your own server
;;   ;; :websocket-url "ws://localhost:3449/figwheel-ws"

;;   ;; optional callback
;;   :on-jsload (fn [] (print "reloaded"))

;;   ;; The heads up display is enabled by default
;;   ;; to disable it:
;;   ;; :heads-up-display false

;;   ;; when the compiler emits warnings figwheel
;;   ;; blocks the loading of files.
;;   ;; To disable this behavior:
;;   ;; :load-warninged-code true

;;   ;; if figwheel is watching more than one build
;;   ;; it can be helpful to specify a build id for
;;   ;; the client to focus on
;;   ;; :build-id "example"
;;            }
)
