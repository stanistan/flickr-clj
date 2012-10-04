(ns utils.conf-test
  (:use utils.conf clojure.test))

(deftest sybmols
  (testing "sname"
    (is (= 'some-symbol
      (sname "some-" "symbol")
      (sname "some-" 'symbol)
      (sname 'some-s 'ymbol))))
  (testing "snamer"
    (let [namer (snamer ["get-" "set-" "reset-"])]
      (is (= ['get-something 'set-something 'reset-something]
        (namer 'something))))))

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
