package com.happyheal.happyhealapp.ui.otp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.Contexts

/**
  * Created by pnagarjuna on 24/01/16.
  */
class OTPActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with TypedFindView with OTPComposer {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.otp_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    //val key = getApplicationContext.getString(R.string.msg91_app_key)
  }

  override def onResume(): Unit = {
    super.onResume()

  }



}
