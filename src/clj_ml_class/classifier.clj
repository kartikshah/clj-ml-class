(defn tokenize-doc [doc]
  (re-seq #"\w+" doc))

(defn length-filter [n lst]
  (filter #(> (count %) n)
          lst))

(defn tokenize-features [docs category]
  (flatten (map tokenize-doc (category docs))))

(defn calc-freq [docs category]
  (frequencies (length-filter 2 (tokenize-features docs category))))

(defn train
  [docs category]
  (->> (tokenize-features docs category)
       (length-filter 2)
       (frequencies)))

(defn update-map-fn [category]
  (fn [m entrykey] (update-in m [entrykey] #(hash-map category %))))

(defn categorize [train-data categories]
  (let [entrykeys (keys train-data)]
    (reduce (update-map-fn categories) train-data entrykeys)
    ))

(defn train-from-data
  [docs]
  (apply merge-with
         conj
         (map #(categorize %1 %2)
              (map #(train docs %)
              (keys docs))
          (keys docs))))

(defn category-count
  [knowledge category]
  (reduce + 0 (filter #(not (nil? %)) (map category (vals knowledge)))))

(def select-values (comp vals select-keys))

(defn feature-count
  [knowledge feature category]
   (apply category (select-values knowledge [feature])))

(defn feature-prob
  [knowledge feature category]
  (if (= category-count 0)
      0
      (/ (feature-count knowledge feature category) (category-count knowledge category))))
