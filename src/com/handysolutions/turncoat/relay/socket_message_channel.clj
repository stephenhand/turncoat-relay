(ns com.handysolutions.turncoat.relay.socket-message-channel
  (require [org.httpkit.server :as httpkit-server]
           [clojure.core.async :as a
           :refer [>! <! >!! <!! go chan buffer close! thread
                   alts! alts!! timeout]])
  )
(def address-channels (atom {}))

(defn channel-handler [req raw-address]
  (httpkit-server/with-channel req ws-channel
    (def send-only (.endsWith raw-address "/send_only"))
    (def address (if send-only (subs raw-address 0 (-(count raw-address) 10)) raw-address))
    (defn retrieve-channel [address]
      (if-not (contains? @address-channels address)
        (swap! address-channels conj @address-channels [address (chan 50)])
      )
      (get @address-channels address)
      )
    (httpkit-server/on-close ws-channel
        (fn [status]
        (println "channel closed (" address "): " status)
      )
    )
    (defn relay-message [relay-data]
      (httpkit-server/send! ws-channel relay-data)
      (println "relaying message (" address "): " relay-data)
    )
    (if-not send-only
      (go
        (relay-message (<! (retrieve-channel address)))
      )
    )
    (httpkit-server/on-receive ws-channel
      (fn [data]
        (go
          (>! (retrieve-channel address) data)
        )
      )
    )
  )
)