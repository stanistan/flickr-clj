(ns utils.string-test
  (:use utils.string clojure.test))

(deftest casting
  (testing "to-keyword"
    (is (= :a
      (to-keyword :a)
      (to-keyword "a"))))
  (testing "to-str"
    (is (= "a"
      (to-str :a)
      (to-str "a")))))

(deftest splitting
  (testing "split"
    (let [re #"-"]
      (is (=
        (spl "split-on-dash" re)
        (spl :split-on-dash re)
        ["split" "on" "dash"]))))
  (testing "dot-split"
    (is (=
      (dot-split "split.on.dot")
      (dot-split :split.on.dot)
      ["split" "on" "dot"])))
  (testing "dash-split"
    (is (=
      (dash-split "split-on-dash")
      (dash-split :split-on-dash)
      ["split" "on" "dash"]))))

(deftest joining
  (testing dot-join
    (is (= "a.b.c.d" (dot-join ["a" "b" "c" "d"])))))

(deftest camel-casing
  (testing "camel-case"
    (is (=
      (camel-case "abc-def-ged")
      (camel-case :abc-def-ged)
      "abcDefGed"))))
