package com.happyheal.happyhealapp.ui.previews

import java.io.{FilenameFilter, File}

import android.app.Activity
import android.content.{Intent, Context}
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view._
import android.widget.ImageView
import com.happyheal.happyhealapp.commons.utils.Utils
import org.apache.commons.io.{FileUtils, FilenameUtils}

import scala.concurrent.Future
import scala.util.{Failure, Success}

//import com.github.amlcurran.showcaseview.ShowcaseView.Builder
import com.happyheal.happyhealapp.commons.ContextWrapperProvider
import com.happyheal.happyhealapp.modules.persistence.impl.PersistenceServicesComponentImpl
import com.happyheal.happyhealapp.ui.main.ImageCapture
import com.happyheal.happyhealapp.{R, TR, TypedFindView}
import com.squareup.picasso.Picasso
import macroid._
import macroid.FullDsl._

//import com.happyheal.happyhealapp.commons.ToolbarActionItemTarget

import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by pnagarjuna on 23/01/16.
  */
class PreviewsActivity
  extends AppCompatActivity
    with Contexts[AppCompatActivity]
    with ContextWrapperProvider
    with TypedFindView
    with PreviewsComposer
    with PersistenceServicesComponentImpl {


  override implicit lazy val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(TR.layout.previews_layout.id)
    toolBar map setSupportActionBar
    getSupportActionBar.setHomeButtonEnabled(true)
    getSupportActionBar().setDisplayHomeAsUpEnabled(true)
    getSupportActionBar().setDisplayShowHomeEnabled(true)

    //runUi(empty)

    //    new Builder(this)
    //      .withMaterialShowcase()
    //      .setTarget(new ToolbarActionItemTarget(toolBar.get, R.id.plus))
    //      .setContentText("Add Prescription pictures as many as possible for more clarity using Plus Button")
    //      .setStyle(R.style.CustomShowcaseTheme2)
    //      .hideOnTouchOutside()
    //      .build()

    reload

  }

  override def onResume(): Unit = {
    super.onResume()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_previews, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.plus =>
        ImageCapture.showDialog(activityContextWrapper)
        return true
      case _ => return super.onOptionsItemSelected(item)
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    if (requestCode == ImageCapture.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
      reload
    } else if (requestCode == ImageCapture.REQUEST_OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
      val uri = data.getData
      runUi(toast("Please wait ...")(activityContextWrapper) <~ fry)
      Future {
        ImageCapture.copyFile(new File(Utils.getRealPathFromURI(getApplication, uri)), ImageCapture.randomFile("jpeg"))
      } mapUi { x =>
        Ui {
          reload
        }
      }
    }
  }

}

case class Preview(file: File)

class PreviewViewHolder(layout: View)
                       (clickListener: => Unit)
                       (implicit activityContextWrapper: ActivityContextWrapper)
  extends ViewHolder(layout) {

  def bind(preview: Preview): Unit = {
    val imageView = layout.findViewById(R.id.image).asInstanceOf[ImageView]
    val cross = layout.findViewById(R.id.cross).asInstanceOf[ImageView]

    runUi {
      cross <~ On.click {
        Ui {
          runUi(toast("Please wait ... ")(activityContextWrapper) <~ fry)
          Future {
            FileUtils.forceDelete(preview.file)
          } mapUi { value =>
            clickListener
            toast("Removed") <~ fry
          } recoverUi {
            case ex =>
              toast("Unable to remove") <~ fry
          }
        }
      }
    }

    Picasso
      .`with`(activityContextWrapper.getOriginal)
      .load(preview.file)
      .fit()
      .centerCrop()
      .placeholder(R.drawable.camera_icon)
      .error(R.drawable.camera_icon)
      .into(imageView)

  }

}

class PreviewsAdapter(previewList: List[Preview])
                     (clickListener: => Unit)
                     (implicit activityContextWrapper: ActivityContextWrapper)
  extends RecyclerView.Adapter[PreviewViewHolder] {

  override def getItemCount: Int = previewList.length

  override def onBindViewHolder(vh: PreviewViewHolder, i: Int): Unit = vh.bind(previewList(i))


  override def onCreateViewHolder(viewGroup: ViewGroup, i: Int): PreviewViewHolder = {
    val inflater = activityContextWrapper.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val view = inflater.inflate(R.layout.preview, viewGroup, false)
    new PreviewViewHolder(view)(clickListener)
  }

}
