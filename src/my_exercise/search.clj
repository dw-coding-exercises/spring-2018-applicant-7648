(ns my-exercise.search
  (:require [hiccup.page :refer [html5]]
            [clojure.string :as str]
            [clojure.edn :as edn]
   :use     [clojure.java.shell :only [sh]]
            ))

;; definition for start of URL for to retrieve district elections 
(def election-api-url "https://api.turbovote.org/elections/upcoming?district-divisions=")

;; define an Address record type to hold data from address form
(defrecord Address [street street2 city state zip county])

;; TODO create record to map election results into

(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Nearby Upcoming Elections"]
   [:link {:rel "stylesheet" :href "default.css"}]])


;; generate URL to use to retrieve elections
;; TODO add county and legislative jurisdiction
(defn get-url [address]
      ;TODO add checking of values (make sure values exist before using; sending empty values seems to 'hang' on call to retrieve data)
      (let [state-ocd-id (str "ocd-division/country:us/state:" (:state address))
            city-ocd-id (str state-ocd-id "/place:" (:city address))
            county-ocd-id (str state-ocd-id "/county:" (:county address))]
         (str election-api-url state-ocd-id "," city-ocd-id "," county-ocd-id)))


;; this function will be responsible for creating the div section of each election returned 
(defn election-info [election]
  [:div {:class "election-info"}
   ;; display pertinent information (type, date, location, instructions
   ])

;; define page to display results
;; TODO add test for exit code (if not 0, display error page, otherwise display results)
(defn elections-nearby [address]
  (let [response (sh "curl" (get-url address))
        exit-code (:exit response)  ; so we can test whether response is good or not
        ]
  [:div {:class "get-elections"}
   [:h2 "Elections near you"]   
   ;; TODO format address entered on form 
   [:p (:street address)  ", " (:street-2 address) ", " (:city address) ", " (:state address) ", " (:zip address) ", " (:county address)]
   ;; need to parse response to generate display
   ;; need to figure out how to do this in clojure/functional programming
   ;; basically we want to process each election returned and call election-info to generate div for display
   [:p (str response) ;; for now just displaying EDN result to study it 
         ]])) 

;; function to retrieve county based on incoming address data
(defn get-county [street street2 city state zip]
  ;; TODO replace with a call to an API that returns county based on address
  (str "FOO")
  )

;; function that creates an Address object based on parameters from the request object
(defn get-address [params]
        ; modify city parameter to remove excess white space, replace remaining spaces with _, and make lower-case
        ; convert state parameter to lower case
  (let [street (get params :street)
        street-2 (get params :street-2)
        city  (str/replace (str/trim (str/lower-case (get params :city))) #"\s+" "_")
        state (str/lower-case (get params :state))
        zip   (get params :zip)]
  (->Address street
             street-2
             city 
             state
             zip
             (get-county street street-2 city state zip))))

;; function that submission of address form is routed to
(defn results [request]
  ;; retrieve address from incoming request
  (let [address (get-address (:params request))]
  ;; create html page for results
  (html5
   (header request)
   (elections-nearby address)  
   )))
