(ns katas.test.birthday
  (:require
   [clojure.test :refer :all]
   [katas.birthday.core :as birthday])
  (:import [java.time LocalDate]))

(deftest is-birthday?
  (testing "when the day and month should match, is-birthday? should return true"
    (is (not (birthday/is-birthday? "1990/01/02" (LocalDate/of 2000 01 01))))
    (is (birthday/is-birthday? "1990/01/01" (LocalDate/of 2000 01 01)))))

(deftest calculate-years
  (testing "should return years between two dates passed in"
    (is (= (birthday/calculate-years "1990/01/01" (LocalDate/of 2010 01 01)) 20))
    (is (= (birthday/calculate-years "2000/01/01" (LocalDate/of 2010 01 01)) 10))))

(deftest build-message
  (testing "should build out expected message with given parameters"
    (let [expected-message {:body "Happy Birthday name! Wow, you're 20 already!"
                            :from "me@example.com"
                            :subject "Happy Birthday!"
                            :to "email"}]
      (is (= (birthday/build-message ["name" "email" "1990/01/01"] (LocalDate/of 2010 01 01)) expected-message)))))

(deftest get-rows
  (testing "should get the expected rows given the example csv"
    (let [expected-rows [["Alice" "miss.alice@example.com" "1865/11/26"]
                         ["Bob" "builder.bob@example.com" "1998/11/28"]
                         ["Cathy" "catherine.the.great@example.com" "1729/04/29"]
                         ["Donald" "thedonald@example.com" "1946/06/14"]]]
      (is (= (vec (birthday/get-rows "birthday/employees.csv")) expected-rows)))))

(deftest birthday-rows
  (testing "should only return the rows from the data that have a date matching the date given"
    (let [sample-input [["Alice" "miss.alice@example.com" "1865/01/01"]
                        ["Bob" "builder.bob@example.com" "1998/11/28"]
                        ["Cathy" "catherine.the.great@example.com" "1729/04/29"]
                        ["Donald" "thedonald@example.com" "1946/06/14"]]
          expected-output '(["Alice" "miss.alice@example.com" "1865/01/01"])]
      (is (= (birthday/birthday-rows (LocalDate/of 2010 01 01) sample-input) expected-output)))))
