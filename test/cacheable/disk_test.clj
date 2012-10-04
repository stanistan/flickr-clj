(ns cacheable.disk-test
  (:use clojure.test cacheable.disk cacheable.common))

(def test-dir
  "/tmp/cacheable-disk-test")

(def test-data
  {:key1 :value1
   :key2 :value2
   :key3 :value3
   :key4 :value4
   :key5 :value5})

(def test-limit
  5)

(def cache (init-cache test-dir))

(deftest clear-test
  (do
    (clear cache)
    (is (empty? (get-all cache)))))

(deftest init-cache-test
  (testing "dir set?"
    (is (= (:dir cache) test-dir))))

(deftest save-test
  (let [[k v] [:key "somevalue"]]
    (do (save cache k v)
      (is (= (value cache k) v)))))

(deftest delete-test
  (do
    (delete cache :key)
    (is (nil? (value cache :key)))))

(deftest clear-test
  (do
    (clear cache)
    (is (empty? (get-all cache)))))

(deftest populate-test
  (do
    (clear cache)
    (populate cache test-data)
    (is (= (keys (get-all cache)) (keys test-data)))))

(deftest cache-limit-test
  (let [cache (assoc cache :limit test-limit)]
    (do
      (clear cache)
      (populate cache test-data)
      (save cache :key6 :value6)
      (testing "is cache size limit kept?"
        (is (= (num-records cache) test-limit)))
      (testing "did the first test-data get-removed?"
        (is (not (contains? (get-all cache) :key1))))
      (clear cache))))
