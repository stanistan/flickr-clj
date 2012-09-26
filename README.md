# flickr-clj [![Build Status](https://secure.travis-ci.org/stanistan/flickr-clj.png)](http://travis-ci.org/stanistan/flickr-clj)

A Clojure library designed to interface with the flickr API.

## Usage

```clj
(use '[flickr-clj config [api :as api]])

(set-api-key "MY_API_KEY")

(api/call :photos.search {:text "nyc" :per_page 10})
```

## License

Copyright © 2012 Stan Rozenraukh

Distributed under the Eclipse Public License, the same as Clojure.
