(ns leiningen.wisp
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [me.raynes.conch
             :refer [programs with-programs let-programs]
             :as sh]
            [me.raynes.fs :as fs]))

(declare wisp-compile wisp-run wisp-repl)

(defn dispatch [project command & args]
  "Dispatches a command string to one of the plugin function."
  (->
   (case command
     "compile" wisp-compile
     "repl" wisp-repl
     "run" wisp-run)
   (partial project)
   (apply args)))

(defn wisp
  "Entry point for lein-wisp tasks."
  [project & args]
  (apply (partial dispatch project (first args)) (rest args)))

(defn compile-files [project root files]
  "Compiles .wisp files to .js in the project output directory."
  (let [out-dir (->>
                 (get-in project [:wisp :out])
                 (string/replace-first (str root) #"src\/?"))]
    (fs/mkdirs out-dir)
    (doseq [src files]
      (let [out (fs/file out-dir (string/replace-first src #"\.wisp$" ".js"))]
        (with-programs [wisp]
          (wisp {:in src :out out}))))))

(defn wisp-src? [file]
  "Returns true if file has extension .wisp"
  (= (fs/extension file) ".wisp"))

(defn compile-dir
  ([project dir]
   "Calls compile-dir with each result of fs/iterate-dir."
   (doseq [res (fs/iterate-dir dir)]
     (apply (partial compile-dir project) res)))
  ([project root dirs files]
   "Compiles each source wisp source file found."
   (compile-files project root (filter wisp-src? files))))

(defn wisp-compile
  "Compile .wisp files to Javascript."
  [project & args]
  (fs/with-cwd (:root project)
    (compile-dir project "src")))

(defn wisp-repl
  "Opens a wisp repl with current project loaded."
  [project & args]
  (println "TODO: Opening repl..."))

(defn wisp-run
  "Runs current project (how?? using lein-npm?)"
  [project & args]
  (println "TODO: Running project...."))

(defn add-aliases [project]
  "Adds wisp aliases for compile, repl and run to the project map."
  (assoc project :aliases
    (conj {} (:aliases project) {"compile" ["wisp" "compile"]
                                 "repl"    ["wisp" "repl"]
                                 "run"     ["wisp" "run"]})))

(defn init-wisp-options [project]
  "Initialize default wisp options."
  (if (get-in project [:wisp :out])
    project
    (assoc project :wisp (conj {} (:wisp project) {:out "js/"}))))

(defn middleware [project]
  "Adds wisp related entries to project map."
  (-> project add-aliases init-wisp-options))
