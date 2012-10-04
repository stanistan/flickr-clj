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
  (populate [this data])
  (get-all-with-meta [this])
  (get-all [this])
  (remove-expired [this])
  (initialize-cache [this])
  (spawn-cache-cleaner [this])
  (stop-cache-cleaners [this])
  (remove-oldest [this])
  (oldest-key [this]))

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

;; Commonly implemented

(defn value*
  [cache k]
  (check-value cache (value-with-meta cache k)))

(defn save*
  ([cache k v] (save cache k v false))
  ([cache k v e]
    (let [limit (get cache :limit false)]
      (do
        (when (and limit (>= (num-records cache) limit)) (remove-oldest cache))
        (store-value cache k v e)
        v))))

(defn populate*
  [cache data]
  (do (doseq [[k v] data] (save cache k v))
    cache))

(defn get-all*
  [cache]
  (let [c (get-all-with-meta cache)]
    (if (empty? c) {}
      (->> c
        (map #(hash-map (first %) (:value (second %))))
        (reduce merge)))))

(defn remove-expired*
  [cache]
  (doseq [[k v] (get-all-with-meta cache)]
    (when (expired? v) (delete cache k))))

(defn spawn-cache-cleaner*
  [cache]
  (swap!
    (:cleaners cache)
    conj
    (future
      (loop []
        (remove-expired cache)
        (Thread/sleep 1000)
        (recur)))))

(defn initialize-cache*
  [cache]
  (let [c (assoc cache :cleaners (atom []))]
    (spawn-cache-cleaner c)
    c))

(defn stop-cache-cleaners*
  [cache]
  (let [cls (:cleaners cache)]
    (do
      (doseq [c @cls] (future-cancel c))
      (reset! cls []))))

(defn remove-oldest*
  [cache]
  (delete cache (oldest-key cache)))

(def shared-behavior
  {:value value*
   :save save*
   :populate populate*
   :get-all get-all*
   :remove-expired remove-expired*
   :spawn-cache-cleaner spawn-cache-cleaner*
   :initialize-cache initialize-cache*
   :stop-cache-cleaners stop-cache-cleaners*
   :remove-oldest remove-oldest*})

(defmacro start-cache
  [record-type arg values]
  `(-> ~arg (~record-type) (initialize-cache) (populate ~values)))
