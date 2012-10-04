(ns cacheable.disk
  (:use cacheable.common
        clojure.java.io
        [cacheable.disk.helpers :as h]))

(defrecord Disk [dir])

(extend Disk
  Cacheable
  (merge
    shared-behavior
    (let
      [{:keys [data-from-file
               write-to-file
               get-dir-files
               get-cache-file
               data-from-filename]}
       (h/fns "cache")]
      { :value-with-meta
        (fn [this k] (data-from-filename (:dir this) k))
        :store-value
        (fn [this k v e] (write-to-file (:dir this) k (prep-for-storage k v e)))
        :clear
        (fn [this] (doseq [f (get-dir-files (:dir this))] (.delete f)))
        :num-records
        (fn [this] (count (get-dir-files (:dir this))))
        :delete
        (fn [this k] (let [f (get-cache-file (:dir this) k)]
                      (or (nil? f) (.delete f))))
        :get-all-with-meta
        (fn [this] (->> (:dir this)
                    (get-dir-files)
                    (map data-from-file)
                    (filter (comp not nil?))
                    (map #(hash-map (:key %) %))
                    (reduce merge)))
        :remove-oldest
        (fn [this] (->> (:dir this)
                    (get-dir-files)
                    (sort-by #(.lastModified %))
                    (first)
                    (.delete)))})))

(defn init-cache
  [path-to-cache & [initial-values]]
  (start-cache ->Disk path-to-cache initial-values))
