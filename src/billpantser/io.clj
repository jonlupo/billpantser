(ns billpantser.io
  (:use billpantser.data))

;; load accounts into atom from .dat file
(defn load-accounts! [] 
 (reset! accounts (read-string (slurp "accounts.dat"))))

;; write data to accounts.dat 
(defn write-accounts! [data] (spit "accounts.dat" data ))

;; load history into atom from .dat file
(defn load-history! []
  (reset! history (read-string (slurp "history.dat"))))

;; write data to history.dat 
(defn write-history! [data] (spit "history.dat" @history ))

;; overwrite all data in accounts.dat with an empty vector
(defn delete-all-accounts! [] (spit "accounts.dat" []))

;; overwrite all data in history.dat with an empty vector
(defn clear-history! [] (spit "history.dat" []))

