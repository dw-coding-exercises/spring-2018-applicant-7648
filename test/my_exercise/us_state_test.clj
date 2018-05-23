(ns my-exercise.us-state-test
  (:require [clojure.test :refer :all]
            [my-exercise.us-state :refer :all]))

(deftest postal-abbreviations-test
  (testing "there are 61 states, territories, military abbreviations, etc."
    (is (= 61
           (count postal-abbreviations))))
  (testing "some of the best states and districts are present"
    (is (every? (set postal-abbreviations)
                #{"CO" "NY" "CA" "KS" "DC" "IL" "WA"}))))


;; create test case for no election results
;; create test case for address with 1 result 
;; create test case for address with multiple results 
;; create test case for missing city
;; create test case for missing state 
