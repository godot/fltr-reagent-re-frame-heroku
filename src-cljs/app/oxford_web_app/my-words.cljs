(ns oxford-web-app.my-words
  (:require [clojure.string :as string]
            [oxford-web-app.subs]
            [ajax.core :refer [GET POST]]
            [oxford-web-app.views :as bs]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]
            [re-frame.core :refer [register-handler path dispatch]]
            [oxford-web-app.subs]))

(register-handler
 :save-translation
 (fn
   [db [_ doc]]
   (println @doc)
   db))

(defn form-template []
  (fn []
    [:form
     [bs/form-row [:input.form-control {:type "text" :placeholder "text" :id :translation.subject}]]
     [bs/form-row [:textarea.form-control {:placeholder "tranlation" :rows 8 :id :translation.body}]]]
    ))

(defn translate-box []
  (let [doc (reagent/atom {:translation {:subject "dupa"}})]
    (fn []
      [bs/panel
       [:strong "my translations"]
       [:span
        [bind-fields form-template doc]
        [bs/form-row [:button.btn.btn-default {:on-click #(dispatch [:save-translation doc])} "save"]]]
       (str @doc)]
      )))


(defn play-sound
  [src]
  (let [msg  (js/SpeechSynthesisUtterance. src)]
    (.speak (.-speechSynthesis js/window) msg)
    )
  )

(play-sound "Testing")

(register-handler
 :stop-reading
 (fn [db]
   (.cancel (.-speechSynthesis js/window))
   db))

(register-handler
 :text-to-speech
 (fn
   [db [_ text]]
   (play-sound text)
   db)
 )
