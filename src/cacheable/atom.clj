(ns cacheable.atom
  (:use cacheable.common))

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
      (fn [this] (reset! (:store this) (atom {})))
      :delete
      (fn [this k] (swap! (:store this) dissoc k))
      :num-records
      (fn [this] (count @(:store this)))
      :get-all-with-meta
      (fn [this] @(:store this))}))

(defn- new-hash-atom [] (atom {}))

(defn init-cache
  [& [initial-values]]
  (start-cache ->Atom (new-hash-atom) initial-values))
