(ns com.handysolutions.turncoat.relay.db
  (require [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]))

(defn output-table-content [table-name]
      (jdbc/with-db-connection [db-con {:classname "com.microsoft.jdbc.sqlserver.SQLServerDriver"
                                        :subprotocol "sqlserver"
                                        :subname "//DIDDYKONG\\SQLEXPRESS;Initial Catalog=AdventureWorks2014"
                                        :user "sa"
                                        :password "test_1234"}]
                               (jdbc/query db-con [(string/join ["SELECT * FROM " table-name]) ] )
      )
  )