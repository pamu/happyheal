package com.happyheal.happyhealapp.ui.previews

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.support.v7.widget.{LinearLayoutManager, GridLayoutManager}
import android.widget.ArrayAdapter
import com.fortysevendeg.macroid.extras.DeviceMediaQueries._
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
  lazy val message = Option(findView(TR.message))
  lazy val addPreview = Option(findView(TR.add_preview))
  lazy val noPreviewsContainer = Option(findView(TR.no_previews))
  lazy val loading = Option(findView(TR.loading))
  lazy val container = Option(findView(TR.container))

  def empty(implicit activityContextWrapper: ActivityContextWrapper) =
    (next <~ vGone) ~
      (container <~ vVisible) ~
      (noPreviewsContainer <~ vVisible) ~
      (message <~ vVisible) ~
      (message <~ tvText("No Medical Prescriptions Added")) ~
      (addPreview <~ vVisible) ~
      (addPreview <~ vVisible) ~
      (addPreview <~ On.click {
        Ui {
          val builder = new AlertDialog.Builder(activityContextWrapper.getOriginal)
          builder.setTitle("Source")

          val arrayAdapter = new ArrayAdapter[String](activityContextWrapper.getOriginal,
            android.R.layout.select_dialog_singlechoice)

          builder.setNegativeButton("Cancel", new OnClickListener {
            override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
              dialogInterface.dismiss()
            }
          })

          builder.setAdapter(arrayAdapter, new OnClickListener {
            override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
              i match {
                case 0 => runUi(toast("0 selected") <~ fry)
                case 1 => runUi(toast("1 selected") <~ fry)
              }
            }
          })

        }
      })


  def addPreviews(list: List[Preview])(implicit activityContextWrapper: ActivityContextWrapper) = {
    val layoutManager =
      landscapeTablet ?
        new GridLayoutManager(activityContextWrapper.getOriginal, 3) |
        tablet ?
          new GridLayoutManager(activityContextWrapper.getOriginal, 2) | new LinearLayoutManager(activityContextWrapper.getOriginal)

    (previews <~ vVisible) ~
      (container <~ vGone) ~
      (previews <~
        rvAdapter(new PreviewsAdapter(list)) <~
        rvLayoutManager(layoutManager) <~
        rvAddItemDecoration(new MainItemDecorator()(activityContextWrapper)))
  }
}
