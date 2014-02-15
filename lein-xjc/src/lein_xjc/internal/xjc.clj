(ns lein-xjc.internal.xjc
  (:import [com.sun.tools.xjc Driver]))

(defn mk-xjc-argvs
  [project-root target-dir xjc-calls]
  (map (fn [c] ["-d" (str target-dir)
                (format "%s/%s" project-root (:xsd-file c)) ])
       xjc-calls))

(defn xjc-main
  [argv]
  ;; TODO: fork process to avoid driver killing currently executing jvm
  (Driver/main (into-array String argv)))

(defn call-xjc
  [project-root target-dir xjc-calls]
  (doseq [argv (mk-xjc-argvs project-root target-dir xjc-calls)]
    (println argv)
    (xjc-main argv)))
