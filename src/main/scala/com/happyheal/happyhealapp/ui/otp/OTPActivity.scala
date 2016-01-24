package com.happyheal.happyhealapp.ui.otp

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{MenuItem, Menu}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.smsservices.impl.SMSServicesComponentImpl
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import macroid.{ContextWrapper, Contexts}

import scala.util.{Failure, Success}

/**
  * Created by pnagarjuna on 24/01/16.
  */
class OTPActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with TypedFindView
    with ContextWrapperProvider
    with OTPComposer
    with SMSServicesComponentImpl {

  override lazy implicit val contextProvider: ContextWrapper = activityContextWrapper

  private var mDialog: ProgressDialog = _

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.otp_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    getSupportActionBar().setDisplayShowHomeEnabled(true)
    mDialog = new ProgressDialog(this)
    mDialog.setTitle("Phone Verification")
    mDialog.setMessage("verifying ... Please wait ...")
    mDialog.setIndeterminate(true)
    mDialog.show()
    import scala.concurrent.ExecutionContext.Implicits.global
    smsServices.verify("+919676905479") onComplete {
      case Success(value) =>
        mDialog.dismiss()
      case Failure(th) =>
        mDialog.dismiss()
    }
  }

  override def onResume(): Unit = {
    super.onResume()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_otp, menu)
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
