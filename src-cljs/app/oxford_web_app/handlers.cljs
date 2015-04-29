(ns oxford-web-app.handlers
  (:require
   [oxford-web-app.articles.core :as articles]
   [ajax.core :refer [GET POST]]
   [re-frame.core :refer [register-handler
                          path
                          dispatch]]))

(enable-console-print!)

(def initial-state
  { :system-messages []
   :my-articles articles/all
   :my-dictionary  [] })


(register-handler
 :initialize
 (fn
   [db _]
   (merge db initial-state)))

(register-handler
 :search-word
 (fn
   [db [_ word]]
   (GET (str "/api/dictionary/" word)
        {:handler #(dispatch [:word-translated %1])
         :format :json
         :response-format :json
         :keywords? true})
   (assoc db :loading? true)))

(register-handler
 :word-translated
 (fn
   [db [_ response]]
   (assoc db :translation (:tuc response) :loading? false)))

(register-handler
 :analyze-text
 (fn
   [db [_ id mode]]
   (let [txt (:text (get-in db [:my-articles id]))]
     (POST "/api/check"
        {:params {:body txt}
         :handler #(dispatch [:article-analyzed id %1 mode])
         :error-handler error-handler
         :format :json
         :response-format :json
         :keywords? true})
     (assoc db :loading? true)
     )))

(register-handler
 :article-analyzed
 (fn
   [db [_ id response mode]]
   (let [article (select-keys response [:analyzed])
         db (assoc db :loading? false)
         ]
     (reset! mode :highlighted)
     (update-in db [:my-articles id] merge article))))

(register-handler
 :word-selected
 (fn [db [_ word]] (println word) (dispatch [:search-word word]) (assoc db :selected-word word)))


(register-handler :clear-system-messages (fn [db] (assoc db :system-messages [])))
(register-handler :article-saved (fn [db] (update-in db [:system-messages] conj "Article saved successfully")))

(register-handler
 :save-article
 (fn
   [db [_ doc]]
   (let
       [next-id (count (:my-articles db))
        article (assoc (:article @doc) :id next-id)
        ]
     (when (not-empty (:text article))
       (reset! doc {})
       (dispatch [:article-saved])
       (assoc-in db [:my-articles next-id] article)))))
