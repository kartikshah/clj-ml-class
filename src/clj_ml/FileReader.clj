(ns clj_ml.FileReader
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
; ["/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml/sports"])

(defn train [docs category])

(defn train_from_data [data]
  (train nil nil))



(defn tokenize [row]
  (re-seq #"\w+" row))

(defn length-filter [n lst]
  (filter #(> (count %) n)
          lst))

(defn calc-freq [docs category]
  (frequencies (length-filter 2 (get-features docs category))))

(defn get-features [docs category]
  (flatten (map tokenize (category docs))))

(def rslt (clj_ml.FileReader/process-files [:arts :sports] ["/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml/arts" "/Users/Kartik/dev/IdeaProjects/clj-ml/src/clj_ml/sports"]))
;(println (calc-freq
;                  (length-filter 2
;                                 (get-features rslt :sports))))

;(defn restructure1 [m mp tag]
;  (let [entrykey (key mp)]
;    (update-in m [entrykey] #(hash-map tag %))
;    ))
;
;(defn restructure2 [m tag]
;  (let [entrykeys (keys m)]
;    (update-in m entrykeys #(hash-map tag %))
;    ))
;
;(defn restructure3 [m tag]
;  (let [entrykeys (keys m)]
;    (update m #(hash-map tag %) entrykeys )
;    ))

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

(println (apply merge-with conj (all-features rslt)))


;(defn update
;  [m f & ks]
;  (merge m (zipmap ks (map (comp f m) ks))))