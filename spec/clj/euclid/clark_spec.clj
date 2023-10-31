(ns euclid.clark-spec
  (:require [euclid.clark :as sut]
            [speclj.core :refer :all]))

(describe "clarku"
  (context "->url"
    (it "generates root url"
      (should= "http://aleph0.clarku.edu/~djoyce/java/elements/"
        (sut/->url)))

    (it "generates book url"
      (should= "http://aleph0.clarku.edu/~djoyce/java/elements/bookI/bookI.html"
        (sut/->url {:book "I"})))

    (it "generates definition url"
      (should= "http://aleph0.clarku.edu/~djoyce/java/elements/bookI/defI1.html"
        (sut/->url {:book "I" :definition 1})))

    (it "generates proposition url"
      (should= "http://aleph0.clarku.edu/~djoyce/java/elements/bookI/propI1.html"
        (sut/->url {:book "I" :proposition 1})))))