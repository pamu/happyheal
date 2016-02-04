package com.happyheal.happyhealapp.ui.orders

import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider

/**
  * Created by pnagarjuna on 04/02/16.
  */
trait OrdersComposer {
  self: TypedFindView with ContextWrapperProvider =>

  lazy val toolbar = Option(findView(TR.toolbar))


}
