(defproject clj-jaxb/lein-xjc "0.1.1"
  :description "Call xjc from leiningen."
  :url "http://github.com/fhofherr/clj-jaxb"
  :scm {:name "git"
        :url "https://github.com/fhofherr/clj-jaxb"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.sun.xml.bind/jaxb-xjc "2.2.7"]]
  :eval-in-leiningen true
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[midje "1.6.2"]
                                  [cljito "0.2.1"]
                                  [org.mockito/mockito-all "1.9.5"]]
                   :plugins [[lein-midje "3.1.3"]]}})
