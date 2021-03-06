(defproject oxford-web-app "1.0.0-SNAPSHOT"
  :description "Oxford web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058" :scope "provided"]
                 [cljsjs/react "0.13.1-0"]
                 [compojure "1.1.8"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-jetty-adapter "1.3.2"]
                 [ring/ring-defaults "0.1.3"]
                 [ring/ring-json "0.3.1"]
                 [prone "0.8.0"]
                 [hiccup "1.0.5"]
                 [stemmers "0.2.2"]
                 [clojure-opennlp "0.3.3"]
                 [reagent "0.5.0"]
                 [re-frame "0.3.2"]
                 [secretary "1.2.3"]
                 [reagent-forms "0.5.0"]
                 [reagent-utils "0.1.4"]
                 [cheshire "5.4.0"]
                 [cljs-ajax "0.3.11"]
                 [clj-http "1.1.1"]
                 [clojurewerkz/elastisch "2.1.0"]
                 [environ "0.5.0"]]
  :min-lein-version "2.0.0"
  :plugins [
            [lein-ring "0.9.3"]
            [lein-cljsbuild "1.0.5"]
            [environ/environ.lein "0.2.1"]
            ]
  :source-paths ["src-cljs" "src"]

  :cljsbuild
  { :builds { :client
             { :source-paths ["src-cljs/app"]
              :compiler {
                         :output-to "resources/public/js/app.js"
                         :output-dir "resources/public/js/out"
                         :asset-path   "js/out" }}}}
  :profiles
  { :dev
   {
    :dependencies [
                   [figwheel "0.2.5"]]
    :source-paths ["src-cljs/dev"]
    :plugins [
              [lein-figwheel "0.2.5"]]
    :cljsbuild
    { :builds
     { :client
      {
       :source-paths ["src-cljs/dev"]
       :compiler
       {
        :optimizations :none
        :main "oxford-web-app.boot"
        :source-map true}}}}
    :figwheel
    {
     :http-server-root "public" ;; this will be in resources/
     :server-port 3449          ;; default
     :css-dirs ["resources/public/css"]
     :ring-handler oxford-web-app.handler/app
     :open-file-command "emacs"}}
   :production
   {
    :env { :production true }
    :cljsbuild
    { :builds
     { :client
      {
       :source-paths ["src-cljs/prod"]
       :compiler
       {
        :main "oxford-web-app.boot"
        :optimizations :advanced
        :pretty-print false}}}
     }}}
  :aliases {"package"
            ["with-profile" "production" "do"
             "clean" ["cljsbuild" "once"]  ["uberjar"] ]}


  :ring {:handler oxford-web-app.handler/app}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "oxford-web-app-standalone.jar"

  )
