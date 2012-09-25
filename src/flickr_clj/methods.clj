(ns flickr-clj.methods
  (:use [flickr-clj.methods.list]
        [flickr-clj.utils]))

(defn- method-info
  "Gets a map of info about a given method based."
  [piece parent]
  (let [[m n] (if (keyword? piece) [:get piece] piece)]
    {:request-method m
     :key n
     :parent parent
     :name (dot-join ["flickr" (to-str parent) (camel-case n)])}))

(defn get-methods-in-group
  "Returns a vector of methods and info for a given group.
   Example: (get-methods-in-group :favorites)"
  [n]
  (let [n (to-keyword n)]
    (map #(method-info % n) (get api-methods n []))))

(def method-group?
  (comp not empty? get-methods-in-group))

(defn- get-possible-groups
  "Gets all possible groups for the given key.
   Useful because of photos/photos.comments."
  [n]
  (let [groups (atom [])]
    (loop [gs (dot-split n)]
      (if (empty? gs)
        @groups
        (do
          (swap! groups conj (keyword (dot-join gs)))
          (recur (pop gs)))))))

(defn get-group
  "Gets the actual group name from a method identifier.
   (get-group :photos.comments.add-comment) -> :photos.comments"
  [n]
  (first (filter method-group? (get-possible-groups n))))

(defn- get-method-name
  "Method name from method identifier."
  [n]
  (keyword (last (dot-split n))))

(defn get-method-info
  "Gets method info based on method identifier or nil.

   (get-method-info :photos.comments.add-comment)
   => {:request-method :post
       :key :add-comment
       :parent :photos.comments
       :name \"flickr.photos.comments.addComment\"}"
  [n]
  (let [group-info (get-methods-in-group (get-group n))
        method (get-method-name n)]
    (first (filter #(= method (:key %)) group-info))))
