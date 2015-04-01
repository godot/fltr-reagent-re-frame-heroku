(defproject oxford-web-app "1.0.0-SNAPSHOT"
  :description "Oxford web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [prone "0.8.0"]
                 [hiccup "1.0.5"]
                 [stemmers "0.2.2"]
                 [environ "0.5.0"]]
  :min-lein-version "2.0.0"
  :plugins [
            [lein-ring "0.9.3"]
            [environ/environ.lein "0.2.1"]
            ]
  :ring {:handler oxford-web-app.handler/app}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "oxford-web-app-standalone.jar"
  :profiles {:production {:env {:production true}}})
