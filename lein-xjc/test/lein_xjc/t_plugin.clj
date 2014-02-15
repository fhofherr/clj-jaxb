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
                                         :xjc-calls ..xjc-calls..}}]
               (plugin/xjc-task project) => irrelevant
               (provided
                 (td/mk-xjc-target-dir ..project-root..
                                       ..target-path..
                                       ..generated-java..)
                 => ..some-target..
                 (xjc/call-xjc ..project-root.. ..some-target.. ..xjc-calls..)
                 => irrelevant))))

(fact "extend-java-source-paths prepends the generated java directory to the
      :java-source-paths"
      (let [project {:root ..project-root..
                            :target-path ..target-path..
                            :xjc-plugin {:generated-java ..generated-java..}}
            expected-java-source-paths (cons (str ..xjc-target-dir..)
                                             (:java-source-paths project))
            expected-project (assoc project
                                    :java-source-paths
                                    expected-java-source-paths)]
        (plugin/extend-java-source-paths project) => expected-project
        (provided
          (td/mk-xjc-target-dir
            ..project-root..
            ..target-path..
            ..generated-java..)
          => ..xjc-target-dir..)))

(fact "add-prep-task adds 'xjc' to the :prep-tasks"
      (let [project {:prep-tasks ["clean" "javac" "compile"]}
            expected-project {:prep-tasks ["clean" "xjc" "javac" "compile"]}]
        (plugin/add-prep-task project) => expected-project))

(fact "middleware extends the java-source-paths and adds the xjc prep task"
      (plugin/middleware ..project..) => ..project..
      (provided
        (plugin/add-prep-task ..project..) => ..project..
        (plugin/extend-java-source-paths ..project..) => ..project..))
