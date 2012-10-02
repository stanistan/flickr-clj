(ns flickr-clj.api-test
  (:use clojure.test)
  (:require [flickr-clj [api :as api]
                        [auth :as auth]
                        [config :as config]]))

(deftest missing-method
  (testing "invalid-method"
    (is (thrown? Exception (api/method-info :a.b.c.d)))))
