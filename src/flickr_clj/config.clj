(ns flickr-clj.config)

;; Key

(def api-key nil)
(defn get-api-key [] api-key)
(defn set-api-key [v] (def api-key v))

;; Secret

(def api-secret nil)
(defn get-api-secret [] api-secret)
(defn set-api-secret [v] (def api-secret v))
