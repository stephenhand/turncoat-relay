(ns com.handysolutions.turncoat.relay.db
  (require [clojure.java.jdbc :as jdbc]))
(def connection-data {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
                      :subprotocol "sqlserver"
                      :subname "//DIDDYKONG\\SQLEXPRESS;Initial Catalog=turncoat"
                      :user "sa"
                      :password "test_1234"})
(defn load-game-state [user, game]
  (jdbc/with-db-connection [db-con connection-data]
    (jdbc/query db-con ["SELECT TOP 1 State FROM turncoat.dbo.GameState WHERE [Username] = ? AND Game = ? ORDER BY Generation DESC" user, game] )
    )
  )

(defn save-game-state [user, game, generation, state-stream]
  (.reset state-stream)
  (jdbc/with-db-connection [db-con connection-data]
    (jdbc/insert! db-con "turncoat.dbo.GameState" {:username user, :game game, :generation generation, :state (slurp state-stream)})
    "OK"
    )
  )