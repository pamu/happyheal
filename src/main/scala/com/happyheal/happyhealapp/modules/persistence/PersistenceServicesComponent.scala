package com.happyheal.happyhealapp.modules.persistence

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait PersistenceServices {
  def setPhone(phone: String): Unit
  def getPhone(defaultValue: String): String
  def isWizardSeen: Boolean
  def setWizardSeen(seen: Boolean): Unit
  def isDemoDone(): Boolean
  def setDemoDone(done: Boolean): Unit
}

trait PersistenceServicesComponent {
  val persistenceServices: PersistenceServices
}
