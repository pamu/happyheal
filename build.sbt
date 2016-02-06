import Libraries.android._
import Libraries.macroid._
import Libraries.playServices._
import Libraries.apacheCommons._
//import Libraries.json._
import Libraries.date._
import Libraries.graphics._
import Libraries.test._
import android.Keys._
import android.PromptPasswordsSigningConfig
import Libraries.smartTabLayout._
import Libraries.showcaseView._
import Libraries.fileutils._
import Libraries.circularLoader._
import Libraries.materialProgressBar._
import Libraries.firebase._

android.Plugin.androidBuild

platformTarget in Android := Versions.androidPlatformV

name := """happyheal"""

organization := "com.happyheal"

organizationName := "happyheal"

organizationHomepage := Some(new URL("http://pamu.github.io"))

version := Versions.appV

scalaVersion := Versions.scalaV

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-feature", "-deprecation", "-target:jvm-1.7")

resolvers ++= Settings.resolvers

libraryDependencies ++= Seq(
  aar(macroidRoot),
  aar(androidAppCompat),
  aar(androidCardView),
  aar(androidRecyclerview),
  aar(androidDesign),
  aar(macroidExtras),
  aar(playServicesBase),
  aar(smartTabLayout),
  aar(showCaseView),
  aar(circularLoader),
  materialProgressBar,
  firebase,
  fileutils,
  apacheCommonsLang,
  //json4s,
  //playJson,
  picasso,
  prettytime,
  specs2,
  mockito,
  androidTest
  //compilerPlugin(Libraries.wartRemover)
)

apkSigningConfig in Android := Option(
  PromptPasswordsSigningConfig(
    keystore = new File(Path.userHome.absolutePath + "/.android/happyheal.keystore"),
    alias = "rxbytes"))

run <<= run in Android

packageRelease <<= packageRelease in Android

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Settings.proguardCommons

//proguardCache in Android := Seq.empty

packagingOptions in Android := PackagingOptions(
  Seq("META-INF/LICENSE",
    "META-INF/LICENSE.txt",
    "META-INF/NOTICE",
    "META-INF/NOTICE.txt",
    "META-INF/LICENSE-FIREBASE.txt"))