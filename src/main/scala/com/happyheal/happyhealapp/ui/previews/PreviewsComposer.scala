package com.happyheal.happyhealapp.ui.previews

import com.happyheal.happyhealapp.{R, TR, TypedFindView}

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait PreviewsComposer {

  self: TypedFindView =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val previews = Option(findView(TR.image_previews))

}
