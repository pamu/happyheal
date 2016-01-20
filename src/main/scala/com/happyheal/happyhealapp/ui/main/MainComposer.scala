package com.happyheal.happyhealapp.ui.main

import com.happyheal.happyhealapp.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait MainComposer {

  self: TypedFindView =>

  lazy val preview = Option(findView(TR.preview))
  lazy val toolBar = Option(findView(TR.toolbar))

}
