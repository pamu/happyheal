package com.happyheal.happyhealapp.modules.persistence.impl

import android.content.{Context, SharedPreferences}
import android.preference.PreferenceManager
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.{PersistenceServices, PersistenceServicesComponent}

/**
  * Created by pnagarjuna on 21/01/16.
  */
trait PersistenceServicesComponentImpl
  extends PersistenceServicesComponent {

  self: ContextWrapperProvider =>

  val wizardWasSeenKey = "wizard_was_seen_key"
  val userPhone = "user_phone"
  val demoDone = "demo_done"


  override lazy val persistenceServices = new PersistenceServicesImpl

  class PersistenceServicesImpl extends PersistenceServices {

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(contextProvider.application)

    override def setWizardSeen(seen: Boolean): Unit = sharedPreferences.edit().putBoolean(wizardWasSeenKey, seen).commit()

    override def isWizardSeen: Boolean = sharedPreferences.getBoolean(wizardWasSeenKey, false)

    override def setPhone(phone: String): Unit = sharedPreferences.edit().putString(userPhone, phone).commit()

    override def getPhone(defaultValue: String): String = sharedPreferences.getString(userPhone, defaultValue)

    override def isDemoDone(): Boolean = sharedPreferences.getBoolean(demoDone, false)

    override def setDemoDone(done: Boolean): Unit = sharedPreferences.edit().putBoolean(demoDone, done).commit()
  }

}
