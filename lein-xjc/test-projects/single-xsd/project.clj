(defproject single-xsd "0.1.0-SNAPSHOT"
  :description "lein-xjc sample project with a single and simple xsd file"
  :url "http://www.example.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-xjc "0.1.0-SNAPSHOT"]]
  :xjc-plugin {:generated-java "generated-java"
               :schemas [{:xsd-file "xsd/simple.xsd"}]})
