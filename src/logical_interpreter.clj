(ns logical-interpreter)
(require '[clojure.string :as str])

(def fact-pattern #"[a-zA-Z]*\(.*\)\." ) ;revisar REGEX

(defn recognise-match
  [regex row]
  (not (nil? (re-matches regex row))) ;re-matches o re-find
  )

(defn is-fact-pattern
  [reg]
  (if (true? (recognise-match fact-pattern reg) ) ; redefinir la comparacion
    true
    false
    )
  )

(defn function-or ;funcion OR
  [x y]
  (or x y )
  )

(defn is-complete-db
  [db]
  ;(println db)
  ;(println(re-matches fact-pattern"varon(juan,diego,pepe,dario)."))
  (if (empty? db)
    false
    (reduce function-or (map is-fact-pattern db ))

    )
  )

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [database query]
  (def db-vector (vec (str/split database #"\s+")))
  (if (is-complete-db db-vector)
    (println "LLLLLUEEGUEEEEE!!");proceso
    nil)
  )
