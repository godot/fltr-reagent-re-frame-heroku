(ns oxford-web-app.handlers
  (:require
   [oxford-web-app.articles.core :as articles]
   [ajax.core :refer [GET POST]]
   [re-frame.core :refer [register-handler
                          path
                          dispatch]]))

(def initial-state
  {
   :my-articles articles/all
   :my-dictionary  [] })


(register-handler
 :initialize
 (fn
   [db _]
   (merge db initial-state)))

(register-handler
 :analyze-text
 (fn
   [db [_ id]]
   (let [txt (:text (get-in db [:my-articles id]))]
     (POST "/api/check"
        {:params {:body txt}
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
