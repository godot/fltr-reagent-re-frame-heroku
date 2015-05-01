(ns oxford-web-app.storage
  (:require
   [clojurewerkz.elastisch.rest :as esr]
   [clojurewerkz.elastisch.rest.index :as esi]
   [clojurewerkz.elastisch.rest.document :as esd]
   [oxford-web-app.utils :as utils]
   [environ.core :refer [env]]
   ))

(def elasticsearch-url (or (env :elasticsearch-url) "http://127.0.0.1:9200"))
(def index-name "oxford-dictionary-development")
(def doc-type "article")

(defn article-doc
  [es-result]
  (let [article (get-in es-result [:_source :body])]
    (assoc article :id (:_id es-result) :analyzed (utils/analyze (:text article)))))

(defn create-index []
  (esi/create (esr/connect elasticsearch-url) index-name))

(defn save
  [doc]
  (let [conn (esr/connect elasticsearch-url)]
    (esd/create conn index-name doc-type doc)))

(defn update
  [doc]
  (let [conn (esr/connect elasticsearch-url)
        id (str (:id doc))]
    (esd/put conn index-name doc-type id doc)))

(defn doc-get
  [id]
  (let [conn (esr/connect elasticsearch-url)]
    (article-doc (esd/get conn index-name doc-type id))))

(defn doc-search
  [query]
  (esd/search (esr/connect elasticsearch-url) index-name doc-type query))


(defn all
  []
  (map article-doc (:hits (:hits (doc-search {})))))
