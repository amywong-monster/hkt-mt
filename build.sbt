name := "hkt-mt"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.2.0"

// Provides syntactic sugar of `SharedCodeExample.compute[M[_]: Monad]`
// s.t. it can call `Monad` function on `M` w/o explicitly declare `implicit M: Monad[M]`
addCompilerPlugin("org.augustjune" %% "context-applied" % "0.1.4")