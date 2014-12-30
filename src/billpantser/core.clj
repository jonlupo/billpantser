(ns billpantser.core
  (:use billpantser.data
        billpantser.io)
  (:require [clj-time.core :as ct-c]
            [clj-time.local :as ct-l]
            [clj-time.format :as ct-f]
            [clojure.string :as cs]
            [clojure.tools.cli :refer [parse-opts]]
            [instaparse.core :as insta])
  (:gen-class :main true)) 


;; parse command line options
(def cli-options
  ;; An option with a required argument
  [
   ["-p" "--paid"
    ]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

;; Get current date
(defn get-now []
  (let [now (ct-l/local-now)]
    {:year (ct-c/year now)
     :day (ct-c/day now)
     :month (ct-c/month now)}))

;; update history atom with a new paid bill
(defn bill-paid! [aID, month, year]
 (let [ paid
            {:aID aID
             :paid-on (get-now)
             :paid-for-month month
             :paid-for-year year}]
    (swap! history conj paid)
    ))
;; check if a bill was already paid for that month
(defn ispaid [acc-id, month, year]
  (let [fby (fn [m]
              (if (and (= acc-id (:aID m)) 
                       (= month (:paid-for-month m))
                       (= year (:paid-for-year m))
                       ) m )) ]
    (first (filter fby @history))))

;; pull an account that matches the acc-id
(defn check-account [acc-id]
  (into {} (filter #(if (=  (:aID %) acc-id) %) @accounts)))

;; get all accounts that are due to be paid
(defn get-accounts-due []
   (let [acc-ids (map :aID @accounts)
        curr-month-year (select-keys (get-now) '(:month :year))
        month (:month curr-month-year)
        year (:year curr-month-year)
        due (filter #(not (ispaid % month year)) acc-ids)
        ]
      (into [] (map check-account due))))

;; generate a custom unique account id
(defn unique-aid-gen [acc-name]
  (let [
        base (cs/lower-case (apply str (map first (cs/split acc-name #" "))))
        aIDs (map :aID @accounts)
        split-aIDs (map #(cs/split % #":") aIDs)
        filtered-aIDs (filter #(= base (first %)) split-aIDs)
        num-strings (map last filtered-aIDs)
        i (fn [s] (Integer. s))
        nums (map i num-strings)
        winner (if (not (empty? nums)) (apply max nums) 0)
        newid  (str base ":" (format "%03d" (inc winner)))
        ]
    newid ))

;; and a new account to the accounts atom
(defn add-account! [accountname duedate accountnumber]
  (let [ new-account 
        {:aID (unique-aid-gen accountname) 
         :account accountname
         :due duedate
         :accountnumber accountnumber}
        ]
    (swap! accounts conj new-account)))

(def weed (insta/parser (clojure.java.io/resource "wps.bnf")))

(defn -main [_ & args] 
  (let  [parsed-args (weed (apply str (interpose " " args)))  
         defaults (select-keys (get-now) [:day :year])  
         results (map  (fn  [[_ & kvs]]  (into defaults kvs)) parsed-args) ]
  (println results)))

;test functions

(comment (add-account! "this" 20 "339393") 
         (weed (apply str (interpose " " ["gp:001" 23 2014 "gp:002" 15 "gp:003" "gp:004" 07 2014])))
         (def x (weed (apply str (interpose " " ["gp:001" 23 2014 "gp:002" 15 "gp:003" "gp:004" 07 2014]))))
         (println @accounts)
         (println @history)
         (load-history!) 
         (load-accounts!)
         (write-accounts! (add-account! "this" 20 "339393"))
         (write-accounts! (add-account! "Georgia Power" 15 "525225")) 
         (write-accounts! (add-account! "Humana" 20 "12349085"))
         (write-history! (bill-paid! "gp:001", 9, 2014))
         (get-accounts-due)
         (ispaid "h:001" 9 2014)
         (ispaid "gp:001" 21 2014)
         (clear-history!)
         (delete-all-accounts!)
         (println @accounts))

(comment (let [d  (select-keys (get-now) [:year :day])]  (map  (fn  [[_ & kvs]]  (into d kvs)) (weed (apply str (interpose " " ["gp:001" 23 2014 "gp:002" 15 "gp:003" "gp:004" 07 2014])))) )
         
         )


