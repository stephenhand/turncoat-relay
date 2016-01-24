(ns com.handysolutions.turncoat.relay.socket-message-channel
  (require [org.httpkit.server :as httpkit-server]
           [clojure.core.async :as a
            :refer [>! <! >!! <!! go go-loop chan buffer close! thread
                    alts! alts!! timeout]]
           [com.handysolutions.turncoat.relay.logger :as log]
           [clojure.string :as string])
  )
(def address-channels (atom {}))
(defn retrieve-channel [address]
  (if-not
    (contains? @address-channels address)
    (do
      (log/stdout "creating new relay channel: %s" address)
      (def new-channel (chan 50))
      (swap! address-channels assoc address new-channel)
      (log/stdout "new channel added to pool: %s" address)
      new-channel
      )

    )

  (log/stdout "retrieving relay channel: %s" address)
  (get @address-channels address)
  )

(defn channel-handler [req ws-address]
  (httpkit-server/with-channel req ws-channel

    (httpkit-server/on-close ws-channel
      (fn [status]
        (log/stdout "channel closed (%s): %s" ws-address status)
        )
    )
    (httpkit-server/on-receive ws-channel
      (fn [data]
        (go
          (let [message (zipmap [:recipient :content] (string/split data #"\r\n"))]
            (log/stdout "directing message (%s > %s): %s" ws-address (get message :recipient) (get message :content) data)
            (>!
              (retrieve-channel (get message :recipient)) (get message :content)
              )
            )
          )
        )
      )
    (log/stdout "relay channel %s open, relaying to %s" ws-address (:uri req))

    (retrieve-channel ws-address)
    (let [relay-message (fn [relay-data]
        (httpkit-server/send! ws-channel relay-data)
        (log/stdout "relaying message (%s > %s): %s" ws-address (:uri req) relay-data)
        relay-data)]
      (go
        (loop []
          (log/stdout "waiting for data (%s)" ws-address)
          (relay-message
            (<!! (retrieve-channel ws-address))
            )
          (log/stdout "message relayed to %s" (:uri req))
          (recur)
          )
        )
      )
    )
  )