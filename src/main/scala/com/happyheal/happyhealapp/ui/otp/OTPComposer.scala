package com.happyheal.happyhealapp.ui.otp

import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.sinch.verification.{VerificationListener, SinchVerification}
import macroid.{Ui, ActivityContextWrapper}
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait OTPComposer {

  self: TypedFindView with ContextWrapperProvider with VerificationListener =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val editText = Option(findView(TR.phone_number))
  lazy val next = Option(findView(TR.next))

  def init(implicit activityContextWrapper: ActivityContextWrapper) =
    (next <~ On.click {
      Ui {
        editText.map { et =>
          val text = et.getText.toString
          runUi(toast(text)(contextProvider) <~ fry)
          startVerification("+91" + text.trim)
        }
      }
    })

  def startVerification(phone: String)(implicit activityContextWrapper: ActivityContextWrapper): Unit = {
    val config = SinchVerification.config()
      .applicationKey(activityContextWrapper.application.getString(R.string.sinch_verification_key))
      .context(activityContextWrapper.application)
      .build()

    val verification = SinchVerification.createFlashCallVerification(
      config,
      phone,
      self
    )

    verification.initiate()
  }

  override def onInitiated(): Unit = {
    runUi(toast("initiated")(contextProvider) <~ fry)
  }

  override def onVerified(): Unit = {
    runUi(toast("verified")(contextProvider) <~ fry)
  }

  override def onInitiationFailed(e: Exception): Unit = {
    runUi(toast("initiation failed")(contextProvider) <~ fry)
  }

  override def onVerificationFailed(e: Exception): Unit = {
    runUi(toast("verification failed")(contextProvider) <~ fry)
  }

}
