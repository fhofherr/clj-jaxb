(ns lein-xjc.plugin
  (:require [clojure.java.io :as io]))

(defn generated-java-dir
  [project]
  (let [generated-java (get-in project [:xjc-plugin :generated-java])
        target-path (:target-path project)]
    (assert (not (nil? generated-java)) ":xjc-plugin :generated-java missing!")
    (io/file target-path generated-java)))

(defn middleware
  [project]
  (let [dir (generated-java-dir project)
        old-java-source-paths (:java-source-paths project)]
    (assoc project :java-source-paths (cons (str dir) old-java-source-paths))))
