(ns lein-xjc.plugin
  (:require [clojure.java.io :as io])
  (:import [com.sun.tools.xjc Driver]))

(defn lein-xjc-src-path [project]
  (format "%s/%s" (:target-path project) "lein-xjc/src"))

(defn- mk-xjc-argv
  [root target-dir xjc-call]
  {:pre (contains? xjc-call :xsd-file)}
  (letfn [(prepend-root [x] (format "%s/%s" root x))
          (append-opt [argv k f]
            (if (contains? xjc-call k)
              (into argv (f (get xjc-call k)))
              argv))]
    (-> ["-extension" "-d" target-dir]
      (append-opt :binding #(list "-b" (prepend-root %)))
      (append-opt :bindings (fn [bs] (mapcat #(list "-b" (prepend-root %)) bs)))
      (append-opt :xsd-file #(list (prepend-root %)))
      (append-opt :episode #(list "-episode" (prepend-root %))))))

(defn mk-xjc-argvs
  [project]
  {:pre [(contains? project :xjc-plugin)]}
  (let [root (:root project)
        target-dir (lein-xjc-src-path project)
        xjc-calls (get-in project [:xjc-plugin :xjc-calls] [])]
    (map  (partial mk-xjc-argv root target-dir) xjc-calls)))

(defn xjc-main
  [argv]
  (Driver/run (into-array String argv) (System/out) (System/err)))

(defn call-xjc
  [project]
  (doseq [argv (mk-xjc-argvs project)]
    (xjc-main argv))) 

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
  (let [xjc-src-path (lein-xjc-src-path project)]
    (mkdir-p xjc-src-path)
    (assoc project
           :java-source-paths
           (cons xjc-src-path (:java-source-paths project)))))

(defn xjc-task
  [project]
  (mkdir-p (lein-xjc-src-path project))
  (call-xjc project))

(defn middleware
  [project]
  (-> project
      add-prep-task
      extend-java-source-paths))
