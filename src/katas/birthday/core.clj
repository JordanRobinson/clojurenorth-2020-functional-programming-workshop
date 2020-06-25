(ns katas.birthday.core
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [postal.core :as postal])
  (:import
   (java.time LocalDate)
   (java.time.format DateTimeFormatter)
   (java.time.temporal ChronoUnit)))

(defn calculate-years [birthdate todays-date]
  (.between ChronoUnit/YEARS
    (LocalDate/parse birthdate
      (DateTimeFormatter/ofPattern "yyyy/MM/dd"))
    todays-date))

(defn is-birthday? [birthdate todays-date]
  (string/ends-with?
    birthdate
    (.format todays-date (DateTimeFormatter/ofPattern "MM/dd"))))

(defn build-message [row todays-date]
  (let [[name email birthdate] row]
    {:host "localhost"
     :user "azurediamond"
     :pass "hunter2"
     :port 2525}
    {:from "me@example.com"
     :to email
     :subject "Happy Birthday!"
     :body (str "Happy Birthday " name "! "
             "Wow, you're "
             (calculate-years birthdate todays-date)
             " already!")}))

(defn birthday-rows [todays-date rows]
  (filter
    (fn [row] (is-birthday? (row 2) todays-date))
    rows))

(defn get-rows [filename]
  (->> (io/resource filename)
    (io/reader)
    (csv/read-csv)
    rest))

(defn send-message [message]
  (postal/send-message message))

(defn greet! []
  (->> (get-rows "birthday/employees.csv")
    (birthday-rows (LocalDate/now))
    (map #(send-message (build-message % (LocalDate/now))))
    doall))
