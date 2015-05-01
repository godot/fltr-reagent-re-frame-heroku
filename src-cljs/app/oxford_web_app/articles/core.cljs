(ns oxford-web-app.articles.core
  (:require [clojure.string :as string]))


(def all [{:id 0 :highlighted "" :url "http://qz.com/386126/the-body-language-secrets-of-successful-people/" :title "Exaggerated gestures" :text "Can imply that you’re stretching the trut palms of your hands—to communicate that you have nothing to hide."}
          { :id 1 :highlighted "" :url "some" :title  "Watching the clock" :text "While talking to someone is a clear sign of disrespect, impatience, and inflated ego. It sends the mess leave
 them." }
          {:id 2 :highlighted "" :url "http://qz.com/386126/the-body-language-secrets-of-successful-people/" :title "Our bodies have a language of their" :text "Our bodies part of who you are, to the point where you might not even think about it."}
          {:id 3 :title "why clojurescript" :text "While not a complete answer, I can point out some attractive qualities of ClojureScript

Clojure as a data format
ClojureScript allows for the client-server exchange data format to be Clojure.  This offers huge advantages over JSON, including simple things like sets or more advanced things like forms.  Using the recent addition of reader literals, the data format becomes open for extension, allowing the engineer to tailor the data format to solve any problem.

The reader and homoiconicity on the client
Imagine you had a Clojure reader on the client... everything you can do on the REPL, you can now do in the browser.  You can do that programmatically from the server, or interactively.  Interactive web development, live code updates, dream it up and you can do it- but ONLY because the reader is present on the client now.  Additionally ClojureScript offers a powerful homoiconic language for client-side development, all benefits apply.

Clojure+Node.js
The JVM is great, but we're all aware of the warts.  ClojureScript allows the language to not be restrained by the JVM's limitations.  With Node.js, startup time for simple Clojure scripts isn't a problem.  Both V8 and IonMonkey/*Monkey JS engines feature advanced tracing JITs that aren't in the JVM - some of the benchmarks for these JITs outperform Python, Ruby, and even PyPy's VM.  Allowing Clojure to step into this world is an instant win for \"getting the job done.\"  Use Clojure proper when it makes sense, when you hit a wart, use Clojure(Script)+Node.js

Client-side applications
Writing client-side dedicated apps is growing in popularity and is a great choice for many applications.  Take a look at GMail, Google Docs, Cloud9 IDE - these are very powerful applications, all built on JavaScript.  ClojureScript allows you to use the tools in Clojure world to push forward into this emerging field.  Even some of Clojure's most powerful contrib libraries, like core.match (and soon core.logic) are available as client-side libraries in ClojureScript.

Mobile Development with HTML5
With ClojureScript, Clojure becomes a first-class language for mobile development.  A growing trend is to build mobile apps with HTML5/CSS3/JS - enhancing the apps portability and maintainability.  Now imagine ClojureScript/HTML5/CSS3 with the benefits of protocols, the reader, macros, etc and all the libraries available to JavaScript including Google Closure and jQuery.  Suddenly a whole new set of possibilities becomes available to the developer.

Clojure in Clojure
Without going into too much detail, ClojureScript provides a map for what Clojure in Clojure might look like.  Searching around can provide you with more information why this would be desirable."}])
