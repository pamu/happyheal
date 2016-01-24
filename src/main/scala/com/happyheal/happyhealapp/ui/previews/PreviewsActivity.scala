package com.happyheal.happyhealapp.ui.previews

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{GridLayoutManager, LinearLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.{LayoutInflater, ViewGroup, View}
import android.widget.ImageView
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.squareup.picasso.Picasso
import macroid.{ContextWrapper, ActivityContextWrapper, Contexts}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import macroid.FullDsl._

/**
  * Created by pnagarjuna on 23/01/16.
  */
class PreviewsActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with ContextWrapperProvider
    with TypedFindView
    with PreviewsComposer {


  override implicit lazy val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.previews_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)

    runUi(previews <~
      rvAdapter(new PreviewsAdapter(List(Preview(Uri.parse("http://fb.com"))))) <~
      rvAddItemDecoration(new MainItemDecorator()(activityContextWrapper)) <~
      rvLayoutManager(new GridLayoutManager(this, 2)))

  }

}

case class Preview(uri: Uri)

class PreviewViewHolder(layout: View) extends ViewHolder(layout) {

  def bind(preview: Preview): Unit = {
    val imageView = layout.findViewById(R.id.image).asInstanceOf[ImageView]
    Picasso
      .`with`(layout.getContext)
      .load(preview.uri)
      .placeholder(R.drawable.ic_launcher)
      .error(R.drawable.ic_launcher)
      .into(imageView)
  }

}

class PreviewsAdapter(previewList: List[Preview])(implicit activityContextWrapper: ActivityContextWrapper)
  extends RecyclerView.Adapter[PreviewViewHolder] {

  override def getItemCount: Int = previewList.length

  override def onBindViewHolder(vh: PreviewViewHolder, i: Int): Unit = vh.bind(previewList(i))

  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): PreviewViewHolder = {
    val inflater = activityContextWrapper.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val view = inflater.inflate(R.layout.preview, viewGroup, false)
    new PreviewViewHolder(view)
  }

}
