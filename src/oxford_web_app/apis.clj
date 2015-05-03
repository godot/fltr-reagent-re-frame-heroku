(ns oxford-web-app.apis
  (:require
   [clj-http.client :as http]))

(defn google-images-search
  [word]
  (let
      [url (str "https://ajax.googleapis.com/ajax/services/search/images?hl=en&as_rights=cc_publicdomain&rsz=3&v=1.0&q=" word)]
    (http/get url {:as :json :accept :json})))


(defn glosbe-translate
  [word]
  (let
      [url (str "https://glosbe.com/gapi/translate?phrase=" word "&pretty=true&from=en&dest=en&format=json")]
    (http/get url {:as :json :accept :json})))
