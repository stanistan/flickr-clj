(ns flickr-clj.api.params
  (:use flickr-clj.auth
        utils.common)
  (:require [flickr-clj.config :as c]))

(def endpoints
  {:http  "http://api.flickr.com/services"
   :https "https://secure.flickr.com/services"})

(defn get-url
  [http]
  (str (http endpoints) "/rest/"))

(defn prep-query-method
  [method api-key]
  (merge (c/get-api-query-args) {:method method :api_key api-key}))

(defn prep-api-call
  [{:keys [request-method name]} {:keys [endpoint]}]
  {:method request-method
   :url (get-url (or endpoint :http))
   :query-params (prep-query-method name (c/get-api-key))})

(defn get-signature
  [{:keys [query-params form-params]}]
  (generate-signature (c/get-api-secret) (merge query-params form-params)))

(defn inject-signature
  [params]
  (assoc-in params [:query-params :api_sig] (get-signature params)))

(defn prepare
  [info options data]
  (let [pars (prep-api-call info options)
        get? (= :get (:request-method info))
        mdata { (if get? :query-params :form-params) data }]
    (inject-signature (mmerge pars mdata))))
