package com.happyheal.happyhealapp.receiver

import android.content.{Intent, Context, BroadcastReceiver}
import android.util.Log

/**
  * Created by pnagarjuna on 25/01/16.
  */
class BootReceiver extends BroadcastReceiver {

  private val LOG_TAG = classOf[BootReceiver].getSimpleName

  override def onReceive(context: Context, intent: Intent): Unit = {
    val action = intent.getAction

    Log.d(LOG_TAG, s"action: ${action}")

    if (action == Intent.ACTION_BOOT_COMPLETED) {
      //TODO: start a sync service and check if something is there is to sync
    }

  }

}
