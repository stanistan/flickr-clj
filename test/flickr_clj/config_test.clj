(ns flickr-clj.config-test
  (:use flickr-clj.config clojure.test))

(deftest api-key-config
  (testing "get defaults"
    (is (= nil
      (get-api-key)
      (get-api-secret))))
  (testing "setting"
    (is (= "abcdef"
      (set-api-key "abcdef")
      (get-api-key)))
    (is (= "something"
      (set-api-secret "something")
      (get-api-secret))))
  (testing "resetting"
    (is (= nil
      (reset-api-key)
      (get-api-key)
      (reset-api-secret)
      (get-api-secret)))))
