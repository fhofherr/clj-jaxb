(ns lein-xjc.plugin
  (:require [clojure.java.io :as io]))

(def ^:private plugin-defaults
  {:xjc-plugin {:generated-java "generated-java"}})

(defn generated-java-dir
  [project]
  (let [generated-java (get-in project [:xjc-plugin :generated-java])
        target-path (:target-path project)]
    (assert (not (nil? generated-java)) ":xjc-plugin :generated-java missing!")
    (io/file target-path generated-java)))

(defn- create-generated-java-dir
  [project]
  (let [dir (generated-java-dir project)]
    (.mkdirs dir)))

(defn middleware
  [project]
  (let [dir (generated-java-dir project)
        old-java-source-paths (:java-source-paths project)]
    (assoc project :java-source-paths (cons (str dir) old-java-source-paths))))

(defn xjc-task
  [project]
  (let [merged-project (merge plugin-defaults project)]
    (create-generated-java-dir merged-project)))
