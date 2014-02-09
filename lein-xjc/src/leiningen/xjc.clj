(ns leiningen.xjc
  (:require [clojure.java.io :as io]))

(def ^:private plugin-defaults
  {:xjc-plugin {:generated-java "generated-java"}})

(defn- create-generated-java-dir
  [project]
  (let [generated-java (get-in project [:xjc-plugin :generated-java])
        target-path (:target-path project)
        generated-java-dir (io/file target-path generated-java)]
    (.mkdirs generated-java-dir)))

(defn xjc
  [project & args]
  (let [merged-project (merge plugin-defaults project)]
    (create-generated-java-dir merged-project)))
