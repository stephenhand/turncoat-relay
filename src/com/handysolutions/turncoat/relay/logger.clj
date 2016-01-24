(ns com.handysolutions.turncoat.relay.logger
  (require [clj-time.core :as time][clj-time.format :as time-format]))
(defn stdout [message-format & args]
  (println
    (time-format/unparse (time-format/formatters :basic-date-time) (time/now)) " - " (.getName (Thread/currentThread))   ":" (apply format message-format args)
    )
  )