package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.GridView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class BottomGridView extends GridView {

    private Globar g;
    private int w,h;

    public BottomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context , attrs);
    }

    private void init(Context context , AttributeSet a) {

        this.g = new Globar(context);
        TypedArray t = context.obtainStyledAttributes(a, R.styleable.CustomGridView);
        this.h = this.g.h(t.getInt(R.styleable.CustomGridView_h,0));

        //하단 카운트
        this.setNumColumns(this.g.titles.length);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0)
            widthMeasureSpec=w;
        if(this.h>0)
            heightMeasureSpec=h;
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }




}
