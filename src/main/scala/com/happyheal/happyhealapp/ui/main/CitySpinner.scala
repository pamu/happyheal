package com.happyheal.happyhealapp.ui.main

import android.view.{LayoutInflater, ViewGroup, View}
import android.widget.{ImageView, TextView, SpinnerAdapter, BaseAdapter}
import com.happyheal.happyhealapp.R
import macroid.ActivityContextWrapper

/**
  * Created by pnagarjuna on 07/02/16.
  */
class CitySpinner(cities: List[String])(implicit activityContextWrapper: ActivityContextWrapper)
  extends BaseAdapter with SpinnerAdapter {

  override def getItemId(i: Int): Long = i

  override def getCount: Int = cities.length

  override def getView(i: Int, view: View, viewGroup: ViewGroup): View = {
    val rootView = LayoutInflater.from(activityContextWrapper.application).inflate(R.layout.spinner_item, viewGroup, false)
    val textView = rootView.findViewById(R.id.spinner_item_text).asInstanceOf[TextView]
    val chevron = rootView.findViewById(R.id.chevron).asInstanceOf[ImageView]
    chevron.setColorFilter(activityContextWrapper.application.getResources.getColor(R.color.colorPrimary))
    textView.setText(cities(i).toString)
    rootView
  }

  override def getDropDownView(position: Int, convertView: View, parent: ViewGroup): View = {
    val rootView = LayoutInflater.from(activityContextWrapper.application).inflate(R.layout.spinner_dropdown, parent, false)
    val textView = rootView.findViewById(R.id.city_dropdown_text).asInstanceOf[TextView]
    textView.setText(cities(position).toString)
    rootView
  }

  override def getItem(i: Int): AnyRef = cities(i)

}
