(ns euclid.util
  (:require [euclid.clark :as clark]
            [net.cgrand.enlive-html :as html]))

(defn ->resource [spec]
  (html/html-resource
    (java.net.URL.
      (clark/->url spec))))