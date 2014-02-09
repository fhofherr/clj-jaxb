(ns lein-xjc.plugin
  (:require [lein-xjc.internal.target-dir :as td]))

(def ^:private plugin-defaults
  {:xjc-plugin {:generated-java "generated-java"}})

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
        schemas (get-in merged-project [:xjc-plugin :schemas])]
    (doseq [s schemas]
      (call-xjc xjc-target-dir s))))
