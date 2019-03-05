(ns my-cloruje-study.db
  (:require [datomic.api :as d]))

;; ----- Database connection -----

(def uri "datomic:free://localhost:4334/account-service-db")
(d/create-database uri)

(def conn (d/connect uri))

;; ------ Database configuration ------
;; parse schema edn file
(def schema-tx (read-string (slurp "resources/schema/schema.edn")))

;; display first statement
(first schema-tx)

;; submit schema transaction
@(d/transact conn schema-tx)


;; parse seed data edn file
(def data-tx (read-string (slurp "samples/seattle/seattle-data0.edn")))

;; display first three statements in seed data transaction
(first data-tx)
(second data-tx)
(nth data-tx 2)

;; submit seed data transaction
@(d/transact conn data-tx)

;; ----- Database functions -----

(defn add-account
  "Adds an account"
  [account]
  (let [balance (bigdec (:balance account))
        res (second (:tx-data
                     @(d/transact conn
                                  [{:db/id           (d/tempid :db.part/user)
                                    :account/balance balance}])))]
    {:id      (:e res)
     :balance (:v res)}))

(defn get-account
  "Retrieves an account given it's id"
  [id]
  (let [res (first (d/q '[:find ?id ?balance
                          :in $ ?id
                          :where [?id :account/balance ?balance]]
                        (d/db conn)
                        id))]
    {:id      (first res)
     :balance (second res)}))

(defn get-accounts
  "Retrieves qall accounts"
  []
  (let [res (d/q '[:find ?a ?balance
                   :where [?a :account/balance ?balance]]
                 (d/db conn))]
    (map #(hash-map :id (first %) :balance (second %)) res)))

(defn get-transfer [id]
  (let [res (first (d/q '[:find ?id ?from-account ?to-account ?amount ?status
                          :in $ ?id
                          :where
                          [?id :transfer/from-account ?from-account]
                          [?id :transfer/to-account ?to-account]
                          [?id :transfer/amount ?amount]
                          [?id :transfer/status ?s]
                          [?s :db/ident ?status]]
                        (d/db conn)
                        id))]
    {:id           (first res)
     :from-account (second res)
     :to-account   (nth res 2)
     :amount       (nth res 3)
     :status       (nth res 4)}))

(defn make-transfer
  "Performs a transfer"
  [transfer]
  (let [amount (bigdec (:amount transfer))
        res (second (:tx-data
                     @(d/transact conn
                                  [{:db/id                 (d/tempid :db.part/user)
                                    :transfer/from-account (:from-account transfer)
                                    :transfer/to-account   (:to-account transfer)
                                    :transfer/amount       amount
                                    :transfer/status       :transfer.status/pending}])))]
    (def x @(d/transact conn [[:make-transfer
                               (:e res)
                               (:from-account transfer)
                               (:to-account transfer)
                               (:amount transfer)]]))
    (get-transfer (:e res))))

(defn get-transfers
  "Retrieves all transfers"
  []
  (let [res (d/q '[:find ?t ?from-account ?to-account ?amount ?status
                   :where
                   [?t :transfer/from-account ?from-account]
                   [?t :transfer/to-account ?to-account]
                   [?t :transfer/amount ?amount]
                   [?t :transfer/status ?s]
                   [?s :db/ident ?status]]
                 (d/db conn))]
    (map #(hash-map :id (first %)
                    :from-account (second %)
                    :to-account (nth % 2)
                    :amount (nth % 3)
                    :status (nth % 4)) res)))
