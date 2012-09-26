(ns flickr-clj.methods-test
  (:use flickr-clj.methods clojure.test))

(deftest method-info-generator
  (testing "method-info with get"
    (is (=
      (method-info :some-method :some.parent)
      (method-info [:get :some-method] :some.parent)
      {:request-method :get
       :key :some-method
       :parent :some.parent
       :name "flickr.some.parent.someMethod"})))
  (testing "method-info with post"
    (is (=
      (method-info [:post :some-method] :some.parent)
      {:request-method :post
       :key :some-method
       :parent :some.parent
       :name "flickr.some.parent.someMethod"})))
  (testing "method-info with other (put)"
    (is (=
      (method-info [:put :some-method] :some.parent)
      {:request-method :put
       :key :some-method
       :parent :some.parent
       :name "flickr.some.parent.someMethod"}))))

