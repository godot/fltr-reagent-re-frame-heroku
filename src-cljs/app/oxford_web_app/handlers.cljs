(ns oxford-web-app.handlers
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
   :spinners {}
   :my-dictionary  [] })

(defn show-spinner [db type] (println (:spinners db)) (assoc-in db [:spinners type] true))
(register-handler
 :hide-spinner
 (fn [db [_ type]] (println (:spinners db)) (assoc-in db [:spinners type] false)))

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
   db
   ))

(register-handler
 :documents-loaded
 (fn [db [_ docs]]
   (update-in db [:my-articles] concat docs)))


(register-handler
 :search-word
 (fn
   [db [_ word]]
   (GET (str "/api/dictionary/" (string/lower-case word))
        {:handler #(dispatch [:word-translated %1 word])
         :format :json
         :response-format :json
         :keywords? true})
   (show-spinner db :translation)))

(register-handler
 :search-google-images
 (fn [db [_ word]]
   (GET (str "/api/images/" word)
        {:handler #(dispatch [:images-found %1])
         :format :json
         :response-format :json
         :keywords? true})
   (show-spinner db :images)))

(register-handler
 :images-found
 (fn
   [db [_ response]]
   (dispatch [:hide-spinner :images])
   (assoc db :images-found (:responseData response))))

(register-handler
 :word-translated
 (fn
   [db [_ response word]]
   (dispatch [:search-google-images word])
   (dispatch [:hide-spinner :translation])
   (assoc db :translation (:tuc response))))

(register-handler
 :word-selected
 (fn [db [_ word]] (dispatch [:search-word word]) (assoc db :selected-word word)))


(register-handler :clear-system-messages (fn [db] (assoc db :system-messages [])))

(register-handler
 :article-saved
 (fn
   [db [_ response]]
   (println response)
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


(defn save-article [article success]
  (POST "/api/documents"
        {:params {:body article}
         :handler success
         :error-handler failure
         :format :json
         :response-format :json
         :keywords? true}))

(defn failure [response] (println response))
