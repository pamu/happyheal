package com.happyheal.happyhealapp.ui.previews

import android.content.Intent
import com.happyheal.happyhealapp.ui.verification.VerificationActivity
import com.happyheal.happyhealapp.{TR, TypedFindView}
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.{ActivityContextWrapper, Ui}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait PreviewsComposer {

  self: TypedFindView =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val previews = Option(findView(TR.image_previews))
  lazy val next = Option(findView(TR.next))
  lazy val addPreview = Option(findView(TR.add_preview))
  lazy val messageCenter = Option(findView(TR.message_center))
  lazy val message = Option(findView(TR.message))


  def empty(implicit activityContextWrapper: ActivityContextWrapper) =
    (next <~ vGone) ~
      (messageCenter <~ vVisible) ~
      (message <~ vVisible) ~
      (message <~ tvText("No Medical Prescriptions Added")) ~
      (addPreview <~ vVisible) ~
      (addPreview <~ On.click {
        Ui {
          runUi(toast("hello world")(activityContextWrapper) <~ fry)
        }
      })


  def addPreviews(list: List[Preview])(implicit activityContextWrapper: ActivityContextWrapper) = {
    (previews <~ vVisible) ~
      (previews <~
        rvAdapter(new PreviewsAdapter(list)) <~
        rvAddItemDecoration(new MainItemDecorator()(activityContextWrapper))) ~
      (next <~ On.click {
        Ui {
          previews.foreach {
            rv =>
              if (rv.getAdapter != null) {
                if (rv.getAdapter.getItemCount > 0) {
                  val otpIntent = new Intent(activityContextWrapper.getOriginal, classOf[VerificationActivity])
                  activityContextWrapper.getOriginal.startActivity(otpIntent)
                } else {
                  runUi(toast("Add at least one Prescription to Go Ahead")(activityContextWrapper) <~ fry)
                }
              }
          }
        }
      })

  }

}
