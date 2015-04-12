(ns oxford-web-app.core
  (:require [clojure.string :as string]
            [reagent.core :as r]))

(enable-console-print!)

(def my-dictionary
  [
   {:word "pencil"}
   {:word "pencil2"}
   {:word "pencil3"}
   {:word "pencil4"}
   ])

(def app-state
  (r/atom {
           :my-dictionary  my-dictionary}))

(defn word [w]
  [:li
   [:span (:word w)]])


(defn oxford-word-list []
  [:div
   [:h1 "Word list"]
   [:ul
    (for [w (:my-dictionary @app-state)] [word w])]
   ])


;; Render the root component
(defn start []
  (r/render-component
   [oxford-word-list]
   (.getElementById js/document "root")))
