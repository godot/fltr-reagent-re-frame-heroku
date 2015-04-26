(ns oxford-web-app.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require
   [re-frame.core :refer [register-sub]]))

(register-sub
 :article
 (fn
   [db [_ id]]
   (let
       [articles-reaction (reaction (:my-articles @db))]
     (reaction (get @articles-reaction (js/parseInt id))))))

(register-sub
 :my-articles
 (fn
   [db]
   (reaction (vals (:my-articles @db)))))


(register-sub
 :system-messages
 (fn [db] (reaction (:system-messages @db))))
