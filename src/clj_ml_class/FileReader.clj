(ns clj_ml_class.FileReader
  (:import (java.io BufferedReader FileReader)))

(defn process-file [filename line-func line-acc]
  (with-open [rdr (BufferedReader. (FileReader. filename))]
    (reduce line-func line-acc (line-seq rdr))))

(defn process-line [acc line]
  (conj acc line))

(defn mfunc [tag file] (assoc {} tag (process-file file process-line [])))

(defn process-files [labels files]
  (apply
    merge
    (map mfunc labels files )))
; ["arts" "sports"]
; ["/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml_class/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml_class/sports"])

(defn train [docs category])

(defn train_from_data [data]
  (train nil nil))


(defn tokenize [row]
  (re-seq #"\w+" row))

(defn length-filter [n lst]
  (filter #(> (count %) n)
          lst))

(defn get-features [docs category]
  (flatten (map tokenize (category docs))))

(defn calc-freq [docs category]
  (frequencies (length-filter 2 (get-features docs category))))

(def docs (clj_ml_class.FileReader/process-files [:arts :sports] ["/Users/Kartik/dev/IdeaProjects/clj-ml-class/src/clj_ml_class/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml-class/src/clj_ml_class/sports"]))

(defn updatefn [tag]
  (fn [m entrykey] (update-in m [entrykey] #(hash-map tag %))))

(defn restructure [m tag]
  (let [entrykeys (keys m)]
    (reduce (updatefn tag) m entrykeys)
    ))

(defn all-features
  [docs]
  (map #(restructure %1 %2)
       (map #(calc-freq docs %)
            (keys docs))
       (keys docs)))

(defn get-knowledge [docs] (apply merge-with conj (all-features docs)))

(defn category-count
  [knowledge category]
  (reduce + 0 (filter #(not (nil? %)) (map category (vals knowledge)))))

(def select-values (comp vals select-keys))

(defn feature-count
  [knowledge feature category]
   (apply cateogry (select-values knowledge [feature])))

(defn feature-prob
  [knowledge feature category]
  (if (= category-count 0)
      0
      (/ (feature-count knowledge feature cateogry) (category-count knowledge category))))
