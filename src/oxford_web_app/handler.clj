(ns oxford-web-app.handler
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.adapter.jetty :as jetty]
            [oxford-web-app.views.layout :as layout]
            [oxford-web-app.views.contents :as contents]
            [oxford-web-app.utils :as utils]
            [environ.core :refer [env]]))

(defn convert [text]
  {:text text :stemm (utils/analyze text) })

(defroutes routes
  (GET "/" [] (layout/application "Home" (contents/index)) )
  (GET "/add" [] (layout/application "Add article" (contents/article-form)) )
  (GET "/check"  {{text :body} :params}  (layout/application "Check article" (contents/summary (convert text))))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))


(def app
  (let [handler (wrap-defaults routes site-defaults)]
    (if (env :dev?) (wrap-exceptions handler) handler)))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty app {:port port :join? false})))
