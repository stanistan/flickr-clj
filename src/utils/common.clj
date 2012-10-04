(ns utils.common
  (:import java.security.MessageDigest))

(def mmerge
  (partial merge-with merge))

(def reduce-map
  (comp (partial reduce merge) map))

(defn md5
  "MD5 Hash a string."
  [s]
  (let [md (MessageDigest/getInstance "MD5")
        digest (do (.reset md)
                   (.update md (.getBytes (str s)))
                   (.toString (java.math.BigInteger. 1 (.digest md)) 16))
        missing (- 32 (count digest))]
    (str (apply str (repeat missing "0")) digest)))

(defmacro elapsed-response
  [& body]
 `(let [start# (. java.lang.System (clojure.core/nanoTime))
        re# ~@body
        end# (. java.lang.System (clojure.core/nanoTime))
        el# (- end# start#)
        els# (/ (double el#) 100000.0)]
    {:response re#
     :elapsed {:start start#
               :end end#
               :dur el#
               :dur-str (str "Elapsed time: " els# " msecs")}}))
