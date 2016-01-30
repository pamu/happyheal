package com.happyheal.happyhealapp.ui.otp

import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders
import com.sinch.verification.{VerificationListener, SinchVerification}
import macroid.{Tweak, Contexts, Ui, ActivityContextWrapper}
import macroid.FullDsl._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._

/**
  * Created by pnagarjuna on 24/01/16.
  */
trait OTPComposer {

  self: TypedFindView
    with ContextWrapperProvider
    with VerificationListener
    with Contexts[AppCompatActivity] =>

  lazy val toolBar = Option(findView(TR.toolbar))
  lazy val phoneContainer = Option(findView(TR.phone_container))
  lazy val editText = Option(findView(TR.phone_number))
  lazy val next = Option(findView(TR.next))
  lazy val loaderContainer = Option(findView(TR.loader_container))
  lazy val circularLoader = Option(findView(TR.circular_loader))
  lazy val loadingMessage = Option(findView(TR.loading_message))

  lazy val verificationStatusContainer = Option(findView(TR.verification_status_container))
  lazy val verificationStatusMessage = Option(findView(TR.verification_status_message))
  lazy val tryAgain = Option(findView(TR.try_again))

  var count: Int = 0

  val timer = new CountDownTimer(30000, 3000) {

    override def onFinish(): Unit =
      runUi(verificationFailed("Verification failed, try again")(activityContextWrapper))

    override def onTick(l: Long): Unit = {
      count += 10
      runUi(verifying(Some(count)))
    }

  }

  def verifying(progress: Option[Int] = None)(implicit activityContextWrapper: ActivityContextWrapper) =
    (phoneContainer <~ vGone) ~
      (loaderContainer <~ vVisible) ~
      (loadingMessage <~ tvText(progress.map { v => s"loading ${v} %" }.getOrElse("loading ..."))) ~
      (circularLoader <~ vVisible) ~
      (circularLoader <~ Tweak[CircularFillableLoaders] {
        loader => loader.setProgress(100 - progress.map(v => v).getOrElse(10))
      }) ~
      (next <~ vGone)

  def verificationFailed(msg: String)(implicit activityContextWrapper: ActivityContextWrapper): Ui[_] =
    (phoneContainer <~ vGone) ~
      (next <~ vGone) ~
      (loaderContainer <~ vGone) ~
      (verificationStatusContainer <~ vVisible) ~
      (verificationStatusMessage <~ vVisible) ~
      (verificationStatusMessage <~ tvText(msg)) ~
      (tryAgain <~ vVisible) ~
      (tryAgain <~ On.click {
        Ui {
          init(activityContextWrapper)
        }
      })

  def init(implicit activityContextWrapper: ActivityContextWrapper) =
    (verificationStatusContainer <~ vGone) ~
      (loaderContainer <~ vGone) ~
      (phoneContainer <~ vVisible) ~
      (next <~ vVisible) ~
      (next <~ On.click {
        Ui {
          editText.map { et =>
            val text = et.getText.toString
            runUi(toast(text)(contextProvider) <~ fry)
            runUi(verifying())
            timer.start()
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
    timer.start()
  }

  override def onInitiated(): Unit = {
    runUi(toast("initiated")(contextProvider) <~ fry)
  }

  override def onVerified(): Unit = {
    runUi(toast("verified")(contextProvider) <~ fry)
    runUi(verificationFailed("Initiation Failed")(activityContextWrapper))
    count = 0
  }

  override def onInitiationFailed(e: Exception): Unit = {
    runUi(toast("initiation failed")(contextProvider) <~ fry)
    runUi(verificationFailed("Verification failed, Check your internet.")(activityContextWrapper))
    count = 0
    timer.cancel()
  }

  override def onVerificationFailed(e: Exception): Unit = {
    runUi(toast("verification failed")(contextProvider) <~ fry)
    runUi(verificationFailed("Verification failed, Try again")(activityContextWrapper))
    timer.cancel()
  }

}
