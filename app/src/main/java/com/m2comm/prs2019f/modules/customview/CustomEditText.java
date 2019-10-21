package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class CustomEditText extends AppCompatEditText {

    private int x,y,w,h;
    private Globar g;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomEditText);

        this.w = this.g.w(t.getInt(R.styleable.CustomEditText_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomEditText_h,0));

        int fontSize =(int)this.g.setTextSize(t.getInt(R.styleable.CustomEditText_fontSize,12));

        String fontStyle = t.getString(R.styleable.CustomEditText_fontType) == null ? "":t.getString(R.styleable.CustomEditText_fontType);
        String font = "Roboto-Regular.ttf";

        if (fontStyle.equals("black")) {
            font = "Roboto-Black.ttf";
        } else if (fontStyle.equals("light")) {
            font = "Roboto-Light.ttf";
        } else if (fontStyle.equals("medium")) {
            font = "Roboto-Medium.ttf";
        } else if (fontStyle.equals("condensed")) {
            font = "Roboto-Condensed.ttf";
        }

        if (!fontStyle.equals("")) {
            Typeface fontAwsome = Typeface.createFromAsset(c.getAssets(), font);
            this.setTypeface(fontAwsome);
        }


        this.setTextSize(fontSize);
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
