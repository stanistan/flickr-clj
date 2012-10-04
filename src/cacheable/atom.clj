(ns cacheable.atom
  (:use cacheable.common))

(defn- oldest-key
  [coll]
  (first (first (sort-by (comp :saved second) coll))))

(defn- new-hash-atom [& args] (atom {}))

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
      :remove-oldest
      (fn [this] (delete this (oldest-key (get-all-with-meta this))))}))

(defn init-cache
  [& [initial-values]]
  (start-cache ->Atom (new-hash-atom) initial-values))
