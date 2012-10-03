(ns flickr-clj.config
  (:use utils.conf))

(configurable-value
  api-key
  nil)

(configurable-value
  api-secret
  nil)

(configurable-value
  api-query-args
  {:format "json"
   :nojsoncallback "1"})

(defn current-settings
  []
  {:api-key (get-api-key)
   :api-secret (get-api-secret)
   :api-query-args (get-api-query-args)})

(defn prepend-api
  [[k v]]
  {(keyword (str "api-" (name k))) v})

(defn or-settings
  [conf]
  (->> conf (map prepend-api) (reduce merge) (merge (current-settings))))

(defn fn-with-config
  [conf f]
  (let [{:keys [api-key api-secret api-query-args]} (or-settings conf)]
   (fn [& args]
      (with-redefs
        [flickr-clj.config/get-api-key (fn [] api-key)
         flickr-clj.config/get-api-secret (fn [] api-secret)
         flickr-clj.config/get-api-query-args (fn [] api-query-args)]
        (apply f args)))))
