(ns logical-interpreter)
(require '[clojure.string :as str])

(def base-pattern #"[a-z]*\(.*\)" ) ;REGEX TEST NUMBER FACT AND RULE
(def fact-pattern #"[a-z]*\([a-z, ]*\)" )
(def rule-pattern #"[a-z]*\([A-Z, ]*\).*" )


(defn recognise-find
  [regex row]
  (not (nil? (re-find regex row)))
  )

(defn recognise-match
  [regex row]
  (not (nil? (re-matches regex row)))
  )

(defn is-fact-pattern
  [reg]
  (if (true? (recognise-find base-pattern reg) )
    true
    false
    )
  )

(defn function-and ;funcion AND
  [x y]
  (and x y )
  )

(defn function-or
  [x y]
  (or x y)
  )

(defn is-a-complete-db?
  [db]
  (reduce function-and(map is-fact-pattern db ))
  )

(defn is-a-empty-query?
  [query]
  (not (is-fact-pattern query))
  )

(defn is-a-rule?
  [db query]
  (if(not (nil? (re-find #"subtract" query)))
    true
    (if (not (nil? (re-find #"hij" query)))
      true
      false
      )
    )
  )

(defn reg-to-vec
  [reg]
  (vec (re-seq #"\w+" reg))
  )

(defn fact-in-db?
  [db query]
  (def query-vec (vec(re-seq #"\w+" query)))
  (if (reduce function-or(map (fn [reg] (= query-vec (reg-to-vec reg) )) db))
    true
    false
    )
  )

(defn process-rule-number-db
  [db query]
  (def rule-parameters (first (rest (re-find #"[a-z]*\(([a-z, ]*)\)" query))))
  (def X (first (re-seq #"\w+" rule-parameters)))
  (def Y (first (rest (re-seq #"\w+" rule-parameters))))
  (def Z (second (rest (re-seq #"\w+" rule-parameters))))
  (def add-parameters (str Y ", " Z ", " X))
  (def add (str/replace "add(Y, Z, X)" "Y, Z, X" add-parameters ))
  (if (fact-in-db? db add)
    true
    false
    )
  )

(defn two-facts-in-db?
  [db fact1 fact2]
  (if (and (fact-in-db? db fact1) (fact-in-db? db fact2))
    true
    false
    )
  )

(defn varon-and-padre?
  [db padre X]
  (def varon (str/replace "varon(X)" "X" X))
  (two-facts-in-db? db padre varon)
  )

(defn mujer-and-padre?
  [db padre X]
  (def mujer (str/replace "mujer(X)" "X" X))
  (two-facts-in-db? db padre mujer)
  )

(defn process-rule-parent-db
  [db query]
  (def rule-parameters (first (rest (re-find #"[a-z]*\(([a-z, ]*)\)" query))))
  (def X (first (re-seq #"\w+" rule-parameters)))
  (def Y (first (rest (re-seq #"\w+" rule-parameters))))
  (def padre-parameters (str Y ", " X))
  (def padre (str/replace "padre(Y, X)" "Y, X" padre-parameters))

  (if (= (str "hijo") (re-find #"hijo" query))
    (varon-and-padre? db padre X)
    (mujer-and-padre? db padre X)
    )
  )

(defn rule-in-db? ;TERMINARRR!!!
  [db query]
  (def rule-number-vars (count (re-seq #"\w+" (str (rest (re-find #"[a-z]*(\([a-z, ]*\))" query))))) )
  (if (= rule-number-vars 3)
    (process-rule-number-db db query)
    (process-rule-parent-db db query)
    )
  )

(defn query-in-db?
  [db query]
  (if (is-a-rule? db query)
    (rule-in-db? db query)
    (fact-in-db? db query)
    )
  )


(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (def db-vector (rest (str/split database #"\n"))) ;se elimina el primer elemento porque es un string vacio
  (if (is-a-complete-db? db-vector)
    (if (is-a-empty-query? query)
      nil
      (query-in-db? db-vector query)
      )
    nil
    )
  )