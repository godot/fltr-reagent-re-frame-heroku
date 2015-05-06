,(ns oxford-web-app.handlers
  (:require
   [clojure.string :as string]
   [oxford-web-app.articles.core :as articles]
   [ajax.core :refer [GET POST]]
   [re-frame.core :refer [register-handler
                          path
                          dispatch]]))

(enable-console-print!)

(def initial-state
  { :system-messages []
   :my-articles []
   :spinners {:translation2 true}
   :selection-history '()
   :my-dictionary  [] })

(defn show-spinner [db type] (assoc-in db [:spinners type] true))
(register-handler
 :hide-spinner
 (fn [db [_ type]] (assoc-in db [:spinners type] false)))

(register-handler
 :initialize
 (fn
   [db _]
   (merge db initial-state)))

(register-handler
 :load-documents
 (fn [db _]
   (GET "/api/documents"
        {:handler #(dispatch [:documents-loaded %1])
         :format :json
         :response-format :json
         :keywords? true})
   db ))

(register-handler
 :load-document
 (fn [db [_ document-id]]
   (GET (str "/api/documents/" document-id)
        {:handler #(dispatch [:document-loaded %1])
         :format :json
         :response-format :json
         :keywords? true})
   db ))

(register-handler
 :document-loaded
 (fn [db [_ document]]
   (update-in db [:my-articles] conj document)))


(register-handler
 :documents-loaded
 (fn [db [_ docs]]
   (update-in db [:my-articles] concat docs)))


(register-handler
 :search-word
 (fn [db [_ word]]
   (GET (str "/api/dictionary/" (string/lower-case word))
        {:handler #(dispatch [:word-translated-successfully %1 word])
         :format :json
         :response-format :json
         :keywords? true})
   (let [db (show-spinner db :translation)]
     (assoc db :translation "..." ))))

(register-handler
 :search-google-images
 (fn [db [_ word]]
   (GET (str "/api/images/" word)
        {:handler #(dispatch [:images-found %1])
         :format :json
         :response-format :json
         :keywords? true})
   (let [db (show-spinner db :images)]
     (assoc db :images-found "..." )
     )))

(register-handler
 :images-found
 (fn
   [db [_ response]]
   (dispatch [:hide-spinner :images])
   (assoc db :images-found (:responseData response))))

(register-handler
 :word-translated-successfully
 (fn
   [db [_ response word]]
   (dispatch [:hide-spinner :translation])
   (assoc db :translation (:tuc response))))

(register-handler
 :word-selected
 (fn [db [_ words sth]]
   (let [word (string/join " " words)]
     (dispatch [:search-word word])
;;     (dispatch [:search-google-images word])
     (update-in db [:selection-history] conj word))))

(register-handler :clear-system-messages (fn [db] (assoc db :system-messages [])))
(register-handler :toggle-append-mode (fn [db] (update db :append-mode not)))

(register-handler
 :article-saved
 (fn
   [db [_ response]]
   (dispatch [:load-document (:_id response)])
   (update-in db [:system-messages] conj "Article saved successfully")))

(register-handler
 :save-article
 (fn
   [db [_ doc]]
   (let
       [ article (assoc (:article @doc)) ]
     (when (not-empty (:text article))
       (reset! doc {})
       (save-article article #(dispatch [:article-saved %1]))

       db))))


(defn save-article [article success-handler]
  (POST "/api/documents"
        {:params {:body article}
         :handler success-handler
         :error-handler failure
         :format :json
         :response-format :json
         :keywords? true}))

(defn failure [response] (println response))
