(ns leiningen.t-xjc
  (:require [clojure.java.io :as io]
            [cljito.core :refer :all]
            [leiningen.xjc :as xjc]
            [midje.sweet :refer :all]))

(def base-project {:name "base-project"
                   :group "A (non-existent) sample project used for testing"
                   :version "0.1.0-SNAPSHOT"
                   :target-path "/path/to/missing/directory/target"
                   :root "/path/to/missing/directory"})

(defn merge-project-snippet
  [snippet & snippets]
  (merge base-project snippet snippets))

(fact "Given a valid plugin config creates a 'generated-java' directory inside
      the project's target directory."
      (let [project (merge-project-snippet {})
            target-path (:target-path project)
            generated-java "generated-java"
            mock-file (mock java.io.File)]
        (xjc/xjc project) => (fn [_]
                               (verify-> mock-file (.mkdirs))
                               true)
        (provided
          (io/file target-path "generated-java")
          => (when-> mock-file (.mkdirs) (.thenReturn true)))))
