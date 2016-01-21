package com.happyheal.happyhealapp.ui.wizard

import android.os.Bundle
import android.support.v4.app.{FragmentManager, Fragment, FragmentPagerAdapter}
import com.happyheal.happyhealapp.R
import macroid.ContextWrapper

/**
  * Created by pnagarjuna on 21/01/16.
  */
class StepsPagerAdapter(fragmentManager: FragmentManager)(implicit contextWrapper: ContextWrapper)
  extends FragmentPagerAdapter(fragmentManager) {

  val steps = Steps.steps

  override def getItem(i: Int): Fragment = {
    val fragment = new StepFragment
    val bundle = new Bundle()
    bundle.putInt(StepFragment.keyStepPosition, i)
    fragment.setArguments(bundle)
    fragment
  }

  override def getCount: Int = steps.length

}

object Steps {

  def steps(implicit contextWrapper: ContextWrapper): List[Step] = List(
    Step(
      R.drawable.ic_launcher,
      "Scala programming language is functional and object oriented",
      "Scala is both functional and object oriented language. Scala has rich language features like pattern matching, mixin composition using traits, java interoperability and tools for concurrency and parallel computing."
    ),
    Step(
      R.drawable.ic_launcher,
      "Java",
      "Java is object oriented language"
    ),
    Step(
      R.drawable.ic_launcher,
      "Clojure",
      "Clojure is functional language with lisp dialect"
    )
  )

}

case class Step(image: Int, title: String, description: String)
