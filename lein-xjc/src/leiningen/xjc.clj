(ns leiningen.xjc
  (:require [lein-xjc.plugin :refer :all]))

(def ^:private plugin-defaults
  {:xjc-plugin {:generated-java "generated-java"}})

(defn- create-generated-java-dir
  [project]
  (let [dir (generated-java-dir project)]
    (.mkdirs dir)))

(defn xjc
  [project & args]
  (let [merged-project (merge plugin-defaults project)]
    (create-generated-java-dir merged-project)))
