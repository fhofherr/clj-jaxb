(ns lein-xjc.t-plugin
  (:require [clojure.java.io :as io]
            [cljito.core :refer :all]
            [leiningen.xjc :as xjc]
            [lein-xjc.plugin :as plugin]
            [midje.sweet :refer :all])
  (:import [com.sun.tools.xjc Driver]))

(def base-project {:name "base-project"
                   :group "A (non-existent) sample project used for testing"
                   :version "0.1.0-SNAPSHOT"
                   :target-path "/path/to/missing/directory/target"
                   :root "/path/to/missing/directory"
                   :java-source-paths ["src/main/java"]})

(defn merge-project-snippet
  [snippet & snippets]
  (merge base-project snippet snippets))

(facts "about mk-xjc-argv"
       (fact "given just a schema file and a target directory it creates an
             argv that compiles the given schema to the given target"
             (let [xsd-file "/path/to/schema.xsd"
                   schema {:xsd-file xsd-file}
                   target-dir "/path/to/target/dir" ]
               (plugin/mk-xjc-argv target-dir schema)
               => ["-d " target-dir xsd-file])))

(fact "call-xjc converts the given schema to an xjc argv and calls the xjc
      Driver"
      (plugin/call-xjc ..some-target-dir.. ..schema..) => irrelevant
      (provided
        (plugin/mk-xjc-argv ..some-target-dir.. ..schema..) => ..xjc-argv..
        (plugin/xjc-main ..xjc-argv..) => irrelevant))

(facts "about xjc-task"
       (fact "Given no plugin config creates a 'generated-java' directory inside
             the project's target directory."
             (let [project (merge-project-snippet {})
                   target-path (:target-path project)
                   generated-java "generated-java"
                   mock-file (mock java.io.File)]
               (plugin/xjc-task project) => (fn [_]
                                              (verify-> mock-file (.mkdirs))
                                              true)
               (provided
                 (io/file target-path "generated-java")
                 => (when-> mock-file (.mkdirs) (.thenReturn true)))))

       (fact "xjc-task calls xjc for each xsd file"
             (let [project (merge-project-snippet {:xjc-plugin
                                                   {:schemas [..schema1..
                                                              ..schema2..]}})]
               (plugin/xjc-task project) => irrelevant
               (provided
                 (plugin/generated-java-dir project) => ..some-target..
                 (plugin/create-generated-java-dir project) => irrelevant
                 (plugin/call-xjc ..some-target.. ..schema1..) => irrelevant
                 (plugin/call-xjc ..some-target.. ..schema2..) => irrelevant))))

(fact "middleware prepends the generated java directory to the
      :java-source-paths"
      (let [project (merge-project-snippet {:xjc-plugin
                                            {:generated-java "generated-java"}})
            generated-java-path (str (plugin/generated-java-dir project))
            expected-java-source-paths (cons generated-java-path
                                             (:java-source-paths project))
            expected-project (assoc project
                                    :java-source-paths
                                    expected-java-source-paths)]
        (plugin/middleware project) => expected-project))
