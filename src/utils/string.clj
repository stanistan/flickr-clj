(ns utils.string
  (:require [clojure.string :as string]))

(defn to-keyword
  "Cast to keyword."
  [n]
  (if (keyword? n) n (keyword n)))

(def to-str
  "Cast to string."
  (comp name to-keyword))

(def match-found?
  (comp not nil? re-seq))

(def butlastletter
  (comp (partial reduce str) butlast))

(defn spl
  "Split alias using to string cast."
  [s re]
  (string/split (to-str s) re))

(defn make-split
  "HOF returning a splitter."
  [re]
  (fn [s] (spl s re)))

(def dot-split
  (make-split #"\."))

(def dash-split
  (make-split #"-"))

(def dot-join
  (partial string/join "."))

(defn camel-case
  "Dash separated string to camel case."
  [s]
  (let [uc (->> s (dash-split) (map string/capitalize) (string/join) (seq) (vec))]
    (string/join (assoc uc 0 (string/lower-case (first uc))))))
