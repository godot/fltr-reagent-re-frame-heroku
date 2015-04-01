(ns oxford-web-app.utils)
(require 'clojure.string)
(require '(stemmers core soundex porter))

(def oxford-dict ["width" "with" "were" "session" "documentation" "was" "less" "test"])

(def my-text "Initially most of the testing van Houtum did was scripted testing for the new regression test. They choose to script the regression test to be able to do the tests more often in the same way and ultimately, to automate them. Using situational testing the test team used other tactics for different purposes. Functionality known to be buggy and layout flaws were tested through bug hunts, because documentation was less important and finding as many existing flaws as possible in a short time span was the goal. User acceptance tests were done with session based testing. By starting with the scripted testforms and moving towards the more exploratory testing activities, van Houtum saw that the agility of the scrum team wasn’t optimized however.")

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

;;(defn oxford-dict-word? [word] (some #{word} oxford-dict))
(defn oxford-dict-word? [word] (in? oxford-dict word))

(oxford-dict-word? "test")
(oxford-dict-word? "tests")
(oxford-dict-word? "died")

(defstruct word :orig :stemm :oxford?)

(defn analyze [text] (map #(struct word % (stemmers.core/stems (str %)) (oxford-dict-word? %)) (filter not-empty (clojure.string/split text #"[ ,.]"))))

(analyze "lorem ipsum ide die died")

;; ;; defaults to porter stemmer
(stemmers.core/stems "what died for stemming dying")
