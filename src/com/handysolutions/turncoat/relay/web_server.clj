(ns com.handysolutions.turncoat.relay.web-server
  (require
    [compojure.core :refer :all]
    [ring.middleware.defaults :refer :all]
    [org.httpkit.server :as httpkit-server]
    [com.handysolutions.turncoat.relay.routes :as routes])

  )
(def site
  (wrap-defaults routes/all
    (assoc site-defaults :security
      (assoc (:security site-defaults) :anti-forgery false)
      )
    )
  )
(defn start [] (httpkit-server/run-server site {:port 8088})
 )
