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

(deftest groups
  (testing "getting methods in group"
    (is (=
      []
      (get-methods-in-group "does.not.exist")
      (get-methods-in-group :does.not.exist)))
    (is (=
      (get-methods-in-group :favorites)
      (get-methods-in-group "favorites")
      [{:request-method :post :key :add :parent :favorites :name "flickr.favorites.add"}
       {:request-method :get :key :get-context :parent :favorites :name "flickr.favorites.getContext"}
       {:request-method :get :key :get-list :parent :favorites :name "flickr.favorites.getList"}
       {:request-method :get :key :get-public-list :parent :favorites :name "flickr.favorites.getPublicList"}
       {:request-method :post :key :remove :parent :favorites :name "flickr.favorites.remove"}])))
  (testing "bools"
    (is (= true
      (method-group? :favorites)
      (method-group? :photos)
      (method-group? "favorites")
      (method-group? "photos.comments")))
    (is (= false
      (method-group? :faaake)
      (method-group? "some.non.group"))))
  (testing "getting possible groups"
    (is (=
      (get-possible-groups :a.b.c.d)
      (get-possible-groups "a.b.c.d")
      [:a.b.c.d :a.b.c :a.b :a])))
  (testing "get actual group using possible groups"
    (is (= nil
      (get-group :a.b.c.d)
      (get-group :something.that.does.not.exist)))
    (is (= :favorites
      (get-group "favorites")
      (get-group :favorites)))
    (is (= :photos.comments
      (get-group "photos.comments")
      (get-group "photos.comments.a-method")
      (get-group :photos.comments)))))
