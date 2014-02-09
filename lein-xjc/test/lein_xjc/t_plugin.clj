(ns lein-xjc.t-plugin
  (:require [cljito.core :refer :all]
            [lein-xjc.plugin :as plugin]
            [lein-xjc.internal.target-dir :as td]
            [lein-xjc.internal.xjc :as xjc]
            [midje.sweet :refer :all])
  (:import [com.sun.tools.xjc Driver]))

(facts "about xjc-task"
       (fact "It creates a generated-java directory"
             (let [project {:root ..project-root..
                            :target-path ..target-path..
                            :xjc-plugin {:generated-java ..generated-java..}}]
               (plugin/xjc-task project) => irrelevant
               (provided
                 (td/mk-xjc-target-dir ..project-root..
                                       ..target-path..
                                       ..generated-java..)
                 => ..xjc-target-dir..)))

       (fact "xjc-task calls xjc for each xsd file"
             (let [project {:root ..project-root..
                            :target-path ..target-path..
                            :xjc-plugin {:generated-java ..generated-java..
                                         :schemas [..schema1..
                                                   ..schema2..]}}]
               (plugin/xjc-task project) => irrelevant
               (provided
                 (td/mk-xjc-target-dir ..project-root..
                                       ..target-path..
                                       ..generated-java..)
                 => ..some-target..
                 (xjc/call-xjc ..some-target.. ..schema1..) => irrelevant
                 (xjc/call-xjc ..some-target.. ..schema2..) => irrelevant))))

(fact "middleware prepends the generated java directory to the
      :java-source-paths"
      (let [project {:root ..project-root..
                            :target-path ..target-path..
                            :xjc-plugin {:generated-java ..generated-java..}}
            expected-java-source-paths (cons (str ..xjc-target-dir..)
                                             (:java-source-paths project))
            expected-project (assoc project
                                    :java-source-paths
                                    expected-java-source-paths)]
        (plugin/middleware project) => expected-project
        (provided
          (td/mk-xjc-target-dir
            ..project-root..
            ..target-path..
            ..generated-java..)
          => ..xjc-target-dir..)))
