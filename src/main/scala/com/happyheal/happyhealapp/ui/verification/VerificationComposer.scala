package com.happyheal.happyhealapp.ui.verification

import android.content.Intent
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.address.AddressActivity
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
trait VerificationComposer {

  self: TypedFindView
    with ContextWrapperProvider
    with VerificationListener
    with Contexts[AppCompatActivity]
    with AppCompatActivity
    with PersistenceServicesComponentImpl =>

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

  var phoneOption: Option[String] = None
  var count: Int = 0
  val timer = new CountDownTimer(30000, 1000) {

    override def onFinish(): Unit =
      runUi(verificationFailed("Verification failed, try again"))

    override def onTick(l: Long): Unit = {
      count += 2
      runUi(verifying(Some(count)))
    }

  }

  def verified(implicit activityContextWrapper: ActivityContextWrapper) =
    (phoneContainer <~ vGone) ~
      (next <~ vGone) ~
      (loaderContainer <~ vGone) ~
      (verificationStatusContainer <~ vVisible) ~
      (tryAgain <~ vGone) ~
      (verificationStatusMessage <~ tvText("Verified :)"))


  def verifying(progress: Option[Int] = None)(implicit activityContextWrapper: ActivityContextWrapper) =
    (phoneContainer <~ vGone) ~
      (loaderContainer <~ vVisible) ~
      (loadingMessage <~ tvText(progress.map { v => s"verifying ${v} %" }.getOrElse("verifying ..."))) ~
      (circularLoader <~ vVisible) ~
      (circularLoader <~ Tweak[CircularFillableLoaders] {
        loader => loader.setProgress(100 - progress.map(v => v).getOrElse(0))
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
          runUi(init)
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
            if (!TextUtils.isEmpty(text)) {
              runUi(toast(text)(contextProvider) <~ fry)
              runUi(verifying())
              timer.start()
              startVerification("+91" + text.trim)
            }
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

    phoneOption = Some(phone)
    verification.initiate()
    timer.start()
  }

  override def onInitiated(): Unit = {
    runUi(toast("initiated")(contextProvider) <~ fry)
  }

  override def onVerified(): Unit = {
    runUi(toast("verified")(contextProvider) <~ fry)
    timer.cancel()
    runUi(verified)
    phoneOption.map(persistenceServices.setPhone(_))
    val addressIntent = new Intent(activityContextWrapper.getOriginal, classOf[AddressActivity])
    finish()
    startActivity(addressIntent)
  }

  override def onInitiationFailed(e: Exception): Unit = {
    runUi(toast("initiation failed")(contextProvider) <~ fry)
    runUi(verificationFailed("Verification failed, Check your internet."))
    count = 0
    timer.cancel()
  }

  override def onVerificationFailed(e: Exception): Unit = {
    runUi(toast("verification failed")(contextProvider) <~ fry)
    runUi(verificationFailed("Verification failed, Try again"))
    count = 0
    timer.cancel()
  }

}
