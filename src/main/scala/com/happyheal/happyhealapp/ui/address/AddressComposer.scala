package com.happyheal.happyhealapp.ui.address

import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider

/**
  * Created by pnagarjuna on 31/01/16.
  */
trait AddressComposer {
  self: TypedFindView with ContextWrapperProvider =>

  lazy val toolbar = Option(findView(TR.toolbar))


}
