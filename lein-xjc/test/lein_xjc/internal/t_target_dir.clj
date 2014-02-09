(ns lein-xjc.internal.t-target-dir
  (:require [lein-xjc.internal.target-dir :as td]
            [clojure.java.io :as io]
            [cljito.core :refer :all]
            [midje.sweet :refer :all]))

(facts "about mk-xjc-target-dir"
       (let [lein-target-path "/absolute/path/to/project/target"
             lein-project-root "/absoulte/path/to/project"
             generated-java "generated-java"
             mock-file (mock java.io.File)]
         (fact "it returns a java.io.File instance"
               (td/mk-xjc-target-dir lein-project-root
                                     lein-target-path
                                     generated-java)
               => mock-file
               (provided
                 (io/file lein-target-path generated-java) => mock-file))
         (fact "it creates the xjc-target-dir if it is missing"
               (do
                 (when-> mock-file (.exists) (.thenReturn false))
                 (td/mk-xjc-target-dir lein-project-root
                                       lein-target-path
                                       generated-java))
               => (fn [_]
                    (verify-> mock-file (.mkdirs))
                    mock-file))))
