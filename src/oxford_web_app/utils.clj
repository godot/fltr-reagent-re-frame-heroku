(ns oxford-web-app.utils)
(require 'clojure.string)
(require '(stemmers core soundex porter))
(require '[clojure.string :as str])

(defn log [message x] (println message) x)

(def s (log "loading dict from file" (slurp "oxford.dic.txt")))

(defstruct word-struct :url :word :type)

(def word-list (map #(apply struct word-struct (str/split % #";")) (str/split s #"\n")))

(def dictionary (apply hash-map (mapcat #(conj [] (:word %) %) word-list)))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn oxford-dict-word?  [word]
  (let [w (str/trim (str/lower-case word))] (or
           (contains? dictionary word)
           (contains? dictionary w)
           (contains? dictionary (first (stemmers.core/stems w))))))

(def mandatory-words ["o'clock" "are" "be" "ugly" "test" "tests" "died" "So" "cheap" "is" "was" "o'clock" "didn't" "doesn't" "isn't" "aren't" "wasn't" "weren't" "don't" "been" "I" "cheaper" "pretty" "better" "best" "badder" "on" "On"])


(defstruct word :orig :stemm :oxford?)

(defn analyze [text]
  (map #(struct word % (stemmers.core/stems (str %)) (oxford-dict-word? %)) (filter not-empty (clojure.string/split text #"[ ,.]"))))

(analyze "lorem ipsum ide die died")

;; ;; defaults to porter stemmer
(stemmers.core/stems "what died for stemming dying")

(or (not-empty (remove oxford-dict-word? mandatory-words)) "Works just fine!")
