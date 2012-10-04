(ns utils.starred-fns
  (:use [utils string common]))

(def kw-symbol
  (comp keyword butlastletter name))

(defn fn-to-map
  [f]
  {(kw-symbol f) (eval f)})

(defn starred?
  [f]
  (match-found? #"\*$" (str f)))

(defn get-star-fns-in-ns
  [space]
  (filter starred? (keys (ns-publics space))))

(defn get-fns-map
  [space]
  (reduce-map fn-to-map (get-star-fns-in-ns space)))
