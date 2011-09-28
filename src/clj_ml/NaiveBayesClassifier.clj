(ns clj_ml.NaiveBayesClassifier)

(defn process-labels [label acc]
  (assoc
    acc
    label
    (clj_ml.FileReader/process-file (str "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml/" label))))

(defn main []
  ;(doseq [label ["arts"]]
  (reduce process-labels "arts" {}))
;)
