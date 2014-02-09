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

(defn create-generated-java-dir
  [project]
  (let [dir (generated-java-dir project)]
    (.mkdirs dir)))

(defn mk-xjc-argv
  [target-dir schema]
  ["-d " (str target-dir) (:xsd-file schema)])

(defn xjc-main
  [argv]
  ;; TODO call com.sun.tools.xjc.Driver)
  )

(defn call-xjc
  [target-dir schema]
  (xjc-main (mk-xjc-argv target-dir schema)))

(defn middleware
  [project]
  (let [dir (generated-java-dir project)
        old-java-source-paths (:java-source-paths project)]
    (assoc project :java-source-paths (cons (str dir) old-java-source-paths))))

(defn xjc-task
  [project]
  (let [merged-project (merge plugin-defaults project)
        dir (generated-java-dir merged-project)
        schemas (get-in merged-project [:xjc-plugin :schemas])]
    (create-generated-java-dir merged-project)
    (doseq [s schemas]
      (call-xjc dir s))))
