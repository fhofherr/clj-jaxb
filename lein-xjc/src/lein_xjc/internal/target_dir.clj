(ns lein-xjc.internal.target-dir
  (:require [clojure.java.io :as io]))


(defn mk-xjc-target-dir
  [lein-project-root lein-target-path generated-java]
  (let [xjc-target-dir (io/file lein-target-path generated-java)]
    (when-not (.exists xjc-target-dir)
      (.mkdirs xjc-target-dir))
    xjc-target-dir))
