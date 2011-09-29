(ns clj_ml_class.core)
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
