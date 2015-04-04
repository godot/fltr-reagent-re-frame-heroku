(ns oxford-web-app.utils)
(require 'clojure.string)
(require '(stemmers core soundex porter))
(require '[clojure.string :as str])

(def s (slurp "oxford.dic.txt"))

(defstruct word-struct :url :word :type)

(def words-list (map #(apply struct word-struct (str/split % #"\t")) (str/split s #"\n")))
(def word-dict (apply hash-map (mapcat #(conj [] (:word %) %) words-list)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

;;(defn oxford-dict-word? [word] (contains word-dict word))
(defn oxford-dict-word?  [w]
  (or
   (contains? word-dict w)
   (contains? word-dict (first (stemmers.core/stems w)))))

(oxford-dict-word? "test")
(oxford-dict-word? "tests")
(oxford-dict-word? "died")

(defstruct word :orig :stemm :oxford?)

(defn analyze [text]
  (map #(struct word % (stemmers.core/stems (str %)) (oxford-dict-word? %)) (filter not-empty (clojure.string/split text #"[ ,.]"))))

(analyze "lorem ipsum ide die died")

;; ;; defaults to porter stemmer
(stemmers.core/stems "what died for stemming dying")
