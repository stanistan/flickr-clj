(ns flickr-clj.auth-test
  (:use flickr-clj.auth clojure.test))

(deftest signatures
  (testing "generateing the signature string"
    (is (=
      "aabcsomevaluekeyvaluekey2value2"
      (generate-signature-string "a" {:key "value" :key2 "value2" :abc "somevalue"})))))
