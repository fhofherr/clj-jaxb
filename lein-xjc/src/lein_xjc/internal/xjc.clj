(ns lein-xjc.internal.xjc
  (:import [com.sun.tools.xjc Driver]))

(defn mk-xjc-argv
  [target-dir schema]
  ["-d" (str target-dir) (:xsd-file schema)])

(defn xjc-main
  [argv]
  ;; TODO: fork process to avoid driver killing currently executing jvm
  (Driver/main (into-array String argv)))

(defn call-xjc
  [target-dir schema]
  (xjc-main (mk-xjc-argv target-dir schema)))
