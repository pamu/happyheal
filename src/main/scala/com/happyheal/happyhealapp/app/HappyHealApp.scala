package com.happyheal.happyhealapp.app

import android.app.Application
import android.util.Log

/**
  * Created by pnagarjuna on 20/01/16.
  */
class HappyHealApp extends Application {

  val LOG_TAG = classOf[HappyHealApp].getSimpleName

  override def onCreate(): Unit = {
    super.onCreate()
    Log.d(LOG_TAG, "HappyHeal init")
  }

}
