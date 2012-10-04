(ns utils.common-test
  (:use clojure.test utils.common))

(deftest mmerge-test
  (let [one {:a {:a 1 :b 2 :c 3}}
        two {:a {:a 2}}
        three {:a {:a 2 :b 2 :c 3}}]
    (is (= (mmerge one two) three))))

(deftest md5-test
  ;; expected values using bash: echo -n :key | md5
  (let [expected {"a" "0cc175b9c0f1b6a831c399e269772661"
                  "b" "92eb5ffee6ae2fec3ad71c777531578f"
                  "c" "4a8a08f09d37b73795649038408b5f33"}]
    (doseq [[k v] expected]
      (is (= (md5 k) v)))))

(deftest elapsed-response-test
  (testing "if responses match"
    (let [fs ['(+ 1 1) '(+ 1 2) '(utils.common/md5 "cccccheck")]]
      (doseq [f fs]
        (is (= (:response (elapsed-response (eval f))) (eval f)))))))
