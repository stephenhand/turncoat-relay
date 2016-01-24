(defproject turncoat-relay "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [clj-time "0.11.0"]
                 [com.microsoft.jdbc/sqljdbc "4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [http-kit "2.1.19"]
                 [compojure "1.4.0"]
                 ]
  :main com.handysolutions.turncoat.relay.jar
  :resource-paths ["resources/sqljdbc4.jar"]
  :plugins [[lein-localrepo "0.5.3"]])