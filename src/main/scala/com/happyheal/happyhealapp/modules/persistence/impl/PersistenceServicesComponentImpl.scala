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


  override lazy val persistenceServices = new PersistenceServicesImpl

  class PersistenceServicesImpl extends PersistenceServices {

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(contextProvider.application)

    override def setWizardSeen(seen: Boolean): Unit = sharedPreferences.edit().putBoolean(wizardWasSeenKey, seen).commit()

    override def isWizardSeen: Boolean = sharedPreferences.getBoolean(wizardWasSeenKey, false)

    override def setPhone(phone: Long): Unit = sharedPreferences.edit().putLong(userPhone, phone).commit()

    override def getPhone(defaultValue: Long): Long = sharedPreferences.getLong(userPhone, defaultValue)
  }

}
