package com.happyheal.happyhealapp.ui.orders

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.{TypedFindView, TR}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import macroid.{ContextWrapper, Contexts}

/**
  * Created by pnagarjuna on 01/02/16.
  */
class OrdersActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with ContextWrapperProvider
    with TypedFindView
    with OrdersComposer {


  override implicit val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.orders_layout.id)
    toolbar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    getSupportActionBar().setDisplayShowHomeEnabled(true)
  }

}
