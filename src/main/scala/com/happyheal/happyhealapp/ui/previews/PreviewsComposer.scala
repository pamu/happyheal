package com.happyheal.happyhealapp.ui.previews

import java.io.{File, FilenameFilter}

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.OnScrollListener
import com.happyheal.happyhealapp.commons.ui.AutofitRecyclerView
import com.happyheal.happyhealapp.ui.address.AddressActivity
import com.happyheal.happyhealapp.ui.main.ImageCapture
import com.happyheal.happyhealapp.{TR, TypedFindView}
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import macroid.{Tweak, Contexts, ActivityContextWrapper, Ui}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import org.apache.commons.io.FilenameUtils

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait PreviewsComposer {

  self: TypedFindView with Contexts[AppCompatActivity] =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val previews = Option(findView(TR.image_previews))
  lazy val next = Option(findView(TR.next))
  lazy val addPreview = Option(findView(TR.add_preview))
  lazy val messageCenter = Option(findView(TR.message_center))
  lazy val message = Option(findView(TR.message))

  def rvScroll: Tweak[AutofitRecyclerView] = {
    Tweak[AutofitRecyclerView] { rv =>
      rv.addOnScrollListener(new OnScrollListener {
        override def onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int): Unit = {
          if (dy > 0) runUi(next <~ vGone)
          else runUi(next <~ vVisible)
        }
      })
    }
  }

  def reload: Unit = {

    val files = ImageCapture.imagesFolder.listFiles(new FilenameFilter {
      override def accept(file: File, s: String): Boolean = FilenameUtils.getExtension(s) == "jpeg"
    })

    if (files != null && files.length > 0) {
      Future {
        files.map { file =>
          Preview(file)
        }.toList
      } mapUi { previews =>
        addPreviews(previews)
      } recoverUi {
        case ex =>
          ex.printStackTrace()
          Ui {}
      }
    } else {
      runUi(empty)
    }

  }

  def empty(implicit activityContextWrapper: ActivityContextWrapper) =
    (previews <~ vGone) ~
      (next <~ vGone) ~
      (messageCenter <~ vVisible) ~
      (message <~ vVisible) ~
      (message <~ tvText("No Medical Prescriptions Added")) ~
      (addPreview <~ vVisible) ~
      (addPreview <~ On.click {
        Ui {
          ImageCapture.showDialog()(activityContextWrapper)
        }
      })


  def addPreviews(list: List[Preview])(implicit activityContextWrapper: ActivityContextWrapper) =
    (next <~ vVisible) ~
      (messageCenter <~ vGone) ~
      (previews <~ rvScroll) ~
      (previews <~ vVisible) ~
      (previews <~ rvAdapter(new PreviewsAdapter(list)(reload))) ~
      //(previews <~ rvAddItemDecoration(new MainItemDecorator()(activityContextWrapper))) ~
      (next <~ On.click {
        Ui {
          previews.foreach {
            rv =>
              if (rv.getAdapter != null) {
                if (rv.getAdapter.getItemCount > 0) {
                  val addressIntent = new Intent(activityContextWrapper.getOriginal, classOf[AddressActivity])
                  activityContextWrapper.getOriginal.startActivity(addressIntent)
                } else {
                  runUi(empty)
                  runUi(toast("Add at least one Prescription to Go Ahead")(activityContextWrapper) <~ fry)
                }
              }
          }
        }
      })

}
