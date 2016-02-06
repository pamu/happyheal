package com.happyheal.happyhealapp.commons

/**
  * Created by pnagarjuna on 06/02/16.
  */
object ValidationRules {

  def isGoodPhone(phone: String): Boolean = phone.matches(s"""^(?:0091|\\+91|0)[7-9][0-9]{9}""")

}
