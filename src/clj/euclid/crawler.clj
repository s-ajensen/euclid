(ns euclid.crawler
  (:require [clojure.pprint :refer [pprint]]
            [euclid.util :as util]
            [euclid.page :as elems]))

(defn -main [& args]
  (let [definitions (-> (util/->resource {:book "IV"})
                      (elems/extract-section "proposition"))]
    (pprint (into (sorted-map) definitions))))