package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class VotingButton extends android.support.v7.widget.AppCompatButton {

    private int w,h;

    private View view;
    private Context c;
    private Globar g;


    public VotingButton(@NonNull Context context) {
        super(context);
    }

    public VotingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context,attrs);
    }

    public void init(Context c , AttributeSet a) {
        this.g = new Globar(c);
        this.c = c;

        TypedArray t = c.obtainStyledAttributes(a, R.styleable.VotingButton);
        this.w = g.w(t.getInt(R.styleable.VotingButton_w,0));
        this.h = g.h(t.getInt(R.styleable.VotingButton_h,0));//t.getInt(R.styleable.CustomImgView_h,0) * height / 1280;
    }

    @Override
    public boolean performClick() {
        return true;
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
