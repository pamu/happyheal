package com.happyheal.happyhealapp.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View.OnClickListener
import android.view.{Menu, MenuItem, View}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.notifications.impl.NotificationServicesComponentImpl
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.{ContextWrapper, Ui, Contexts}
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 20/01/16.
  */
class MainActivity extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with TypedFindView
  with MainComposer
  with ContextWrapperProvider
  with NotificationServicesComponentImpl {

  override implicit lazy val contextProvider: ContextWrapper = activityContextWrapper

  val LOG_TAG = classOf[MainActivity].getSimpleName

  private val REQUEST_IMAGE_CAPTURE = 111
  private val REQUEST_OPEN_GALLERY = 222

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.main_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)

    camera.map(_.setOnClickListener(new OnClickListener {
      override def onClick(view: View): Unit = {
        takePhoto()
      }
    }))

    gallery.map(_.setOnClickListener(new OnClickListener {
      override def onClick(view: View): Unit = {
        openGallery()
      }
    }))

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

  private def takePhoto(): Unit = {
    val takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (takePhotoIntent.resolveActivity(getPackageManager) != null) {
      startActivityForResult(Intent.createChooser(takePhotoIntent, "Capture Prescription"), REQUEST_IMAGE_CAPTURE)
    } else {
      runUi(toast("No Application installed to Capture Photo.")(activityContextWrapper) <~ fry)
    }
  }

  private def openGallery(): Unit = {
    val openGalleryIntent = new Intent()
    openGalleryIntent.setType("image/*");
    openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
    if (openGalleryIntent.resolveActivity(getPackageManager) != null) {
      startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Prescription"), REQUEST_OPEN_GALLERY)
    } else runUi(toast("No Application installed to Open Gallery.")(activityContextWrapper) <~ fry)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
      val extras = data.getExtras
      val bitmap = extras.get("data").asInstanceOf[Bitmap]
      //TODO: do something with this bitmap
    }
  }

}
