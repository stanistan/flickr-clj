(ns flickr-clj.api
  (:use [flickr-clj [utils :only [mmerge elapsed-response]]
                    [methods :only [get-method-info]]
                    config]
        [flickr-clj.api [params :as params] [log :as log]]
        [cheshire.core :as json])
  (:require [clj-http.client :as client]))

(defn method-info
  [method]
  (or (get-method-info method)
      (throw (Exception. "Invalid flickr api method."))))

(def requests-made)

(def last-request (atom nil))

(defn parse-args
  [method data & [opts]]
  (-> (method-info method) (params/repare opts data) (mmerge opts)))

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
  [method data & [opts]]
  (-> (parse-args method data opts) (client/request) (:body) (json/parse-string true)))

(defn call
  [& args]
  (let [client-args (apply parse-args args)
        {:keys [elapsed response]} (elapsed-response (apply call* args))]
    (do
      (r/log-request client-args response elapsed)
      response)))

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
