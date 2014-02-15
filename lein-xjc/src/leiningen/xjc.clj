(ns leiningen.xjc
  (:require [lein-xjc.plugin :as plugin]))

(defn xjc
  [project & args]
  (plugin/xjc-task project)
  project)
