import sbt._

object Settings {

  lazy val resolvers =
    Seq(
      Resolver.mavenLocal,
      DefaultMavenRepository,
      Resolver.typesafeRepo("releases"),
      Resolver.typesafeRepo("snapshots"),
      Resolver.typesafeIvyRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      Resolver.defaultLocal,
      "jcenter" at "http://jcenter.bintray.com",
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
    )

  lazy val proguardCommons = Seq(
    "-ignorewarnings",
    "-keep class com.fortysevendeg.** { *; }",
    "-keep class macroid.** { *; }",
    "-keep class scala.Dynamic",
    //proguard for firebase
    "-keep class com.firebase.** { *; }",
    "-keep class org.apache.** { *; }",
    "-keepnames class com.fasterxml.jackson.** { *; }",
    "-keepnames class javax.servlet.** { *; }",
    "-keepnames class org.ietf.jgss.** { *; }",
    "-dontwarn org.w3c.dom.**",
    "-dontwarn org.joda.time.**",
    "-dontwarn org.shaded.apache.**",
    "-dontwarn org.ietf.jgss.**"
  )

}
