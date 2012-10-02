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
      [{:keys [data-from-file write-to-file get-dir-files get-cache-file]} (h/fns "cache")]
      { :value-with-meta (fn [this k] (data-from-file (:dir this) k))
        :store-value (fn [this k v e] (write-to-file (:dir this) k (prep-for-storage k v e)))
        :clear (fn [this] (doseq [f (get-dir-files (:dir this))] (.delete f)))
        :num-records (fn [this] (count (get-dir-files (:dir this))))
        :delete (fn [this k] (let [f (get-cache-file (:dir this) k)]
                              (or (nil? f) (.delete f))))})))

(defn init-cache
  [path-to-cache & [initial-values]]
  (populate (->Disk path-to-cache) initial-values))
