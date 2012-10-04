(ns cacheable.atom
  (:use cacheable.common))

(defn new-hash-atom [& args] (atom {}))

(defrecord Atom [store])

(extend Atom
  Cacheable
  (merge
    shared-behavior
    { :value-with-meta
      (fn [this k] (get @(:store this) k))
      :store-value
      (fn [this k v e] (swap! (:store this) assoc k (prep-for-storage k v e)))
      :clear
      (fn [this] (doseq [k (keys (get-all this))] (delete this k)))
      :delete
      (fn [this k] (swap! (:store this) dissoc k))
      :num-records
      (fn [this] (count @(:store this)))
      :get-all-with-meta
      (fn [this] @(:store this))
      :oldest-key
      (fn [this] (->> (get-all-with-meta this)
                  (sort-by (comp :saved second))
                  (first) (first)))}))

(defn init-cache
   "Usage--
   No limit, no pre-populated data:
   (init-cache)

   Prepopulated data:
   (init-cache data-hash-map)

   Prepopulated data and configuration (only limit is supported)
   (init-cache {} :limit 10)"
  [& [initial-values & extra]]
  (start-cache ->Atom (new-hash-atom) initial-values extra))
