package com.happyheal.happyhealapp.ui.main

import java.io.{FileOutputStream, File}
import java.util.UUID

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.{Menu, MenuItem}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.notifications.impl.NotificationServicesComponentImpl
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.previews.PreviewsActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.{Ui, ActivityContextWrapper, ContextWrapper, Contexts}
import macroid.FullDsl._
import scala.concurrent.Future
import scala.tools.util.PathResolver.Environment
import scala.util.{Success, Failure}

/**
  * Created by pnagarjuna on 20/01/16.
  */
class MainActivity extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with TypedFindView
  with MainComposer
  with ContextWrapperProvider
  with PersistenceServicesComponentImpl
  with NotificationServicesComponentImpl {

  override implicit lazy val contextProvider: ContextWrapper = activityContextWrapper

  val LOG_TAG = classOf[MainActivity].getSimpleName

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.main_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)

    runUi {
      (fab <~ On.click {
        Ui {
          ImageCapture.takePhoto
        }
      })
    }
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_settings => return true
      case _ => return super.onOptionsItemSelected(item)
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    if (requestCode == ImageCapture.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
      val extras = data.getExtras
      val bitmap = extras.get("data").asInstanceOf[Bitmap]
      import scala.concurrent.ExecutionContext.Implicits.global
      Future {
        val external = android.os.Environment.getExternalStorageDirectory
        val happyhealDir = new File(external, "happyheal")
        if (! happyhealDir.exists()) happyhealDir.mkdirs()
        val file = new File(happyhealDir, UUID.randomUUID().toString + ".png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file))
        Uri.fromFile(file)
      } onComplete {
        case Failure(th) =>
          Log.d(LOG_TAG, "writing to file failed")
          th.printStackTrace()
        case Success(value) =>
          Log.d(LOG_TAG, s"writing to file done: $value")
          runUi(toast(s"${value.getPath}")(activityContextWrapper) <~ fry)
          launchPreviews(value)
      }
    } else if (requestCode == ImageCapture.REQUEST_OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
      val uri = data.getData
      runUi(toast(s"${uri.getPath}")(activityContextWrapper) <~ fry)
      Log.d(LOG_TAG, s"${uri.toString}")
      launchPreviews(uri)
    }
  }

  private def launchPreviews(uri: Uri): Unit = {
    persistenceServices.setFirstPreview(uri.getPath)
    val previewIntent = new Intent(getApplication, classOf[PreviewsActivity])
    startActivity(previewIntent)
  }
}

object ImageCapture {

  val LINK = "link"

  val REQUEST_IMAGE_CAPTURE = 111

  val REQUEST_OPEN_GALLERY = 222

  def takePhoto(implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    val takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePhotoIntent.resolveActivity(activityContextWrapper.application.getPackageManager) != null) {
      activityContextWrapper.getOriginal.startActivityForResult(Intent.createChooser(takePhotoIntent, "Capture Prescription"), REQUEST_IMAGE_CAPTURE)
    } else {
      runUi(toast("No Application installed to Capture Photo.")(activityContextWrapper) <~ fry)
    }
  }

  def openGallery(implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    val openGalleryIntent = new Intent()
    openGalleryIntent.setType("image/*");
    openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    if (openGalleryIntent.resolveActivity(activityContextWrapper.application.getPackageManager) != null) {
      activityContextWrapper.getOriginal.startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Prescription"), REQUEST_OPEN_GALLERY)
    } else runUi(toast("No Application installed to Open Gallery.")(activityContextWrapper) <~ fry)
  }

}