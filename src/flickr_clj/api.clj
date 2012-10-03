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

(def call*
  [& args]
  (-> (apply parse-args args) (client/make-request*) (json/parse-string true)))

(def call
  [& args]
  (-> (apply parse-args args) (client/make-request) (json/parse-string true)))

(defn cacheable-client client/cacheable-client)



(defn call*
  "Makes an API call to flickr based on the method and options given.
   The data parameter will either be mapped to :query-params or :form-params depending
   on the type of request that is being made.
   opts is additional configuration that can be added to the request that is not data,
   this can override anything.

   An api_key is pretty much necessary for every request.

   Example:
   (config/set-api-key MY_API_KEY)
   (api/call :photos.search {:text nyc})"
  [& args]
  (-> (apply parse-args args) (client/make-request*))

(defn call
  [& args]
  (elapsed-response (apply call* args)))

(defn cached-caller
  [dir interval]
  (let [c (init-cache dir)]
    {:cache c
     :call (fn [& args]
            (let [k (md5 (apply parse-args args))
                  re (value c k)
                  exp (if interval (+ (now) interval) false)]
              (:response (or (value c k) (save c k (apply call args) exp)))))}))

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


