(ns com.handysolutions.turncoat.relay.jar
  (:gen-class)
  (require [ com.handysolutions.turncoat.relay.web-server :as web])
  )
(defn -main [& args]
  (web/start)
  )