# lein-wisp

A Leiningen plugin to compile [wisp](https://github.com/gozalla/wisp) source files to Javascript.

[![Clojars Project](http://clojars.org/lein-wisp/latest-version.svg)](http://clojars.org/lein-wisp)

## Usage

Put `[lein-wisp "0.1.0-SNAPSHOT"]` into the `:plugins` vector and `leinignen.wisp/middleware` into the `:middleware` vector of your project.clj.

    $ lein compile

Here is a sample project.clj:

```clojure
(defproject example "0.1.0-SNAPSHOT"
  :plugins [[lein-wisp "0.1.0-SNAPSHOT"]]
  :middleware [leiningen.wisp/middleware])
```

You can use [lein-npm](https://github.com//lein-npm) to manage npm dependencies with Leiningen as well.

## License

Copyright © 2014 Paul d'Hubert

Distributed under the BSD 3-Clause License.
