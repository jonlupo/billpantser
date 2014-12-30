

(defn int-if-I-can [x]
  (let [i (try (Integer. x) (catch Exception e) (finally nil))]
    (if i i x)))

(find-doc "take")
(defn do-something [v]
  (let [a (split-with string? v)
        b (rest v)
        c (split-with integer? b)
        d (take-while integer? c)
        ]
[a d (last c)]))

(mapv int-if-I-can ["gp:001" "10" "bs:001" "foo:001" "bar:001" "10" "2014" "test:001"])
(split-with string? ["gp:001" 10 "bs:001" "foo:001" "bar:001" 10 2014 "test:001"])
(split-with string? ["gp:001"  "bs:001" "foo:001" "bar:001" 10 2014 "test:001"])
(do-something ["gp:001" 10 "bs:001" "foo:001" "bar:001" 10 2014 "test:001"])
(do-something ["gp:001" "bs:001" "foo:001" "bar:001" 10 2014 "test:001"])
(do-something ["x"])
(loop 
  [v ["gp:001" 10 "bs:001" "foo:001" "bar:001" 10 2014 "test:001"]] 
  (let [[x y z] (do-something v)]
    (println x y)
    (when (not (empty? z))
    (recur z))))

  (do-something ["gp:001" 10 "bs:001" "foo:001" "bar:001" 10 2014 "test:001"])
(do-something ["gp:001" 10 ])
(do-something ["gp:001" ])
 
