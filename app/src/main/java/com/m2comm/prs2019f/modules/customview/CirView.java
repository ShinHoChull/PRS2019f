package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class CirView extends RelativeLayout {

    private int x, y, w, h;
    private Globar g;

    public CirView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    private void initView(Context c, AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CirView);

        this.w = this.g.w(t.getInt(R.styleable.CirView_w, 0));
        this.h = this.g.w(t.getInt(R.styleable.CirView_w, 0));
        int color = t.getColor(R.styleable.CirView_imgDefaultColor, 0);

        GradientDrawable gd1 = new GradientDrawable();
        gd1.setShape(GradientDrawable.RECTANGLE);
        gd1.setColor(color);
        gd1.setStroke(2, Color.TRANSPARENT);
        gd1.setCornerRadius(this.w / 2);
        this.setBackground(gd1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.w > 0)
            widthMeasureSpec = w;
        if (this.h > 0)
            heightMeasureSpec = h;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}

