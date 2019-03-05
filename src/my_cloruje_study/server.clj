(ns my-cloruje-study.server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            [my-cloruje-study.db :as db]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok       (partial response 200))
(def created  (partial response 201))
(def accepted (partial response 202))

;;;
;;; Domain functions
;;;
(defn make-list [nm]
  {:name  nm
   :items {}})

(defn make-list-item [nm]
  {:name  nm
   :done? false})

;;;
;;; API Interceptors
;;;

;; routes
(def routes
  (route/expand-routes
   #{["/account"                    :post   [db-interceptor list-create]]
     ["/accout/:id"                 :get    echo :route-name :list-query-form]}))

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))

;; For interactive development
(defonce server (atom nil))

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))

(defn test-request [verb url]
  (io.pedestal.test/response-for (::http/service-fn @server) verb url))
