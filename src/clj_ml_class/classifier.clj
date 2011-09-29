(ns clj_ml_class.classifier)

(defn tokenize-doc
  "Tokenizes an article (document) by word"
  [doc]
  (re-seq #"\w+" doc))

(defn length-filter
  "Filters features (word) by length"
  [n lst]
  (filter #(> (count %) n)
          lst))

(defn tokenize-features
  "Tokenizes all docs (articles) by category e.g. :sports :arts"
  [docs category]
  (flatten (map tokenize-doc (category docs))))

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

(def select-values (comp vals select-keys))

(defn feature-count
  "Calculates feature count"
  [knowledge feature category]
   (apply category (select-values knowledge [feature])))

(defn feature-prob
  "Calculates given feature probablity"
  [knowledge feature category]
  (if (= category-count 0)
      0
      (/ (feature-count knowledge feature category) (category-count knowledge category))))
