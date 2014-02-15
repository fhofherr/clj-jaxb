(ns lein-xjc.internal.t-xjc
  (:require [lein-xjc.internal.xjc :as xjc]
            [midje.sweet :refer :all]))

(facts "about mk-xjc-argv"
       (fact "given just a schema file and a target directory it creates an
             argv that compiles the given schema to the given target"
             (let [project-root "/absoulte/path/to/project/root"
                   xsd-file "relative/path/to/schema.xsd"
                   xjc-calls [{:xsd-file xsd-file}]
                   target-dir "/path/to/target/dir"]
               (xjc/mk-xjc-argvs project-root target-dir xjc-calls)
               => [["-d" target-dir (format "%s/%s" project-root xsd-file)]])))

(fact "call-xjc converts the given schema to an xjc argv and calls the xjc
      Driver"
      (xjc/call-xjc ..some-project-root.. ..some-target-dir.. ..xjc-calls..)
      => irrelevant
      (provided
        (xjc/mk-xjc-argvs ..some-project-root.. ..some-target-dir.. ..xjc-calls..)
        => [..xjc-argv..]
        (xjc/xjc-main ..xjc-argv..) => irrelevant))
