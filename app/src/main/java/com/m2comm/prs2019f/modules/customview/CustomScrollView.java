package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class CustomScrollView extends ScrollView {

    private int w,h;
    private Globar g;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context,attrs);
    }

    private void init(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomScrollView);

        this.w = this.g.w(t.getInt(R.styleable.CustomScrollView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomScrollView_h,0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0) {
            widthMeasureSpec = this.w;
            getLayoutParams().width = this.w;
        }

        if(this.h > 0) {
            heightMeasureSpec = this.h;
            getLayoutParams().height = this.h;
        }
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

    }


}
