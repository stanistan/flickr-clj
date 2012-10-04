(ns cacheable.disk.helpers-test
  (:use clojure.test cacheable.disk.helpers))

(deftest filename-test
  (is (= "a.end"
    (filename ".end" :a)
    (filename ".end" "a"))))

(deftest get-dir-test
  (is
    (instance? java.io.File (get-dir "/tmp"))))
