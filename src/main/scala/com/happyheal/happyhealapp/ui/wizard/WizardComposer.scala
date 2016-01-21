package com.happyheal.happyhealapp.ui.wizard

import android.support.v4.app.{FragmentManager, Fragment}
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import com.happyheal.happyhealapp.{TR, TypedFindView}
import macroid.{FragmentManagerContext, ActivityContextWrapper}
import com.fortysevendeg.macroid.extras.ViewPagerTweaks._
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait WizardComposer {

  self: TypedFindView =>

  lazy val wizardViewPager = Option(findView(TR.wizard_view_pager))

  def initializeWizard(implicit activityContextWrapper: ActivityContextWrapper, managerContext: FragmentManagerContext[Fragment, FragmentManager]) = {
    wizardViewPager <~
      vpAdapter(new StepsPagerAdapter(managerContext.manager)) <~
      vpOnPageChangeListener(new OnPageChangeListener {

        override def onPageScrollStateChanged(i: Int): Unit = {}

        override def onPageScrolled(i: Int, v: Float, i1: Int): Unit = {}

        override def onPageSelected(i: Int): Unit = {}

      })
  }

}
