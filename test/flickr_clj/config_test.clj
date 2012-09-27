(ns flickr-clj.config-test
  (:use flickr-clj.config clojure.test))

(deftest sybmols
  (testing "sname"
    (is (= 'some-symbol
      (sname "some-" "symbol")
      (sname "some-" 'symbol)
      (sname 'some-s 'ymbol))))
  (testing "snamer"
    (is (= ['get-something 'set-something 'reset-something]
      (snamer 'something)))))

(deftest config-macro
  (configurable-value test-value "a")
  (testing "existence of fns"
    (is (fn? get-test-value))
    (is (fn? set-test-value))
    (is (fn? reset-test-value)))
  (testing "getter"
    (is (= "a"
      (get-test-value))))
  (testing "setter"
    (is (= "b"
      (set-test-value "b")
      (get-test-value))))
  (testing "resetter"
    (is (= "a"
      (reset-test-value)
      (get-test-value)))))

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
