(ns lein-xjc.internal.t-xjc
  (:require [lein-xjc.internal.xjc :as xjc]
            [midje.sweet :refer :all]))

(facts "about mk-xjc-argvs"

       (fact "it fails for a project without xjc-plugin configuration"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"}]
               (xjc/mk-xjc-argvs prj) => (throws AssertionError)))

       (fact "it creates no argvs if no xjc-calls are configured"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {}}]
               (xjc/mk-xjc-argvs prj) => empty?))

       (fact "it creates an argv for each given xsd file"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-file "some.xsd"}]}}
                   expected-argv ["-d" (xjc/lein-xjc-src-path prj)
                                  (format "%s/%s" (:root prj) "some.xsd")]]
               (xjc/mk-xjc-argvs prj) => [expected-argv]))

       (fact "if a bindings file is given it is taken into account"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-file "some.xsd"
                                                  :binding "some-binding.jxb"}]}}
                   expected-argv ["-d" (xjc/lein-xjc-src-path prj)
                                  "-b" (format "%s/%s" (:root prj) "some-binding.jxb")
                                  (format "%s/%s" (:root prj) "some.xsd")]]
               (xjc/mk-xjc-argvs prj) => [expected-argv])))

(fact "call-xjc converts the given schema to an xjc argv and calls the xjc
      Driver"
      (xjc/call-xjc ..project..)
      => irrelevant
      (provided
        (xjc/mk-xjc-argvs ..project..) => [..xjc-argv..]
        (xjc/xjc-main ..xjc-argv..) => irrelevant))
