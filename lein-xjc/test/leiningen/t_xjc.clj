(ns leiningen.t-xjc
  (:require [midje.sweet :refer :all]
            [leiningen.xjc :as xjc]
            [lein-xjc.plugin :as plugin]))

(fact "xjc calls the xjc-task"
      (xjc/xjc ..project..) => ..task-applied..
      (provided
        (plugin/xjc-task ..project..) => ..task-applied..))
