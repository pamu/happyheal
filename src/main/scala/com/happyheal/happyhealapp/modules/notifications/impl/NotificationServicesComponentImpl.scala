package com.happyheal.happyhealapp.modules.notifications.impl

import android.app.{PendingIntent, NotificationManager}
import android.content.{Intent, Context}
import android.support.v4.app.NotificationCompat
import com.happyheal.happyhealapp.R
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.notifications.{NotificationServices, NotificationServicesComponent}
import com.happyheal.happyhealapp.ui.main.MainActivity

import scala.util.Random

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait NotificationServicesComponentImpl
  extends NotificationServicesComponent {

  self: ContextWrapperProvider =>

  override lazy val notificationServices = new NotificationServicesImpl

  class NotificationServicesImpl extends NotificationServices {

    override def showDemoNotification: Unit = {
      val requestCode = 112233
      val notificationId = Random.nextInt()
      val intent = new Intent(contextProvider.application, classOf[MainActivity])
      val pendingIntent = PendingIntent.getActivity(contextProvider.application, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      val notification = new NotificationCompat.Builder(contextProvider.application)
        .setContentTitle("Demo Notification")
        .setContentText("Demo Content")
        .setContentInfo("Demo info")
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_launcher)
        .setAutoCancel(true)
        .build()
      val notifyManager = contextProvider.application.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
      notifyManager.notify(notificationId, notification)
    }

  }

}
