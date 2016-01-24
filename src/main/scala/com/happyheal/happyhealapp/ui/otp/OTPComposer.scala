package com.happyheal.happyhealapp.ui.otp

import com.happyheal.happyhealapp.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait OTPComposer {

  self: TypedFindView =>

  lazy val toolBar = Option(findView(TR.toolbar))

}
