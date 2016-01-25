package com.happyheal.happyhealapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
  * Created by pnagarjuna on 25/01/16.
  */
class SyncService extends Service {

  override def onBind(intent: Intent): IBinder = null

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    //TODO: asyncronously check if something is available to sync and sync if available
    //TODO: syncing will only be done for non realtime actions
    return Service.START_STICKY
  }

}
