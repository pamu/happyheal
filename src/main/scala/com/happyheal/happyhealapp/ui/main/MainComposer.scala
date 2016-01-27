package com.happyheal.happyhealapp.ui.main

import com.happyheal.happyhealapp.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait MainComposer {

  self: TypedFindView =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val title = Option(findView(TR.title))
  lazy val description = Option(findView(TR.description))
  lazy val fab = Option(findView(TR.fab))

}
