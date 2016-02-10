package com.happyheal.happyhealapp.ui.address

import android.support.v7.app.AppCompatActivity
import android.widget.Spinner
import com.firebase.client.{FirebaseError, Firebase}
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.main.CitySpinner
import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import macroid.{Tweak, Ui, Contexts}
import macroid.FullDsl._

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

  def onSubmit(): Unit = {
    runUi {
      Ui {
        val phone = persistenceServices.getPhone("0")
        val fref = new Firebase("https://brilliant-heat-4158.firebaseio.com/")
        fref.push().child(phone).child("order").setValue("address", new Firebase.CompletionListener() {
          override def onComplete(firebaseError: FirebaseError, firebase: Firebase): Unit = {
            runUi(toast("done") <~ fry)
          }
        })
      }
    }
  }
}
