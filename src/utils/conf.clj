(ns utils.conf)

(defn sname
  "Returns a prefixed symbol for the given symbol."
  [pref s]
  (symbol (str pref s)))

(defn snamer
  "Returns a function to create symbols with a prefix for each coll."
  [coll]
  (fn [s] (mapv #(sname % s) coll)))

(defmacro configurable-value
  "Given a symbol and an initial value, this sets up a getter, setter, and resetter fns."
  [n v]
  (let [[g s r] ((snamer ["get-" "set-" "reset-"]) n)]
   `(do
      (def ~n ~v)
      (defn ~g [] ~n)
      (defn ~s [s#] (do (def ~n s#) (~g)))
      (defn ~r [] (~s ~v))
      ~v)))
