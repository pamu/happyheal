package com.happyheal.happyhealapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
  * Created by pnagarjuna on 06/02/16.
  */
class GPSFetcherService extends Service {

  override def onBind(intent: Intent): IBinder = ???

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    super.onStartCommand(intent, flags, startId)
  }

}
