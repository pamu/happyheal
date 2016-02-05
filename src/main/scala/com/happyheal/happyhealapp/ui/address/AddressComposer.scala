package com.happyheal.happyhealapp.ui.address

import android.support.v7.app.AppCompatActivity
import com.firebase.client.{FirebaseError, Firebase}
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.{TR, TypedFindView}
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import macroid.{Ui, Contexts}
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
  lazy val next = Option(findView(TR.next))

  def init() = {
    (next <~ On.click {
      Ui {
        val phone = persistenceServices.getPhone("0")
        val fref = new Firebase("https://brilliant-heat-4158.firebaseio.com/")
        fref.push().child(phone).child("order").setValue("address", new Firebase.CompletionListener() {
          override def onComplete(firebaseError: FirebaseError, firebase: Firebase): Unit = {
            runUi(toast("done") <~ fry)
          }
        })
      }
    })
  }
}
