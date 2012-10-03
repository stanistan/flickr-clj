(ns flickr-clj.api
  (:use [flickr-clj [methods :only [get-method-info]] config]
        [flickr-clj.api.params :as params]
        [cheshire.core :as json]
        utils.common
        cacheable.disk
        cacheable.common)
  (:require [cacheable-client.core :as client]
            [cheshire.core :as json]))

(defn method-info
  [method]
  (or (get-method-info method)
      (throw (Exception. "Invalid flickr api method."))))

(defn parse-args
  [method data & [opts]]
  (-> (method-info method) (params/prepare opts data) (mmerge opts)))

(defn parse-json
  [s]
  (json/parse-string s true))

(def call*
  (comp parse-json client/make-request* parse-args))

(defn call
  [& args]
  (elapsed-response (apply call* args)))

(defn cacheable-client
  [cache-type interval & args]
  (let [args (concat [cache-type interval parse-json] (or args []))]
    (println args)
    (apply client/cacheable-client args)))

(defmacro defcaller
  "This should be used for when using multiple api accounts.

  (api/defcaller my-caller {:key SOME_KEY :secret SOME_SECRET})
  (api/defcaller another {:key SOME_OTHER_KEY})

  And use each one the same way as api/call

  (my-caller :photos.search {:text nyc})"
  [caller-name conf]
  `(defn ~caller-name [& gs#]
    (with-redefs [flickr-clj.config/get-api-key (fn [] ~(:key conf))
                  flickr-clj.config/get-api-secret (fn [] ~(:secret conf))
                  flickr-clj.config/get-api-query-args (fn [] ~(:query-args conf))]
      (apply call gs#))))


