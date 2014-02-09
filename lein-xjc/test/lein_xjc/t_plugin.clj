(ns lein-xjc.t-plugin
  (:require [clojure.java.io :as io]
            [cljito.core :refer :all]
            [leiningen.xjc :as xjc]
            [lein-xjc.plugin :as plugin]
            [midje.sweet :refer :all]))

(def base-project {:name "base-project"
                   :group "A (non-existent) sample project used for testing"
                   :version "0.1.0-SNAPSHOT"
                   :target-path "/path/to/missing/directory/target"
                   :root "/path/to/missing/directory"
                   :java-source-paths ["src/main/java"]})

(defn merge-project-snippet
  [snippet & snippets]
  (merge base-project snippet snippets))


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
