package com.happyheal.happyhealapp.ui.previews

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.State
import android.view.View
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.happyheal.happyhealapp.R
import macroid.ContextWrapper

import scala.language.postfixOps

class MainItemDecorator(implicit context: ContextWrapper)
  extends RecyclerView.ItemDecoration {

  override def getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State): Unit = {
    outRect.top = resGetDimensionPixelSize(R.dimen.padding_default)
    outRect.bottom = resGetDimensionPixelSize(R.dimen.padding_default)
    outRect.left = resGetDimensionPixelSize(R.dimen.padding_default)
    outRect.right = resGetDimensionPixelSize(R.dimen.padding_default)
  }

}
