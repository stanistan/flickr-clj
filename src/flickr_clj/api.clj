(ns flickr-clj.api
  (:use [flickr-clj.methods :only [get-method-info]]
        [flickr-clj.config]
        [cheshire.core :as json])
  (:require [clj-http.client :as client]))

(def endpoints
  {:http  "http://api.flickr.com/services"
   :https "https://secure.flickr.com/services"})

(defn method-info
  [method]
  (or
    (get-method-info method)
    (throw (Exception. "api method not found"))))

(defn- get-url
  [http]
  (str (http endpoints) "/rest/"))

(defn- query-params
  "Default query params for a flickr api call. We want json back."
  [method]
  {:method method
   :format "json"
   :nojsoncallback "1"
   :api_key (get-api-key)
   :api_secret (get-api-secret)})

(defn- api-params
  "Default client options based on method info."
  [method-info opts]
  {:method (:request-method method-info)
   :url (get-url (or (:endpoint opts) :http))
   :query-params (query-params (:name method-info))})

(defn call
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
  (let [opts (or opts {})
        info (method-info method)
        params (api-params info opts)
        add { (if (= :get (:request-method info)) :query-params :form-params) data }]
    (-> (merge-with merge params add opts)
      (client/request)
      (:body)
      (json/parse-string true))))

(defmacro defcaller
  "This should be used for when using multiple api accounts.

  (api/defcaller my-caller {:key SOME_KEY :secret SOME_SECRET})
  (api/defcaller another {:key SOME_OTHER_KEY})

  And use each one the same way as api/call

  (my-caller :photos.search {:text nyc})"
  [caller-name conf]
  `(defn ~caller-name [& gs#]
    (with-redefs [flickr-clj.config/get-api-key (fn [] ~(:key conf))
                  flickr-clj.config/get-api-secret (fn [] ~(:secret conf))]
      (apply call gs#))))
