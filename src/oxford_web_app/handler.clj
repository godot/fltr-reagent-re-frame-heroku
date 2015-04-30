(ns oxford-web-app.handler
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.json :as middleware]
            [ring.adapter.jetty :as jetty]
            [oxford-web-app.views.layout :as layout]
            [oxford-web-app.views.contents :as contents]
            [oxford-web-app.utils :as utils]
            [cheshire.core :refer :all]
            [clj-http.client :as client]
            [environ.core :refer [env]]))

(defn analyze-text [text]
  {
   :text text
   :dict utils/word-list
   :analyzed (utils/analyze text)
   :highlighted (utils/highligh text)
   })

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (encode data)})

(defn google-images-search
  [word]
  (let
      [url (str "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" word)]
    (client/get url {:as :json :accept :json})))


(defn glosbe-translate
  [word]
  (let
      [url (str "https://glosbe.com/gapi/translate?phrase=" word "&pretty=true&from=en&dest=en&format=json")]
    (client/get url {:as :json :accept :json})))

(defroutes routes
  (GET "/" [] (layout/application "Oxford-Web-App" [:div "loading..."]) )
  (GET "/add" [] (layout/application "Add article" (contents/article-form)) )
  (GET "/check"  {{text :body} :params}  (layout/application "Check article" (contents/summary (analyze-text text))))

  ;;JSON
  (GET "/api/dictionary" [] (json-response (take 100 utils/word-list)))
  (GET "/api/dictionary/:word" [word] (json-response (:body (glosbe-translate word))))
  (GET "/api/images/:word" [word] (json-response (:body (google-images-search word))))
  (POST "/api/check" request (let [
                                   source-text (get-in request [:body :body])
                                   analyzed-response (select-keys (analyze-text source-text) [:text :highlighted :analyzed])]
                          (json-response analyzed-response)))

  (route/resources "/")
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(def my-site-defaults
  (wrap-defaults routes
                 (-> site-defaults
                     (assoc-in [:security :anti-forgery] false))))

(def app
  (let [handler my-site-defaults]
    (-> (if (env :dev?) (wrap-exceptions handler) handler)
        (middleware/wrap-json-body {:keywords? true})
        middleware/wrap-json-response
     )))


(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty app {:port port :join? false})))
