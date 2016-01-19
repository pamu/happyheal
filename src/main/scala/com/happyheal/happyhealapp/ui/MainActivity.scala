package com.happyheal.happyhealapp.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{MenuItem, Menu}
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.Contexts

/**
  * Created by pnagarjuna on 20/01/16.
  */
class MainActivity
  extends AppCompatActivity
  with Contexts[AppCompatActivity]
  with TypedFindView {

  val LOG_TAG = classOf[MainActivity].getSimpleName

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.layout_main_activity.id)
    val toolbar = findView(TR.toolbar)
    setSupportActionBar(toolbar)
    getSupportActionBar.setHomeButtonEnabled(true)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_settings => return true
      case _ => return super.onOptionsItemSelected(item)
    }
  }
}
