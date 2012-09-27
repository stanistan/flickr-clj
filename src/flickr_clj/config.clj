(ns flickr-clj.config)

(defn sname
  "Returns a prefixed symbol for the given symbol."
  [pref s]
  (symbol (str pref s)))

(defn snamer
  "Returns a vector of a getter and setter symbol."
  [s]
  (mapv #(sname % s) ["get-" "set-" "reset-"]))

(defmacro configurable-value
  "Given a symbol and an initial value, this sets up a getter, setter, and resetter fns."
  [n v]
  (let [[g s r] (snamer n)]
   `(do
      (def ^{:private true} ~n ~v)
      (defn ~g [] ~n)
      (defn ~s [s#] (do (def ~n s#) (~g)))
      (defn ~r [] (~s ~v))
      ~v)))

;; initialize

(configurable-value api-key nil)

(configurable-value api-secret nil)
