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
           (re-find #"[;\"\d?!,.\(\)\[\]]" word)
           (contains? dictionary (first (stemmers.core/stems w))))))

(defn normalize [text] (str/replace text "’" "'"))
(defn mark-new-lines [text] (str/replace text "\n" "\n\r"))
(defn tokenize [text] (-> text mark-new-lines normalize nlp-tokenize))

(defstruct word :orig :oxford?)

(defn analyze [text]
  (map #(struct word % (oxford-dict-word? %))
       (filter not-empty (tokenize text))))

(defn html-mark-word [word oxford] (if oxford (str "<span class='word'><mark>" word "</mark></span>") (str "<span class='word'>" word "</span>")))

(defn mark-non-oxford-word [word] (if (or (re-find #"[;\"\d?!,.\(\)\[\]]" word) (oxford-dict-word? word)) (html-mark-word word false) (html-mark-word word true)))
