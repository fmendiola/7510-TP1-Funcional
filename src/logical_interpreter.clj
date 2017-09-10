(ns logical-interpreter)
(require '[clojure.string :as str])

;(def fact-pattern #"[a-zA-Z]*\(.*[\w\s,$]*\)\." ) ;revisar REGEX
(def fact-pattern #"[a-z]*\(.*\)." ) ;REGEX TEST NUMBER FACT

(defn recognise-match
  [regex row]
  (not (nil? (re-find regex row))) ;re-matches o re-find
  )

(defn is-fact-pattern
  [reg]
  (if (true? (recognise-match fact-pattern reg) ) ; redefinir la comparacion
    true
    false
    )
  )

(defn function-and ;funcion OR
  [x y]
  (and x y )
  )

(defn is-complete-db
  [db]
  (reduce function-and (map is-fact-pattern db ))
  ;(reduce (fn [x y] (and x y)) (map (fn [row] (not (nil? (re-find #"[a-z]*\(.*\)." row)))) ["  add(zero, one, one)." "add()." "  add(zero, two, two)." "  subtract(X, Y, Z) :- add(Y, Z, X)."]))
  )

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (def db-vector (rest (str/split database #"\n")))
  ;(map println db-vector)
  (if (is-complete-db db-vector)
  ;(if (is-complete-db '["add(zero, zero, zero)." "  add(zero, one, one)." "    add(zero, two, two)." "  add(one, zero, one)."  "    add(one, one, two)." "   add(one, two, zero)."  "  add(two, zero, two)."  "  add(two, one, zero)."  "  add(two, two, one).  " "   subtract(X, Y, Z) :- add(Y, Z, X).  " ])
  ;  (println "termino")
    true
    nil)
  )