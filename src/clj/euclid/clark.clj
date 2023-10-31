(ns euclid.clark
  (:require [c3kit.apron.corec :as ccc]))

(def root "http://aleph0.clarku.edu/~djoyce/java/elements/")
(def selectors {:definition  {:path "def"}
                :postulate   {:path "post"}
                :proposition {:path "prop"}})

(defn update-path [spec update-fn]
  (update-in spec [:path] update-fn))

(defn add-root [spec]
  (assoc spec :path root))
(defn append-book [{:keys [book] :as spec}]
  (let [only-book? (not (some spec (keys selectors)))]
    (update-path spec
      #(str % "book" book
         (when only-book? (str "/book" book))))))

(defn append-selector [{:keys [book] :as spec} key]
  (let [val (get spec key)]
    (if (contains? spec key)
      (update-path spec #(str % "/" (get-in selectors [key :path]) book val))
      spec)))

(defn append-suffix [spec]
  (update-path spec #(str % ".html")))

(defn ->url
  ([] root)
  ([spec]
   (-> (add-root spec)
     append-book
     (append-selector :definition)
     (append-selector :proposition)
     append-suffix
     :path)))