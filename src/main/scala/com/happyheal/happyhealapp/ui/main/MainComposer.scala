package com.happyheal.happyhealapp.ui.main

import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.{TR, TypedFindView}
import macroid.{Ui, Contexts}
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait MainComposer {

  self: TypedFindView with Contexts[AppCompatActivity] =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val title = Option(findView(TR.title))
  lazy val description = Option(findView(TR.description))
  lazy val fab = Option(findView(TR.fab))

}
