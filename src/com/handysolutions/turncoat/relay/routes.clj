(ns com.handysolutions.turncoat.relay.routes
  (:require [compojure.core :as core]
            [compojure.route :as route]
            [com.handysolutions.turncoat.relay.db :as db]
            [com.handysolutions.turncoat.relay.socket-message-channel :as socket]))
(core/defroutes all
  (core/GET "/:table-name" [table-name]
     (fn [req] {:status  200
                 :headers {"Content-Type" "text/html"}
                 :body    (db/output-table-content table-name) }

        )
     )
  (core/GET "/messagerelay/*" [*]
    (fn [req] (socket/channel-handler  req *))
    )
  (route/not-found "Page not found")
)
