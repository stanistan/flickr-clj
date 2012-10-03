(ns flickr-clj.api
  (:use [flickr-clj [methods :only [get-method-info]] config]
        [flickr-clj.api.params :as params]
        [cheshire.core :as json]
        utils.common
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

(defn cache
  [cache-type interval & args]
  (let [args (concat [cache-type interval parse-json] (or args []))]
    (apply client/cacheable-client args)))

(defn cached-call
  [cache & args]
  (client/request cache (apply parse-args args)))

(defrecord Flickr [cached normal])

(defn init
  [api-settings]
  (->Flickr
    (fn-with-config api-settings cached-call)
    (fn-with-config api-settings call)))
