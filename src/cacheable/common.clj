(ns cacheable.common)

;; The protocol

(defprotocol Cacheable
  (value-with-meta [this k])
  (store-value [this k v e])
  (clear [this])
  (delete [this k])
  (num-records [this])
  (save [this k v] [this k v e])
  (value [this k])
  (populate [this data]))

;; Helper functions

(defn now
  []
  (System/currentTimeMillis))

(defn validate-time
  [t]
  (if (number? t) t false))

(defn prep-for-storage
  [k v e]
  {:key k
   :value v
   :expires (validate-time e)
   :saved (now)})

(defn expired?
  [{:keys [saved expires]}]
  (if (false? expires) false
    (>= (now) expires)))

(defn check-value
  [cache value]
  (cond (nil? value) nil
        (expired? value) (do (delete cache value) nil)
        :else (:value value)))

;; Commonnly implimented interface functions

(defn value*
  [cache k]
  (check-value cache (value-with-meta cache k)))

(defn save*
  ([cache k v] (save cache k v false))
  ([cache k v e] (store-value cache k v e)))

(defn populate*
  [cache data]
  (do (doseq [[k v] data] (save cache k v))
    cache))

(def shared-behavior
  {:value value*
   :save save*
   :populate populate*})

