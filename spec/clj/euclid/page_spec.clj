(ns euclid.page-spec
     (:require [clojure.edn :as edn]
               [clojure.pprint :refer [pprint]]
               [euclid.page :as sut]
               [speclj.core :refer :all]
               [euclid.util :as util]))

(def page (edn/read-string (slurp "bookI-stub.edn")))

(describe "elements"

  (it "names definition"
    (let [node {:tag     :dt
                :content [{:tag     :b
                           :attrs   nil
                           :content [{:tag     :a
                                      :attrs   {:href "defI1.html"}
                                      :content ["Definition 1"]}
                                     "."]}
                          "\n"]}]
      (should= 1 (sut/definition-num node))))

  (context "extract(s)"
    (it "definitions"
      ;(pprint (sut/extract-section page "proposition"))
      ;(pprint (sut/extract page :definitions))
      (pprint (sut/extract-proposition (util/->resource {:book "I" :proposition 1})))
      #_(should= {1 "A point is that which has no part."
                2 "A line is breadthless length."}
        (sut/extract-section page :definitions)))

    (it "justifications"
      (should= [{:book "I" :type :post :idx  3}
                {:book "I" :type :post :idx 1}]
        (sut/extract-justifications "I.Post.3\nI.Post.1")))))