(defproject mult-xsd-with-bindings "0.1.0-SNAPSHOT"
  :description "lein-xjc sample project with a single and simple xsd file"
  :url "http://www.example.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[clj-jaxb/lein-xjc "0.1.1"]]
  :xjc-plugin {:xjc-calls [{:xsd-file "xsd/first.xsd"
                            :binding "xsd/first-binding.jxb"}
                           {:xsd-file "xsd/second.xsd"
                            :binding "xsd/second-binding.jxb"}]})
