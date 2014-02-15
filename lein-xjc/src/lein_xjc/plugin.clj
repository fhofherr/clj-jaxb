(ns lein-xjc.plugin
  (:require [lein-xjc.internal.target-dir :as td]
            [lein-xjc.internal.xjc :as xjc]))

(def ^:private plugin-defaults
  {:xjc-plugin {:generated-java "generated-java"}})

(defn add-prep-task
  [project]
  (let [prep-tasks (mapcat #(if (= % "javac")
                              ["xjc" "javac"]
                              [%] )
                           (:prep-tasks project))]
    (assoc project :prep-tasks prep-tasks)))

(defn extend-java-source-paths
  [project]
  (let [xjc-target-dir (td/mk-xjc-target-dir (:root project)
                                             (:target-path project)
                                             (get-in project
                                                     [:xjc-plugin :generated-java]))
        old-java-source-paths (:java-source-paths project)]
    (assoc project :java-source-paths (cons (str xjc-target-dir)
                                            old-java-source-paths))))

(defn xjc-task
  [project]
  (let [merged-project (merge plugin-defaults project)
        xjc-target-dir (td/mk-xjc-target-dir (:root merged-project)
                                  (:target-path merged-project)
                                  (get-in merged-project [:xjc-plugin :generated-java]))
        xjc-calls (get-in merged-project [:xjc-plugin :xjc-calls])]
    (xjc/call-xjc (:root project) xjc-target-dir xjc-calls)))

(defn middleware
  [project]
  (-> project
      add-prep-task
      extend-java-source-paths))
