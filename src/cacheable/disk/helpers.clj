(ns cacheable.disk.helpers
  (:use clojure.java.io))

(defn filename
  [suffix n]
  (str (if (keyword? n) (name n) n) suffix))

(defn get-dir
  [dir]
  (java.io.File. dir))

(defn cachefile?
  [suffix-pattern f]
  (not (nil? (re-seq suffix-pattern (.getName f)))))

(defn get-dir-files
  [suffix-pattern dir]
  (filter (partial cachefile? suffix-pattern) (file-seq (get-dir dir))))

(defn get-cache-file
  [suffix suffix-pattern dir n]
  (-> #(= (.getName %) (filename suffix n))
    (filter (get-dir-files suffix-pattern dir))
    (first)))

(defn data-from-file
  [f]
  (if (nil? f) nil
    (-> f (.getAbsolutePath) (slurp) (load-string))))

(def data-from-filename
  (comp data-from-file get-cache-file))

(defn write-to-file
  [suffix dir f data]
  (with-open [wrtr (writer (str dir "/" (filename suffix f)))]
    (.write wrtr (str data))))

(defn gen-partial
  [[k m]]
  (let [f (-> (name k) (symbol) (eval))
        v (if (empty? m) f (apply partial f m))]
    {k v}))

(defn fns
  [suffix]
  (let [pattern (re-pattern (str "\\." suffix))
        suffix (str "." suffix)
        conf {:filename [suffix]
              :get-dir []
              :cachefile? [pattern]
              :get-dir-files [pattern]
              :get-cache-file [suffix pattern]
              :data-from-filename [suffix pattern]
              :data-from-file []
              :write-to-file [suffix]}]
    (reduce merge (map gen-partial conf))))
