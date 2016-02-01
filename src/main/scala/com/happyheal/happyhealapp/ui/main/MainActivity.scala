package com.happyheal.happyhealapp.ui.main

import java.io.{FileOutputStream, FileInputStream, File}
import java.util.UUID

import android.app.{AlertDialog, Activity}
import android.content.DialogInterface.OnClickListener
import android.content.{DialogInterface, Intent}
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.{Menu, MenuItem}
import android.widget.ArrayAdapter
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.commons.utils.Utils
import com.happyheal.happyhealapp.modules.notifications.impl.NotificationServicesComponentImpl
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.previews.PreviewsActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.{Ui, ActivityContextWrapper, ContextWrapper, Contexts}
import macroid.FullDsl._

import scala.concurrent.Future
import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

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

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.main_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)

    runUi {
      (fab <~ On.click {
        Ui {
          //val file = ImageCapture.randomFile("jpeg")
          //ImageCapture.takePhoto(file)
          ImageCapture.showDialog
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
      runUi(toast("Please wait ...")(activityContextWrapper) <~ fry)
      Future {
        ImageCapture.copyFile(new File(Utils.getRealPathFromURI(getApplicationContext, uri)), ImageCapture.randomFile("jpeg"))
      } onComplete {
        case Success(sValue) =>
          runUi(toast("copying successful")(activityContextWrapper) <~ fry)
          launchPreviews()
        case Failure(fValue) =>
          runUi(toast("copying failed, reason: " + fValue.getMessage + " cause: " + fValue.getCause)(activityContextWrapper) <~ fry)
          launchPreviews()
      }
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

  private val imagesFolderName = ".happy_heal"

  def imagesFolder(implicit activityContextWrapper: ActivityContextWrapper) = {
    val folder = new File(android.os.Environment.getExternalStorageDirectory, ImageCapture.imagesFolderName)
    if (!folder.exists()) folder.mkdirs()
    folder
  }

  def randomFile(extension: String)(implicit activityContextWrapper: ActivityContextWrapper): File = {
    val file = new File(imagesFolder, "happy_heal-" + UUID.randomUUID().toString + "." + extension.trim)
    file
  }

  def copyFile(srcFile: File, destFile: File): Unit = {
    val in = new FileInputStream(srcFile)
    val out = new FileOutputStream(destFile)
    val buf = Array.ofDim[Byte](1024)
    var len: Int = 0
    while ( {
      len = in.read(buf)
      len > 0
    }) {
      out.write(buf, 0, len);
    }
    in.close()
    out.close()
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

  def showDialog(implicit activityContextWrapper: ActivityContextWrapper) = {
    val builder = new AlertDialog.Builder(activityContextWrapper.getOriginal)
    builder.setTitle("Source")

    val arrayAdapter = new ArrayAdapter[String](
      activityContextWrapper.getOriginal,
      android.R.layout.select_dialog_item, Array[String]("Camera", "Gallery"))

    builder.setAdapter(arrayAdapter, new OnClickListener {
      override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
        i match {
          case 0 =>
            val file = randomFile("jpeg")
            takePhoto(file)
          case 1 =>
            openGallery
          case 2 =>
            runUi(toast("Nothing, to do")(activityContextWrapper) <~ fry)
        }
      }
    })

    builder.setNegativeButton("Cancel", new OnClickListener {
      override def onClick(dialogInterface: DialogInterface, i: Int): Unit = {
        dialogInterface.dismiss()
      }
    })

    builder.show()
  }

}