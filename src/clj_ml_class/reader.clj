(ns clj_ml_class.reader
  (:import (java.io BufferedReader FileReader)))

(defn read-file [filename line-func line-acc]
  (with-open [rdr (BufferedReader. (FileReader. filename))]
    (reduce line-func line-acc (line-seq rdr))))

(defn process-line [acc line]
  (conj acc line))

(defn label-file [tag file] (assoc {} tag (read-file file process-line [])))

(defn read-files [labels files]
  (apply
    merge
    (map label-file labels files )))
; ["arts" "sports"]
; ["/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml_class/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml_class/sports"])

(def docs (clj_ml_class.reader/read-files [:arts :sports] ["/Users/Kartik/dev/IdeaProjects/clj-ml-class/src/clj_ml_class/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml-class/src/clj_ml_class/sports"]))

