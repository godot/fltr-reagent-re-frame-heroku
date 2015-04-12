(defproject oxford-web-app "1.0.0-SNAPSHOT"
  :description "Oxford web app"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3058" :scope "provided"]
                 [figwheel "0.2.5"]
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
                 [cheshire "5.4.0"]
                 [environ "0.5.0"]]
  :min-lein-version "2.0.0"
  :plugins [
            [lein-ring "0.9.3"]
            [environ/environ.lein "0.2.1"]
            [lein-figwheel "0.2.5"]
            [lein-cljsbuild "1.0.5"]
            ]

  :cljsbuild {
              :builds [ { :id "example"
                         :source-paths ["src/"]
                         :compiler { :output-to "resources/public/js/compiled/example.js"
                                    :output-dir "resources/public/js/compiled/out"
                                    ;;:externs ["resources/public/js/externs/jquery-1.9.js"]
                                    :optimizations :none
                                    :source-map true } } ]
              }

  :figwheel {
             :http-server-root "public" ;; this will be in resources/
             :server-port 3449          ;; default

             ;; CSS reloading (optional)
             ;; :css-dirs has no default value
             ;; if :css-dirs is set figwheel will detect css file changes and
             ;; send them to the browser
             :css-dirs ["resources/public/css"]

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server
             :ring-handler oxford-web-app.handler/app

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

             }

  :ring {:handler oxford-web-app.handler/app}
  :hooks [environ.leiningen.hooks]
  :uberjar-name "oxford-web-app-standalone.jar"
  :profiles {
             :production {:env {:production true}}
             })
