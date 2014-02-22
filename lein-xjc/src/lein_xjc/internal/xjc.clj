(ns lein-xjc.internal.xjc
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
    (-> ["-d" target-dir]
      (append-opt :binding #(list "-b" (prepend-root %)))
      (append-opt :xsd-file #(list (prepend-root %))))))

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
