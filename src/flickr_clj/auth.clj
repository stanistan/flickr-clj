(ns flickr-clj.auth
  (:use utils.common))

(defn generate-signature-string
  [secret coll]
  (let [r (fn [p [k v]] (str p (name k) v))]
    (->> (into (sorted-map) coll) (reduce r secret))))

(def generate-signature
  (comp md5 generate-signature-string))
