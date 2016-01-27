package com.happyheal.happyhealapp.ui.main

import com.happyheal.happyhealapp.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait MainComposer {

  self: TypedFindView =>

  lazy val toolBar = Option(findView(TR.toolbar))
//  lazy val actionImage = Option(findView(TR.action_image))
  lazy val title = Option(findView(TR.title))
  lazy val description = Option(findView(TR.description))

}
