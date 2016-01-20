package com.happyheal.happyhealapp.modules.notifications

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait NotificationServices {
  def showDemoNotification: Unit
}

trait NotificationServicesComponent {
  val notificationServices: NotificationServices
}
