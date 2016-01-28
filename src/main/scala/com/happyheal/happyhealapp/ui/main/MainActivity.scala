package com.happyheal.happyhealapp.ui.main

import java.io.File
import java.util.UUID

import android.app.{AlertDialog, Activity}
import android.content.DialogInterface.OnClickListener
import android.content.{DialogInterface, Intent}
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.{Menu, MenuItem}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.notifications.impl.NotificationServicesComponentImpl
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.previews.PreviewsActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.{Ui, ActivityContextWrapper, ContextWrapper, Contexts}
import macroid.FullDsl._

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
          val file = ImageCapture.randomFile("jpeg")
          ImageCapture.takePhoto(file)
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
      launchPreviews()
    } else if (requestCode == ImageCapture.REQUEST_OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
      val uri = data.getData
      launchPreviews()
    }
  }

  private def launchPreviews(): Unit = {
    val previewIntent = new Intent(getApplication, classOf[PreviewsActivity])
    startActivity(previewIntent)
  }
}

object ImageCapture {

  val REQUEST_IMAGE_CAPTURE = 111

  val REQUEST_OPEN_GALLERY = 222

  val imagesFolderName = "happy_heal"

  def imagesFolder(implicit activityContextWrapper: ActivityContextWrapper) = {
    val folder = new File(android.os.Environment.getExternalStorageDirectory, ImageCapture.imagesFolderName)
    if (!folder.exists()) folder.mkdirs()
    folder
  }

  def randomFile(extension: String)(implicit activityContextWrapper: ActivityContextWrapper): File = {
    val file = new File(imagesFolder, UUID.randomUUID().toString + "." + extension.trim)
    if (! file.exists()) file.createNewFile()
    file
  }

  def takePhoto(file: File)(implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    val takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val uri = Uri.fromFile(file)
    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    if (takePhotoIntent.resolveActivity(activityContextWrapper.application.getPackageManager) != null) {
      activityContextWrapper.getOriginal.startActivityForResult(
        Intent.createChooser(takePhotoIntent, "Capture Prescription"), REQUEST_IMAGE_CAPTURE)
    } else {
      runUi(toast("No Application installed to Capture Photo.")(activityContextWrapper) <~ fry)
    }
  }

  def openGallery(implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    val openGalleryIntent = new Intent()
    openGalleryIntent.setType("image/*")
    openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT)
    if (openGalleryIntent.resolveActivity(activityContextWrapper.application.getPackageManager) != null) {
      activityContextWrapper.getOriginal.startActivityForResult(
        Intent.createChooser(openGalleryIntent, "Select Prescription"), REQUEST_OPEN_GALLERY)
    } else runUi(toast("No Application installed to Open Gallery.")(activityContextWrapper) <~ fry)
  }

}