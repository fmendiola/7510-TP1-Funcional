(ns logical-interpreter)
(require '[clojure.string :as str])

;(def fact-pattern #"[a-zA-Z]*\(.*[\w\s,$]*\)\." ) ;revisar REGEX
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
  [query]
  (recognise-find rule-pattern query)
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

(defn rule-in-db? ;TERMINARRR!!!
  [db query]
  false
  )

(defn query-in-db?
  [db query]
  (if (is-a-rule? query)
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