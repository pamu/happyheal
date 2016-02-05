package com.happyheal.happyhealapp.models

import java.util.Calendar

/**
  * Created by pnagarjuna on 05/02/16.
  */
object Models {

  case class GPSLocation(latitude: Double, longitude: Double)

  case object FirebaseTimestamp

  case class UserInfo(phone: String,
                      name: Option[String] = None,
                      gender: Option[Char] = None,
                      profilePicture: Option[String] = None,
                      dob: Option[Calendar] = None,
                      city: Option[String] = None)

  case class Order(phone: String,
                   name: String,
                   completeAddress: String,
                   city: String,
                   pin: Int,
                   timestamp: FirebaseTimestamp.type,
                   gpsLocation: Option[GPSLocation] = None)

  case class Prescription(phone: String,
                          orderFirebaseId: String,
                          s3link: String,
                          timestamp: FirebaseTimestamp.type)

}
