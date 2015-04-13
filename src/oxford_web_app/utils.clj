(ns oxford-web-app.utils)
(require 'clojure.string)
(require '(stemmers core soundex porter))
(require '[clojure.string :as str])
(require '[opennlp.nlp :as nlp])
(require 'opennlp.treebank)

;;npl
(def get-sentences (nlp/make-sentence-detector "models/en-sent.bin"));
(def nlp-tokenize (nlp/make-tokenizer "models/en-token.bin"))
(def nlp-detokenize (nlp/make-detokenizer "models/english-detokenizer.xml"))

;;analyze
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

(defn normalize [text] (str/replace text "’" "'"))
(defn tokenize [text] (-> text normalize nlp-tokenize))

(defstruct word :orig :stemm :oxford?)

(defn analyze [text]
  (map #(struct word % (stemmers.core/stems (str %)) (oxford-dict-word? %))
       (filter not-empty (tokenize text))))

(defn html-mark-word [word, oxford] (if oxford (str "<span class='word'><mark>" word "</mark></span>") (str "<span class='word'>" word "</span>")))

(defn mark-non-oxford-word [word] (if (or (re-find #"[;\"\d?!,.\(\)\[\]]" word) (oxford-dict-word? word)) (html-mark-word word false) (html-mark-word word true)))

(defn highligh [text]
  (nlp-detokenize (map mark-non-oxford-word (tokenize text))))

(analyze "lorem ipsum ide die died")

;; ;; defaults to porter stemmer
(stemmers.core/stems "what died for stemming dying")

(or (not-empty (remove oxford-dict-word? mandatory-words)) "Works just fine!")

(def sentence "Dot. This is test of some hyper oswald functionalities! Don't mess with me and don't mess with them. There aren't funny!")

(def sentence "it makes it hard for engineers to progress beyond the feature-level stage, because meatier projects just aren’t done in most organizations when it’s seen as tenable for non-coding architects and managers to pull down off-the-shelf solutions and expect the engineers to “make the thingy work with the other thingy.")
(highligh sentence)
