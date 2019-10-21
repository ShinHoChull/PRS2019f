package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class CustomSpiner extends android.support.v7.widget.AppCompatSpinner {

    private int x,y,w,h;


    private Globar g;

    public CustomSpiner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomEditText);

        this.w = this.g.w(t.getInt(R.styleable.CustomEditText_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomEditText_h,0));

        int fontSize =(int)this.g.setTextSize(t.getInt(R.styleable.CustomEditText_fontSize,12));
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
