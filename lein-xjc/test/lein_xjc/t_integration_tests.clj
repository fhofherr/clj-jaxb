(ns lein-xjc.t-integration-tests
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [leiningen.xjc :as xjc]
            [leiningen.core.project :as project]
            [leiningen.clean :as clean]
            [leiningen.core.user :as user]
            [midje.sweet :refer :all]))

(defn- read-test-project [name]
  "Read one of the test projects under test-projects.
  Copied (and slightly modified) from the leiningen source.
  See leiningen.test.helper."
  (with-redefs [user/profiles (constantly {})]
    (letfn [(read-prj []
              (let [prj (project/read (format
                                        "test-projects/%s/project.clj"
                                        name))]
                (project/init-project
                  (project/project-with-profiles-meta
                    prj (merge @project/default-profiles
                               (:profiles prj))))))
            (clean-prj [prj]
              (clean/clean prj) prj)]
      #(clean-prj (read-prj)))))

(defn- file-exists? [path]
  (let [file (io/file path)]
    (and (.exists file) (.isFile file))))

(defchecker java-sources-created [class-names]
  (checker [project]
           (let [generated-java (format "%s/%s"
                                        (:target-path project)
                                        (get-in project [:xjc-plugin :generated-java]))
                 src-files (map #(format "%s/%s.java"
                                         generated-java
                                         (s/replace % #"\." "/"))
                                class-names)]
             (every? file-exists? src-files))))

(fact-group
  :integration-test
  (let [project-reader (read-test-project  "single-xsd")]
    (fact "running lein xjc creates the java classes for
          the simple.xsd schema."
          (xjc/xjc (project-reader)) => (java-sources-created
                                          ["com.example.ObjectFactory"
                                           "com.example.Something"]))))
