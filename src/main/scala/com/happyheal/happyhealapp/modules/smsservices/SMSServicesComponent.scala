package com.happyheal.happyhealapp.modules.smsservices

import scala.concurrent.Future

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait SMSServicesComponent {
  val smsServices: SMSServices
}

trait SMSServices {
  def verify(phno: String): Future[Boolean]
}