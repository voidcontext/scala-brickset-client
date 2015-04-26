# scala-brickset-client
A brickset.com api client written in Scala.

### Requirements

Scala 2.11.x

Register dependency in your build.sbt file

```sbt
resolvers ++= Seq(Resolvers.sonatypeRepo("releases")) // when using stable

resolvers ++= Seq(Resolvers.sonatypeRepo("snapshots")) // when using snapshot

libraryDependencies += "io.github.voidcontext" %% "bricksetclient" % "0.2.0-SNAPSHOT"
```

### Usage

Using BricksetClient with futures:

```scala
// import classes generated from WSDL
import io.github.voidcontext.bricksetclient.api._
// import client
import io.github.voidcontext.bricksetclient.client.BricksetClient

import scala.util.{Success, Failure}

val client = new BricksetClient(apikey, None)
val future = client.login(username, password)

future flatMap {
  case Right(hash) => client.getOwnedSets(hash)
  case Left(err) => Future { throw err }
} onComplete {
  case Success(setsOwnedByLoggedInUser) => {
      setsOwnedByLoggedInUser map { s => println(s.getName)}
      client.shutdown
    }
  case Failure(_) => {
      println("Couldn't get owned sets")
      client.shutdown
    }
}
```

You can find an example implementation in the examples direcotry.
To run the example app you should publish the bricksetclient package locally.

```bash
$ # edit credentials in examples/ownedsets.scala 
$ $EDITOR examples/ownedsets.scala
$ sbt publishLocal
$ cd examples
$ sbt run
```

### See also

- [brickset.com](http://brickset.com)
- [bricket api v2](http://brickset.com/tools/webservices/v2)

### License

The MIT License (MIT)

Copyright (c) 2015 Gabor Pihaj

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
