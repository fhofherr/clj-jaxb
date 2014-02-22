(ns lein-xjc.plugin
  (:require [clojure.java.io :as io]
            [lein-xjc.internal.xjc :as xjc]))

(defn mkdir-p
  [path]
  (.. (io/file path)
      (mkdirs)))

(defn add-prep-task
  [project]
  (let [prep-tasks (mapcat #(if (= % "javac")
                              ["xjc" "javac"]
                              [%] )
                           (:prep-tasks project))]
    (assoc project :prep-tasks prep-tasks)))

(defn extend-java-source-paths
  [project]
  (let [xjc-src-path (xjc/lein-xjc-src-path project)]
    (mkdir-p xjc-src-path)
    (assoc project
           :java-source-paths
           (cons xjc-src-path (:java-source-paths project)))))

(defn xjc-task
  [project]
  (mkdir-p (xjc/lein-xjc-src-path project))
  (xjc/call-xjc project))

(defn middleware
  [project]
  (-> project
      add-prep-task
      extend-java-source-paths))
