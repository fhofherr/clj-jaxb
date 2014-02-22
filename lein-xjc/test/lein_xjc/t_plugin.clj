(ns lein-xjc.t-plugin
  (:require [cljito.core :refer :all]
            [lein-xjc.plugin :as plugin]
            [midje.sweet :refer :all])
  (:import [com.sun.tools.xjc Driver]))

(facts "about mk-xjc-argvs"

       (fact "it fails for a project without xjc-plugin configuration"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"}]
               (plugin/mk-xjc-argvs prj) => (throws AssertionError)))

       (fact "it creates no argvs if no xjc-calls are configured"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {}}]
               (plugin/mk-xjc-argvs prj) => empty?))

       (fact "it creates an argv for each given xsd file"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-file "some.xsd"}]}}
                   expected-argv ["-extension"
                                  "-d" (plugin/lein-xjc-src-path prj)
                                  (format "%s/%s" (:root prj) "some.xsd")]]
               (plugin/mk-xjc-argvs prj) => [expected-argv]))

       (fact "if a bindings file is given it is taken into account"
             (let [prj {:root "/some/absolute/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-file "some.xsd"
                                                  :binding "some-binding.jxb"}]}}
                   expected-argv ["-extension"
                                  "-d" (plugin/lein-xjc-src-path prj)
                                  "-b" (format "%s/%s" (:root prj) "some-binding.jxb")
                                  (format "%s/%s" (:root prj) "some.xsd")]]
               (plugin/mk-xjc-argvs prj) => [expected-argv]))

       (fact "if multiple binding files are given they are all taken into account"
             (let [prj {:root "/some/absoulte/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-file "some.xsd"
                                                  :bindings ["binding1.jxb"
                                                             "binding2.jxb"]}]}}
                   expected-argv ["-extension"
                                  "-d" (plugin/lein-xjc-src-path prj)
                                  "-b" (format "%s/%s" (:root prj) "binding1.jxb")
                                  "-b" (format "%s/%s" (:root prj) "binding2.jxb")
                                  (format "%s/%s" (:root prj) "some.xsd")]]
               (plugin/mk-xjc-argvs prj) => [expected-argv]))

       (fact "adds arguments to create episode files"
             (let [prj {:root "/some/absoulte/path"
                        :target-path "/path/to/target/dir"
                        :xjc-plugin {:xjc-calls [{:xsd-files "some.xsd"
                                                  :episode "episode.file"}]}}
                   expected-argv ["-extension"
                                  "-d" (plugin/lein-xjc-src-path prj)
                                  "-episode" (format "%s/%s"
                                                     (:root prj)
                                                     "episode.file")]]
               (plugin/mk-xjc-argvs prj) => [expected-argv])))

(fact "call-xjc converts the given schema to an xjc argv and calls the xjc
      Driver"
      (plugin/call-xjc ..project..)
      => irrelevant
      (provided
        (plugin/mk-xjc-argvs ..project..) => [..xjc-argv..]
        (plugin/xjc-main ..xjc-argv..) => irrelevant)) 

(facts "about xjc-task"
       (fact "xjc-task creates the xjc-src-path and calls xjc"
             (plugin/xjc-task ..project..) => irrelevant
             (provided
               (plugin/lein-xjc-src-path ..project..) => ..xjc-src-path..
               (plugin/mkdir-p ..xjc-src-path..) => irrelevant
               (plugin/call-xjc ..project..) => irrelevant)))

(fact "extend-java-source-paths prepends the generated java directory to the
      :java-source-paths"
      (let [project {:java-source-paths [..existing-source-paths..]}
            expected-project {:java-source-paths [..lein-xjc-source-path..
                                                  ..existing-source-paths.. ]}]
        (plugin/extend-java-source-paths project) => expected-project
        (provided
          (plugin/lein-xjc-src-path project) => ..lein-xjc-source-path..
          (plugin/mkdir-p ..lein-xjc-source-path..) => irrelevant)))

(fact "add-prep-task adds 'xjc' to the :prep-tasks"
      (let [project {:prep-tasks ["clean" "javac" "compile"]}
            expected-project {:prep-tasks ["clean" "xjc" "javac" "compile"]}]
        (plugin/add-prep-task project) => expected-project))

(fact "middleware extends the java-source-paths and adds the xjc prep task"
      (plugin/middleware ..project..) => ..project..
      (provided
        (plugin/add-prep-task ..project..) => ..project..
        (plugin/extend-java-source-paths ..project..) => ..project..))
