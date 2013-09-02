import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "Gooby"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      javaCore,
      javaJdbc,
      javaEbean,
      // Add your project dependencies here,
      "org.imgscalr" % "imgscalr-lib" % "4.2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here
      resolvers ++= Seq(
        "The Buzz Media Maven Repository" at "http://maven.thebuzzmedia.com"
      )
    )

}
