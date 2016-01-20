package com.happyheal.happyhealapp.modules.persistence.impl

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.{PersistenceServices, PersistenceServicesComponent}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait PersistenceServicesComponentImpl
  extends PersistenceServicesComponent {

  self: ContextWrapperProvider =>

  val loggedInKey = "loggedIn_key"
  val wizardWasSeenKey = "wizard_was_seen_key"

  override lazy val persistenceServices = new PersistenceServicesImpl

  class PersistenceServicesImpl extends PersistenceServices {

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(contextProvider.application)

    override def setLoggedIn(isLoggedIn: Boolean): Unit = sharedPreferences.edit().putBoolean(loggedInKey, isLoggedIn).commit()
    override def setWizardSeen(seen: Boolean): Unit = sharedPreferences.edit().putBoolean(wizardWasSeenKey, seen).commit()
    override def isLoggedIn: Boolean = sharedPreferences.getBoolean(loggedInKey, false)
    override def isWizardSeen: Boolean = sharedPreferences.getBoolean(wizardWasSeenKey, false)

  }

}
