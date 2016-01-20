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
      "Title",
      "Description"
    )
  )

}

case class Step(image: Int, title: String, description: String)
