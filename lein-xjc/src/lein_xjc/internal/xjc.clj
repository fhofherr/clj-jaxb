(ns lein-xjc.internal.xjc
  (:import [com.sun.tools.xjc Driver]))

(defn mk-xjc-argvs
  [project-root target-dir xjc-calls]
  (map (fn [c] ["-d" (str target-dir)
                (format "%s/%s" project-root (:xsd-file c)) ])
       xjc-calls))

(defn xjc-main
  [argv]
  (Driver/run (into-array String argv) (System/out) (System/err)))

(defn call-xjc
  [project-root target-dir xjc-calls]
  (doseq [argv (mk-xjc-argvs project-root target-dir xjc-calls)]
    (xjc-main argv)))
