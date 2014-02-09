(ns lein-xjc.internal.xjc)

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
