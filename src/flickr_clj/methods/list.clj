(ns flickr-clj.methods.list)

;;
;; All of the flickr api methods
;;
;; map key is the method group, value is a vector of methods
;; if the value is a vector, the first piece of the vector is the request method
;;

(def api-methods
  {:activity [:user-comments :user-photos]
   :auth [:check-token :get-frob :get-full-token :get-token]
   :auth.oauth [:get-token :get-access-token]
   :blogs [:get-list :get-services [:post :post-photo]]
   :collections [:get-info :get-tree]
   :commons [:get-institutions]
   :contacts [:get-list :get-list-recently-uploaded :get-public-list :get-tagging-suggestions]
   :favorites [[:post :add] :get-context :get-list :get-public-list [:post :remove]]
   :galleries [[:post :add-photo] [:post :create] [:post :edit-media] [:post :edit-photo]
               [:post :edit-photos] :get-info :get-list :get-list-for-photo :get-photos]
   :groups [:browse :get-info [:post :join] [:post :join-request] [:post :leave] :search]
   :groups.discuss.replies [[:post :add] [:post :delete] [:post :edit] :get-info :get-list]
   :groups.discuss.topics [[:post :add] :get-info :get-list]
   :groups.members [:get-list]
   :groups.pools [[:post :add] :get-context :get-groups :get-photos [:post :remove]]
   :interestingness [:get-list]
   :machinetags [:get-namespaces :get-pairs :get-predicates :get-recent-values :get-values]
   :panda [:get-list :get-photos]
   :people [:find-by-email :find-by-username :get-groups :get-info :get-limits :get-photos
            :get-photos-of :get-public-groups :get-public-photos :get-upload-status]
   :photos [[:post :add-tags] [:post :delete] :get-all-contexts :get-contacts-photos
            :get-contacts-public-photos :get-context :get-counts :get-exif :get-favorites
            :get-info :get-not-in-set :get-perms :get-recent :get-sizes :get-untagged
            :get-with-geo-data :get-without-geo-data :recently-updated [:post :remove-tag]
            :search [:post :set-content-type] [:post :set-dates] [:post :set-meta]
            [:post :set-perms] [:post :set-safety-level] [:post :set-tags]]
   :photos.comments [[:post :add-comment] [:post :delete-comment] [:post :delete-comment]
                     :get-list :get-recent-for-contacts]
   :photos.geo [[:post :batch-correct-location] [:post :correct-location] :get-location
                :get-perms :photos-for-location [:post :remove-location] [:post :set-context]
                [:post :set-location] [:post :set-perms]]
   :photos.licenses [:get-info [:post :set-licence]]
   :photos.notes [[:post :add] [:post :delete] [:post :edit]]
   :photos.suggestions [[:post :approve-suggestion] :get-list [:post :reject-suggestion]
                        [:post :remove-suggestion] [:post :suggest-location]]
   :photos.transform [[:post :rotate]]
   :photos.upload [:check-tickets]
   :photosets [[:post :add-photo] [:post :create] [:post :delete] [:post :edit-media]
               [:post :edit-photos] :get-context :get-info :get-list :get-photos
               [:post :order-sets] [:post :remove-photo] [:post :remove-photos]
               [:post :reorder-photos] [:post :set-primary-photo]]
   :photosets.comments [[:post :add-comment] [:post :delete-comment] [:post :edit-comment]
                        :get-list]
   :places [:find :find-by-lat-lon :get-children-with-public-photos :get-info
            :get-info-by-url :get-place-types :get-shape-history :get-top-places-list
            :places-for-bounding-box :places-for-contacts :places-for-tags
            :places-for-user :tags-for-place]
   :prefs [:get-content-type :get-geo-perms :get-hidden :get-privacy :get-safety-level]
   :push [:get-subscriptions :get-topics [:post :subscribe] [:post :unsubscribe]]
   :reflection [:get-method-info :get-methods]
   :stats [:get-collection-domains :get-collection-referrers :get-collection-stats
           :get-csv-files :get-photo-domains :get-photo-referrers :get-photoset-domains
           :get-photoset-referrers :get-photoset-stats :get-photo-stats
           :get-photostream-domains :get-photostream-referrers :get-photostream-stats
           :get-popular-photos :get-total-views]
   :tags [:get-cluster-photos :get-clusters :get-hot-list :get-list-photo :get-list-user
          :get-list-user-popular :get-list-user-raw :get-most-frequently-used :get-related]
   :test [:echo :login :null]
   :urls [:get-group :get-user-photos :get-user-profile :lookup-gallery :lookup-group
          :lookup-user]})
