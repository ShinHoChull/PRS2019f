package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class CustomFrameLayout extends FrameLayout {

    private int x,y,w,h;
    private Globar g;

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomFrameLayout);

        this.w = this.g.w(t.getInt(R.styleable.CustomFrameLayout_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomFrameLayout_h,0));
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
