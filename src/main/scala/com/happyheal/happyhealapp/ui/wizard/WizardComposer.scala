package com.happyheal.happyhealapp.ui.wizard

import android.content.Intent
import android.support.v4.app.{FragmentManager, Fragment}
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.widget.{LinearLayout, ImageView}
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.main.MainActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid._
import com.fortysevendeg.macroid.extras.ViewPagerTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait WizardComposer {

  self: TypedFindView with PersistenceServicesComponentImpl =>

  lazy val wizardViewPager = Option(findView(TR.wizard_view_pager))
  lazy val paginationContent = Option(findView(TR.pagination))
  lazy val gotIt = Option(findView(TR.done))

  def paginationItemStyle(implicit contextWrapper: ContextWrapper) =
    lp[LinearLayout](16 dp, 16 dp) +
      vPaddings(4 dp) +
      ivSrc(R.drawable.wizard_pager)

  def pagination(position: Int)(implicit contextWrapper: ActivityContextWrapper) = getUi(
    w[ImageView] <~ paginationItemStyle <~ vTag("position_%d".format(position))
  )

  def activateImages(position: Int) = Transformer {
    case i: ImageView if i.getTag.equals("position_%d".format(position)) => i <~ vActivated(true)
    case i: ImageView => i <~ vActivated(false)
  }

  def initializeWizard(modeTutorial: Boolean)(implicit activityContextWrapper: ActivityContextWrapper, managerContext: FragmentManagerContext[Fragment, FragmentManager]) = {
    val stepsCount = Steps.steps.length
    val duration = resGetInteger(R.integer.duration_default)
    (wizardViewPager <~
      vpAdapter(new StepsPagerAdapter(managerContext.manager)) <~
      vpOnPageChangeListener(new OnPageChangeListener {
        var isLastStep = false

        override def onPageScrollStateChanged(i: Int): Unit = {}

        override def onPageScrolled(i: Int, v: Float, i1: Int): Unit = {}

        override def onPageSelected(i: Int): Unit = {
          val ui = (modeTutorial, isLastStep, stepsCount) match {
            case (tutorial, _, steps) if !tutorial && i >= steps - 1 =>
              isLastStep = true
              (paginationContent <~~ (vGone ++ fadeOut(duration))) ~
                (gotIt <~ vVisible <~~ fadeIn(duration))
            case (tutorial, lastStep, _) if !tutorial && lastStep =>
              isLastStep = false
              (paginationContent <~ vVisible <~~ fadeIn(duration)) ~
                (gotIt <~~ (vGone ++ fadeOut(duration)))
            case _ => Ui.nop
          }
          runUi((paginationContent <~ activateImages(i)) ~ ui)
        }

      })) ~
      (paginationContent <~
        vgAddViews(0 until stepsCount map pagination) <~
        activateImages(0)) ~
      (gotIt <~ On.click {
        Ui {
          activityContextWrapper.original.get.map { activity =>
            persistenceServices.setWizardSeen(true)
            activity.finish()
            activity.startActivity(new Intent(activityContextWrapper.application, classOf[MainActivity]))
          }
        }
      })
  }

}
