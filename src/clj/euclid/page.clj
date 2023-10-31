(ns euclid.page
  (:require [c3kit.apron.corec :as ccc]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as s]
            [euclid.util :as util]
            [net.cgrand.enlive-html :as html]))

(def sections ["Definition" "Postulate" "Common Notion" "Proposition"])

(defn definition-name [node]
  (-> (html/text node)
    s/trim
    (s/replace #"\." "")))

(defn definition-num [node]
  (-> (s/split (definition-name node) #"\s")
    last
    Integer/parseInt))

(defn titles-section? [node section]
  (when (= :dt (:tag node))
    (re-find (re-pattern section) (s/lower-case (definition-name node)))))

(defn marks-section? [tag section]
  (and (= :center (:tag tag))
    (re-find (re-pattern section) (s/lower-case (html/text tag)))))

(defn get-section [page section]
  (let [body    (first (html/select page [:body]))
        tags    (filter #(not (string? %)) (:content body))]
    (->> (drop-while #(not (marks-section? % section)) tags)
      (drop-while #(or (s/blank? (html/text %))
                     (not= :dl (:tag %)))))))

(defn extract-section [page section]
  (let [nodes (:content (first (get-section page section)))]
    (->> (loop [nodes nodes defs {}]
      (if (empty? nodes)
        defs
        (let [title (first nodes)
              desc  (second nodes)]
          (when (titles-section? title section)
            (recur (nthrest nodes 2)
              (assoc defs (definition-num title) (s/replace (html/text desc) #"\n+" " ")))))))
      (into (sorted-map)))))

(defn extract-justifications [just-str]
  (when (not= "Q.E.F." just-str)
    (let [justs (s/split just-str #"\n")]
      (map #(let [[book type idx] (s/split % #"\.")]
              (prn idx)
              {:book book
               :type (keyword (s/lower-case type))
               :idx  (Integer/parseInt idx)})
        justs))))

(defn extract-proposition [page]
  (let [nodes     (filter #(not (string? %)) (:content (first (html/select page [:div.theorem]))))
        name      (html/text (first nodes))
        statement (html/text (second nodes))
        prop      {:name name :statement statement :lines []}
        nodes     (rest (drop-while #(not= "ldiagram" (get-in % [:attrs :class])) nodes))]
    (loop [prop prop nodes nodes]
      (if (empty? nodes)
        prop
        (let [node (first nodes)]
          (if (and (= :div (:tag node)))
            (recur (update-in prop [:lines] #(conj % {:line (html/text (second nodes))
                                                      :just (extract-justifications (html/text node))}))
              (nthrest nodes 2))
            (recur (update-in prop [:lines] #(conj % {:line (html/text node)}))
              (rest nodes))))))))