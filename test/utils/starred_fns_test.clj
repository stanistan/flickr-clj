(ns utils.starred-fns-test
  (:use clojure.test utils.starred-fns))

(deftest kw-symbol-test
  (is (= :abcde
    (kw-symbol :abcde*)
    (kw-symbol 'abcde*))))

(deftest starred?-test
  (is (= true
    (starred? 'abcde*)
    (starred? :abcde*)
    (starred? "abcde*"))))
