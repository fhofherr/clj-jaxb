(ns lein-xjc.t-plugin
  (:require [cljito.core :refer :all]
            [lein-xjc.plugin :as plugin]
            [lein-xjc.internal.xjc :as xjc]
            [midje.sweet :refer :all])
  (:import [com.sun.tools.xjc Driver]))

(facts "about xjc-task"
       (fact "xjc-task creates the xjc-src-path and calls xjc"
             (plugin/xjc-task ..project..) => irrelevant
             (provided
               (xjc/lein-xjc-src-path ..project..) => ..xjc-src-path..
               (plugin/mkdir-p ..xjc-src-path..) => irrelevant
               (xjc/call-xjc ..project..) => irrelevant)))

(fact "extend-java-source-paths prepends the generated java directory to the
      :java-source-paths"
      (let [project {:java-source-paths [..existing-source-paths..]}
            expected-project {:java-source-paths [..lein-xjc-source-path..
                                                  ..existing-source-paths.. ]}]
        (plugin/extend-java-source-paths project) => expected-project
        (provided
          (xjc/lein-xjc-src-path project) => ..lein-xjc-source-path..
          (plugin/mkdir-p ..lein-xjc-source-path..) => irrelevant)))

(fact "add-prep-task adds 'xjc' to the :prep-tasks"
      (let [project {:prep-tasks ["clean" "javac" "compile"]}
            expected-project {:prep-tasks ["clean" "xjc" "javac" "compile"]}]
        (plugin/add-prep-task project) => expected-project))

(fact "middleware extends the java-source-paths and adds the xjc prep task"
      (plugin/middleware ..project..) => ..project..
      (provided
        (plugin/add-prep-task ..project..) => ..project..
        (plugin/extend-java-source-paths ..project..) => ..project..))
