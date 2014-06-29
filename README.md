# flickr-clj

[![Build Status](https://secure.travis-ci.org/stanistan/flickr-clj.png)](http://travis-ci.org/stanistan/flickr-clj)

A Clojure library designed to interface with the flickr API.

### Lein

```clj
[org.clojars.stanistan/flickr-clj "0.1.0"]
```

### Maven

```xml
<dependency>
  <groupId>org.clojars.stanistan</groupId>
  <artifactId>flickr-clj</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Usage

Api signature is automatically generated based on the arguments passed to the calling function.

#### Simplest use case.

```clj
(require '[flickr-clj [config :as conf] [api :as api]])

(conf/set-api-key "MY_API_KEY")
(conf/set-api-secret "MY_API_SECRET")
(def data {:text "nyc" :per_page 10})

; This call is not cached
(api/call :photos.search data)
;=> {:keys [elapsed response]}

; This call will not have elapsed information
(api/call* :photos.search data)
;=> ... equivalent of previous function except for the return is the response key

; Usinge caching, currently supported :disk and :atom
; ttl since the call is made, in milliseconds, if false, this does not expire
(def ms-interval 10000)

(def disk-cache (api/cache :disk ms-interval "/path/to/cache"))
(def atom-cache (api/cache :atom ms-interval))

(api/cached-call disk-cache :photos.search data)
(api/cached-call atom-cache :photos.search data)
;=> {:keys [elapsed response cache-hit?]}
```

#### Using multiple api keys and secrets

```clj
(use '[flickr-clj.api :as api])

(def my-conf {:key "MY_API_KEY" :secret "MY_API_SECRET"})
(def my-client (api/init-cache my-conf))
(def my-cache (api/cache :disk 10000 "/tmp/disk-cache"))

((:normal my-client) :photos.search data)
((:cached my-client) my-cache :photos.search data)
;=> These will return the same thing except for one will be cached and have :cache-hit?
```

## Cacheable

#### Disk

```clj
(use 'cacheable.common 'cacheable.disk)

(def path-to-cache "/path/to/cache")

;; Initializing the cache...

(def cache (init-cache path-to-cache))
; or
(def cache (init-cache path-to-cache {:populate "with this data"}))
; or
(def cache (init-cache path-tocache {} :limit 50))
```

#### Atom

```clj
(use 'cacheable.common 'cacheable.atom)

;; Initializing the cache...

(def cache (init-cache))
; or
(def cache (init-cache {:populate "with this data"}))
; or
(def cache (init-cache {} :limit 50))
```

#### Available fns

These are common to both caches.

```clj
(save cache :some-key :some-value)

(save cache :some-key :some-value time-expires-in-ms)

(delete cache :some-key)

(value cache :some-key)

(clear cache)

(get-all cache)

(get-all-with-meta cache)

;; Spawning/killing threads that remove expired data

(spawn-cache-cleaner cache)

(stop-cache-cleaners cache)
```

## Todo:

- ~~Limit the size of the cache~~
- ~~Cacheable docs and examples~~
- Fn docs
- ~~Figure out what is good for standalone usage~~

## License

Copyright © 2012 Stan Rozenraukh

Distributed under the Eclipse Public License, the same as Clojure.
