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
