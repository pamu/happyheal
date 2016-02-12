package com.happyheal.happyhealapp.ui.address

import java.io.{File, FilenameFilter}
import java.util

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Spinner
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.event.ProgressEvent
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{CannedAccessControlList, PutObjectRequest}
import com.firebase.client.Firebase.CompletionListener
import com.firebase.client.{ServerValue, FirebaseError, Firebase}
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.main.{MainActivity, ImageCapture, CitySpinner}
import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import macroid.{Tweak, Ui, Contexts}
import macroid.FullDsl._
import org.apache.commons.io.{FileUtils, FilenameUtils}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by pnagarjuna on 31/01/16.
  */
trait AddressComposer {
  self: TypedFindView with ContextWrapperProvider with Contexts[AppCompatActivity] with PersistenceServicesComponentImpl =>

  lazy val toolbar = Option(findView(TR.toolbar))
  lazy val name = Option(findView(TR.name))
  lazy val completeAddress = Option(findView(TR.complete_address))
  lazy val pin = Option(findView(TR.pin))
  lazy val spinner = Option(findView(TR.cities))

  def addPlaces(places: List[String]) = {
    spinner <~ Tweak[Spinner] { citySpinner =>
      citySpinner.setAdapter(new CitySpinner(places))
    }
  }

  def getName: Option[String] = name.flatMap { editText =>
    val name = editText.getText.toString
    if (!TextUtils.isEmpty(name)) Some(name) else None
  }

  def getCompleteAddress: Option[String] = completeAddress.flatMap { editText =>
    val completeAddress = editText.getText.toString
    if (!TextUtils.isEmpty(completeAddress)) Some(completeAddress) else None
  }

  def getPin: Option[Int] = pin.flatMap { editText =>
    val pin = Try(editText.getText.toString.toInt) match {
      case Success(pin) => Some(pin)
      case Failure(th) => None
    }
    pin
  }

  def upload(file: File): Future[String] = {
    val p = Promise[String]()

    val credentialsProvider = new CognitoCachingCredentialsProvider(
      activityContextWrapper.application,
      "us-east-1:c2432cb5-55e1-42ec-a12f-4be2bc8abd9a",
      Regions.US_EAST_1
    )

    val transferManager = new TransferManager(credentialsProvider)

    val putObjectRequest = new PutObjectRequest("car-classifieds", file.getName(), file)
      .withCannedAcl(CannedAccessControlList.PublicRead)

    val upload = transferManager.upload(putObjectRequest)

    upload.addProgressListener(new com.amazonaws.event.ProgressListener {
      override def progressChanged(progressEvent: ProgressEvent): Unit = progressEvent.getEventCode match {
        case ProgressEvent.COMPLETED_EVENT_CODE =>
          Try (FileUtils.forceDelete(file))
          p.success("http://s3-ap-southeast-1.amazonaws.com/car-classifieds/" + file.getName)
        case ProgressEvent.FAILED_EVENT_CODE => p.failure(new Exception("something"))
      }
    })

    p.future
  }

  def process: Future[List[String]] = {
    val files = ImageCapture.imagesFolder.listFiles(new FilenameFilter {
      override def accept(file: File, s: String): Boolean = FilenameUtils.getExtension(s) == "jpeg"
    })
    if (files != null) {
      val list = files.toList
      Future.sequence(list.map(upload(_).recover { case th => "" }))
    } else Future.successful(List[String]())
  }

  def onSubmit(): Unit = {
    val pDialog = new ProgressDialog(activityContextWrapper.getOriginal)
    pDialog.setIndeterminate(true)
    pDialog.setTitle("Placing order")
    pDialog.setMessage("Please wait ... ")
    runUi {
      Ui {
        if (getName.isDefined) {
          val name = getName
          if (getCompleteAddress.isDefined) {
            val completeAddress = getCompleteAddress
            if (getPin.isDefined) {
              val pin = getPin
              val place = spinner.map(_.getSelectedItem.toString)
              pDialog.show()
              val phone = persistenceServices.getPhone("0")
              val fref = new Firebase("https://brilliant-heat-4158.firebaseio.com/")
              import scala.collection.JavaConversions._
              process onComplete {
                case Success(c) =>

                  Log.d("place order", c.mkString(","))

                  val map = Map(phone -> mapAsJavaMap(Map[String, AnyRef](
                    "name" -> name.get,
                    "complete_address" -> completeAddress.get,
                    "pin" -> pin.get.toString,
                    "place" -> place.get,
                    "images" -> c.mkString(","),
                    "timestamp" -> ServerValue.TIMESTAMP
                  )))

                  Log.d("place order", map.mkString(","))

                  fref.push().setValue(mapAsJavaMap[String, util.Map[String, AnyRef]](map), new CompletionListener {
                    override def onComplete(firebaseError: FirebaseError, firebase: Firebase): Unit = {
                      runUi(toast("done, order placed") <~ fry)
                      pDialog.dismiss()
                      activityContextWrapper.getOriginal.finish()
                      activityContextWrapper.getOriginal.startActivity(new Intent(activityContextWrapper.application, classOf[MainActivity]))
                    }
                  })
                case Failure(th) =>
                  Log.d("place order", th.getMessage)
                  th.printStackTrace()
                  runUi(toast("no prescriptions") <~ fry)
                  pDialog.dismiss()
              }

            } else {
              runUi(toast("Enter pin") <~ fry)
            }
          } else {
            runUi(toast("Enter complete address") <~ fry)
          }
        } else {
          runUi(toast("Enter name") <~ fry)
        }
      }
    }
  }
}
