(ns flickr-clj.utils
  (:require [clojure.string :as string])
  (:import java.security.MessageDigest))

(def mmerge
  (partial merge-with merge))

(defn to-keyword
  "Cast to keyword."
  [n]
  (if (keyword? n) n (keyword n)))

(def to-str
  "Cast to string."
  (comp name to-keyword))

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

(defn md5
  "MD5 Hash a string."
  [s]
  (let [md (MessageDigest/getInstance "MD5")
        digest (do (.reset md)
                   (.update md (.getBytes (str s)))
                   (.toString (java.math.BigInteger. 1 (.digest md)) 16))
        missing (- 32 (count digest))]
    (str (apply str (repeat missing "0")) digest)))

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

(defmacro elapsed-response
  [& body]
 `(let [start# (. java.lang.System (clojure.core/nanoTime))
        re# ~@body
        end# (. java.lang.System (clojure.core/nanoTime))
        el# (/ (double (- end# start#)) 100000.0)]
    {:response re#
     :elapsed {:start start#
               :end end#
               :dur el#
               :dur-str (str "Elapsed time: " el# " msecs")}}))
