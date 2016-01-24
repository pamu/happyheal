package com.happyheal.happyhealapp.modules.smsservices.impl

import com.happyheal.happyhealapp.modules.smsservices.{SMSServicesComponent, SMSServices}

import scala.concurrent.Future

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait SMSServicesComponentImpl extends SMSServicesComponent {
  override val smsServices: SMSServices = new SMSServicesImpl

  class SMSServicesImpl extends SMSServices {
    override def verify(phno: String): Future[Boolean] = {
      import scala.concurrent.ExecutionContext.Implicits.global
      Future {
        Thread.sleep(10000)
        true
      }
    }
  }

}