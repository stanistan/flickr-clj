(ns cacheable.disk
  (:use cacheable.common
        clojure.java.io
        [cacheable.disk.helpers :as h]))

(def dir-fns (h/fns "cache"))

(defn oldest-file
  [dir]
  (let [{:keys [get-dir-files]} dir-fns]
    (->> dir
      (get-dir-files)
      (sort-by #(.lastModified %))
      (first))))

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
       dir-fns]
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
        :oldest-key
        (fn [this] (->> (:dir this) (oldest-file) (data-from-file) (:key)))})))

(defn init-cache
  "Usage--
   No limit, no pre-populated data:
   (init-cache \"/path/to/cache\")

   Prepopulated data:
   (init-cache \"/path/to/cache\" data-hash-map)

   Prepopulated data and configuration (only limit is supported)
   (init-cache \"/path/to/cache\" {} :limit 10)"
  [path-to-cache & [initial-values & confs]]
  (start-cache ->Disk path-to-cache initial-values confs))
