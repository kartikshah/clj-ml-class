(ns clj_ml_class.classifier
  (:require [opennlp.nlp :as nlp]))

(defn tokenize-doc
  "Tokenizes an article (document) by word"
  [doc]
  (re-seq #"\w+" doc))

(defn length-filter
  "Filters features (word) by length"
  [n lst]
  (filter #(> (count %) n)
          lst))

(def tokenize-nlp (nlp/make-tokenizer "resources/en-token.bin"))

(defn tokenize-features
  "Tokenizes all docs (articles) by category e.g. :sports :arts"
  [docs category]
  (flatten (map tokenize-nlp (category docs))))

(defn train
  "Trains from supplied documents and category mapping."
  [docs category]
  (->> (tokenize-features docs category)
       (length-filter 2)
       (frequencies)))

(defn update-map-fn
  "Update Map Utility function to insert categories in parsed data. e.g {\"feature\" {:sports 1, :arts 2 }}"
  [category]
  (fn [m entrykey] (update-in m [entrykey] #(hash-map category %))))

(defn categorize
  "Categorizes train data"
  [train-data categories]
  (let [entrykeys (keys train-data)]
    (reduce (update-map-fn categories) train-data entrykeys)
    ))

(defn train-from-data
  "Trains from data"
  [docs]
  (apply merge-with
         conj
         (map #(categorize %1 %2)
              (map #(train docs %)
              (keys docs))
          (keys docs))))

(defn category-count
  "Calculates category count"
  [knowledge category]
  (reduce + 0 (filter #(not (nil? %)) (map category (vals knowledge)))))

(defn category-total
  "Category Total"
  [knowledge]
  (reduce + 0 (flatten (map vals (vals knowledge)))))

(def select-values (comp vals select-keys))

(defn select-values-nil
  [selected-values nil-val]
  (if (nil? selected-values)
       nil-val
       selected-values))

(defn feature-count
  "Calculates feature count"
  [knowledge feature category]
  (let [selected-values (select-values knowledge [feature])
        counts (apply category (select-values-nil selected-values [{category 0}]))]
   (+ 0 (if counts counts 0))))

(defn feature-total
  "Total feature count for all categories"
  [knowledge feature]
  (let [selected-values (select-values knowledge [feature])]
  (reduce + 0
          (apply vals
                 (select-values-nil selected-values [{:none 0}])))))

(defn feature-prob
  "Calculates given feature probablity"
  [knowledge feature category]
  (if (= (category-count knowledge category) 0)
      0
      (/ (feature-count knowledge feature category) (category-count knowledge category))))

(defn weighted-prob
  [knowledge feature category]
  (let [totals (feature-total knowledge feature)
        basic-prob (feature-prob knowledge feature category)]
  (println " " feature "-" category "-"  (/ (+ (* 1.0 0.5) (*  basic-prob totals))
     (+ 1.0 totals)))
  (/ (+ (* 1.0 0.5) (*  basic-prob totals))
     (+ 1.0 totals))))

(defn document-probability
  [knowledge item category]
  (let [item-features (length-filter 2 (distinct (tokenize-nlp item)))]
    (reduce * 1 (map #(weighted-prob knowledge %1 category) item-features))))

(defn probability
  [knowledge item category]
  (let [category-cnt (category-count knowledge category)
        category-ttl (category-total knowledge)
        category-prob (/ category-cnt category-ttl)]
    (* (document-probability knowledge item category) category-prob)))