package com.happyheal.happyhealapp.ui.wizard

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.happyheal.happyhealapp.{TypedFindView, TR}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.main.MainActivity
import macroid.{ContextWrapper, Contexts}
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 21/01/16.
  */
class WizardActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with ContextWrapperProvider
    with PersistenceServicesComponentImpl
    with TypedFindView
    with WizardComposer {

  override implicit lazy val contextProvider: ContextWrapper = activityContextWrapper

  val LOG_TAG = classOf[WizardActivity].getSimpleName

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    Log.d(LOG_TAG, "WizardActivity")
    val extras = getIntent.getExtras
    val tutorialMode = if (extras != null) extras.getBoolean(WizardActivity.tutorialMode, false) else false
    if (!(tutorialMode || !persistenceServices.isWizardSeen)) {
      finish()
      startActivity(new Intent(getApplication, classOf[MainActivity]))
    }
    setContentView(TR.layout.wizard_layout.id)
    runUi(initializeWizard)
  }

}

object WizardActivity {
  val tutorialMode = "tutorial_mode"
}
