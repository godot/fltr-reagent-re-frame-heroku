(ns oxford-web-app.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require
   [re-frame.core :refer [register-sub]]))

(register-sub
 :my-articles
 (fn
   [db]
   (reaction (vals (:my-articles @db)))))
