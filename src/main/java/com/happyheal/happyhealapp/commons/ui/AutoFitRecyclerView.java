package com.happyheal.happyhealapp.commons.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by pnagarjuna on 28/01/16.
 */
public class AutoFitRecyclerView extends RecyclerView {
    int columnWidth;
    GridLayoutManager manager;

    public AutofitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            // Read android:columnWidth from xml
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

        manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            manager.setSpanCount(spanCount);
        }
    }
}
