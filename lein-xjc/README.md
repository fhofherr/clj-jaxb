# lein-xjc

A Leiningen plugin that uses xjc to compile Java bindings from XML Schema files.

## Usage

Put `[clj-jaxb/lein-xjc "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your
`:user` profile.

Additionally configure the plugin by adding an `:xjc-plugin` entry to your
project definition:

```clojure
...
:xjc-plugin {:xjc-calls [{:xsd-file "xsd/simple.xsd"}]}
...
```

At the moment the only required entry in the `:xjc-plugin` map is a vector
named `:xjc-calls`. It contains a map for each XML Schema file you want
compiled. The map configures the individual calls to xjc and requires at least
the entry `:xsd-file`.

In addition to that it is possible to specify a bindings file via the
`:binding` keyword:

```clojure
...
:xjc-plugin {:xjc-calls [{:xsd-file "xsd/first.xsd"
                          :binding "xsd/first-binding.jxb"}]}
...
```
Or, mutliplie bindings for one call by using the `:bindings` keyword:

```clojure
...
:xjc-plugin {:xjc-calls [{:xsd-file "xsd/first.xsd"
                          :bindings ["xsd/first-binding.jxb"
                                     "xsd/other-binding.jxb"]}]}
...
```
Episode files can be created by giving the `:episode` keyword:

```clojure
:xjc-plugin {:xjc-calls [{:xsd-file "xsd/simple.xsd"
                          :episode "target/lein-xjc/episode"}]}
```

The created episode file can then be used as a binding in another xjc call.

All paths are relative to the project root.

Calling

    $ lein xjc

creates the bindings and puts them into `target/lein-xjc/src`. lein-xjc
automatically prepends the `target/lein-xjc/src` directory to your project's
`:java-source-paths`. Additionally it inserts the `xjc` task into your project's
`:prep-tasks` just before the `javac` task. Therefore calling

    lein install

creates your bindings, and ensures they get compiled into java classes.

## License

Copyright © 2014 Ferdinand Hofherr

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
