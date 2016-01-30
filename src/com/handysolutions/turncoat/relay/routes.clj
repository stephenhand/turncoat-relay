(ns com.handysolutions.turncoat.relay.routes
  (:require
    [ring.util.request :as request-util]
    [compojure.core :as core]
            [compojure.route :as route]
            [com.handysolutions.turncoat.relay.db :as db]
            [com.handysolutions.turncoat.relay.socket-message-channel :as socket]))
(core/defroutes all
  (core/GET "/:user/:game" [user, game]
    (fn [req] {:status  200
                 :headers {"Content-Type" "text/html"}
                 :body    (db/load-game-state user, game) }
      )
    )

  (core/POST "/:user/:game/:generation" [user, game, generation]
    (fn [req] {:status  200
               :headers {"Content-Type" "text/html"}
               :body    (db/save-game-state user, game, generation, (:body req)) }

      )
    )
  (core/GET "/messagerelay/*" [*]
    (fn [req] (socket/channel-handler  req *))
    )
  (route/not-found "Page not found")
)
