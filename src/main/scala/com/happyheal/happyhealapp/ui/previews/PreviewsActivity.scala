package com.happyheal.happyhealapp.ui.previews

import android.content.{Intent, Context}
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.{GridLayoutManager, RecyclerView}
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view._
import android.widget.ImageView
import com.github.amlcurran.showcaseview.ShowcaseView.Builder
import com.happyheal.happyhealapp.commons.{ToolbarActionItemTarget, ContextWrapperProvider}
import com.happyheal.happyhealapp.ui.otp.OTPActivity
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.squareup.picasso.Picasso
import macroid.{Ui, ContextWrapper, ActivityContextWrapper, Contexts}
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

    runUi(next <~ On.click {
      Ui {
        val otpIntent = new Intent(getApplicationContext, classOf[OTPActivity])
        startActivity(otpIntent)
      }
    })

    new Builder(this)
      .withMaterialShowcase()
      .setTarget(new ToolbarActionItemTarget(toolBar.get, R.id.plus))
      .setContentText("Add Prescription pictures as many as possible for more clarity using Plus Button")
      .setStyle(R.style.CustomShowcaseTheme2)
      .hideOnTouchOutside()
      .build()

    //    runUi(previews <~
    //      rvAdapter(new PreviewsAdapter(
    //        List(
    //          Preview(Uri.parse("http://fb.com")),
    //          Preview(Uri.parse("http://fb.com"))
    //        )
    //      )) <~
    //      rvAddItemDecoration(new MainItemDecorator()(activityContextWrapper)) <~
    //      rvLayoutManager(new GridLayoutManager(this, 2)))

  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_previews, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.plus =>

        return true
      case _ => return super.onOptionsItemSelected(item)
    }
  }


}

case class Preview(uri: Uri)

class PreviewViewHolder(layout: View) extends ViewHolder(layout) {

  def bind(preview: Preview): Unit = {
    val imageView = layout.findViewById(R.id.image).asInstanceOf[ImageView]
    Picasso
      .`with`(layout.getContext)
      .load(preview.uri)
      .placeholder(R.drawable.camera_icon)
      .error(R.drawable.camera_icon)
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
