(ns flickr-clj.api-test
  (:use clojure.test)
  (:require [flickr-clj.api :as api]))

(deftest missing-method
  (testing "invalid-method"
    (is (thrown? Exception (api/method-info :a.b.c.d)))))
