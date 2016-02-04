package com.happyheal.happyhealapp.ui.verification

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{MenuItem, Menu}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.sinch.verification.VerificationListener
import macroid.{ContextWrapper, Contexts}
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 24/01/16.
  */
class VerificationActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with TypedFindView
    with ContextWrapperProvider
    with VerificationListener
    with PersistenceServicesComponentImpl
    with VerificationComposer {

  override lazy implicit val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.verification_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    getSupportActionBar().setDisplayShowHomeEnabled(true)

    runUi(init)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_verification, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.tick =>

        return true
      case _ => return super.onOptionsItemSelected(item)
    }
  }

}