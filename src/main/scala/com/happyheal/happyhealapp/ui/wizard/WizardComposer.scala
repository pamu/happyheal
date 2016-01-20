package com.happyheal.happyhealapp.ui.wizard

import com.happyheal.happyhealapp.{TR, TypedFindView}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait WizardComposer {

  self: TypedFindView =>

  lazy val wizardViewPager = Option(findView(TR.wizard_view_pager))

}
