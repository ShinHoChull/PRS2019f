package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class CustomGridView extends GridView {

    private int x,y,w,h;

    private View view;
    private Context c;
    private Globar g;


    public CustomGridView(@NonNull Context context) {
        super(context);
    }
    public CustomGridView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context,attrs);
    }

    public void init(Context c , AttributeSet a) {
        this.g = new Globar(c);
        this.c = c;

        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomGridView);

        this.w = g.w(t.getInt(R.styleable.CustomGridView_w,0));//t.getInt(R.styleable.CustomImgView_w,0) * width / 720;
        this.h = g.h(t.getInt(R.styleable.CustomGridView_h,0));//t.getInt(R.styleable.CustomImgView_h,0) * height / 1280;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w > 0) {
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
