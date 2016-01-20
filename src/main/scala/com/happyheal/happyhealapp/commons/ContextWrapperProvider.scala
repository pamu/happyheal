package com.happyheal.happyhealapp.commons

import macroid.ContextWrapper

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait ContextWrapperProvider {

  implicit val contextProvider: ContextWrapper

}
