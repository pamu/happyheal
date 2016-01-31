package com.happyheal.happyhealapp.ui.address

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import macroid.{ContextWrapper, Contexts}

/**
  * Created by pnagarjuna on 31/01/16.
  */
class AddressActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with ContextWrapperProvider
  with TypedFindView
  with PersistenceServicesComponentImpl
    with AddressComposer {

  override lazy implicit val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.address_layout.id)
    toolbar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    getSupportActionBar().setDisplayShowHomeEnabled(true)

  }

}
