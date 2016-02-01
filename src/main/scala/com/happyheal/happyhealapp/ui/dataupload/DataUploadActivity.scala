package com.happyheal.happyhealapp.ui.dataupload

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import macroid.{ContextWrapper, Contexts}

/**
  * Created by pnagarjuna on 01/02/16.
  */
class DataUploadActivity extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with ContextWrapperProvider
  with TypedFindView {

  override implicit val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.data_upload_layout.id)
  }

}
