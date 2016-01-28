package com.happyheal.happyhealapp.modules.persistence

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait PersistenceServices {
  def isLoggedIn: Boolean
  def setLoggedIn(isLoggedIn: Boolean): Unit
  def isWizardSeen: Boolean
  def setWizardSeen(seen: Boolean): Unit
}

trait PersistenceServicesComponent {
  val persistenceServices: PersistenceServices
}
