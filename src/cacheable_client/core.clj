(ns cacheable-client.core
  (:use cacheable.common
        utils.common)
  (:require [org.httpkit.client :as client]
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
  [cache interval parse-response]
  Client
  (request [this arg]
    (let [re (make-request-with-cache cache interval arg)]
      (assoc re :response (parse-response (:response re))))))

(defn cache-type-init-fn
  [type]
  (-> (str "cacheable." (name type) "/init-cache") (symbol) (eval)))

(defn cacheable-client
  [cache-type interval & args]
  (let [args (or args [])
        parser (if (fn? (first args)) (first args) false)
        args (if parser (rest args) args)
        cache (apply (cache-type-init-fn cache-type) args)]
    (->CacheClient cache interval (or parser identity))))

