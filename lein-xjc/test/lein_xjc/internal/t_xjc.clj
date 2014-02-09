(ns lein-xjc.internal.t-xjc
  (:require [lein-xjc.internal.xjc :as xjc]
            [midje.sweet :refer :all]))

(facts "about mk-xjc-argv"
       (fact "given just a schema file and a target directory it creates an
             argv that compiles the given schema to the given target"
             (let [xsd-file "/path/to/schema.xsd"
                   schema {:xsd-file xsd-file}
                   target-dir "/path/to/target/dir" ]
               (xjc/mk-xjc-argv target-dir schema)
               => ["-d " target-dir xsd-file])))

(fact "call-xjc converts the given schema to an xjc argv and calls the xjc
      Driver"
      (xjc/call-xjc ..some-target-dir.. ..schema..) => irrelevant
      (provided
        (xjc/mk-xjc-argv ..some-target-dir.. ..schema..) => ..xjc-argv..
        (xjc/xjc-main ..xjc-argv..) => irrelevant))
