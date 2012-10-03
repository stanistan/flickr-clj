(ns cacheable-client.core
  (:use cacheable.common
        utils.common)
  (:require [clj-http.client :as client]
            cacheable.disk
            cacheable.atom))

(defn make-request*
  [args]
  (-> args (client/request) (:body)))

(defn make-request
  [args]
  (elapsed-response (make-request* args)))

(defn get-expires
  [interval]
  (if interval (+ (now) interval) false))

(defn make-request-with-cache
  [cache interval args]
  (let [k (md5 args)
        found (value cache k)
        metadata {:cache-hit? (if found true false)}]
    (merge metadata
      (or found
          (save cache k (make-request args) (get-expires interval))))))

(defprotocol Client
  (request [this arg]))

(defrecord CacheClient
  [cache interval]
  Client
  (request [this arg] (make-request-with-cache cache interval arg)))

(defn cache-type-init-fn
  [type]
  (-> (str "cacheable." (name type) "/init-cache") (symbol) (eval)))

(defn cacheable-client
  [cache-type interval & args]
  (let [cache (apply (cache-type-init-fn cache-type) (or args []))]
    (->CacheClient cache interval)))
