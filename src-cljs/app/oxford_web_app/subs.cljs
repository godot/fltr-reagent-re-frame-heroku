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
     (reaction (first (filter #(= (str (:id %)) id) @articles-reaction))))))

(register-sub
 :my-articles
 (fn
   [db]
   (reaction (:my-articles @db))))

(register-sub
 :system-messages
 (fn [db] (reaction (:system-messages @db))))

(register-sub :selection-history (fn [db] (reaction (:selection-history @db))))

(register-sub
 :selected-word
 (fn [db]
   (let [history (reaction (:selection-history @db))]
     (reaction (first @history)))))

(register-sub
 :translation
 (fn [db] (reaction (:translation @db))))

(register-sub
 :images-found
 (fn [db] (reaction (:images-found @db))))


(register-sub
 :spinner
 (fn [db [_ type]] (reaction (get-in @db [:spinners type]))))
