(ns clj_ml_class.NaiveBayesClassifier)

(defn process-labels [label acc]
  (assoc
    acc
    label
    (clj_ml_class.FileReader/process-file (str "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml_class/" label))))

(defn main []
  ;(doseq [label ["arts"]]
  (reduce process-labels "arts" {}))
;)
