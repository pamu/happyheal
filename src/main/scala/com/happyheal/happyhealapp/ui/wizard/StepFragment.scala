package com.happyheal.happyhealapp.ui.wizard

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View.OnClickListener
import android.view.{View, ViewGroup, LayoutInflater}
import android.widget.{Button, TextView, ImageView}
import com.happyheal.happyhealapp.R
import com.happyheal.happyhealapp.ui.main.MainActivity
import macroid.Contexts

/**
  * Created by pnagarjuna on 21/01/16.
  */
class StepFragment extends Fragment
  with Contexts[Fragment] {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val steps = Steps.steps
    val element = getArguments.getInt(StepFragment.keyStepPosition)
    val step = steps(element)
    implicit val rootView = inflater.inflate(R.layout.step_layout, container, false)
    initView(step)
    rootView
  }

  private def initView(step: Step)(implicit view: View): Unit = {
    val imageView = view.findViewById(R.id.image).asInstanceOf[ImageView]
    val titleView = view.findViewById(R.id.title).asInstanceOf[TextView]
    val descriptionView = view.findViewById(R.id.description).asInstanceOf[TextView]
    titleView.setText(step.title)
    descriptionView.setText(step.description)
    imageView.setImageResource(step.image)
  }

}

object StepFragment {

  val keyStepPosition = "key_step_position"

}