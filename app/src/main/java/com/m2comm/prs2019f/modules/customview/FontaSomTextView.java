package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class FontaSomTextView extends TextView {

    private int x,y,w,h;
    private Globar g;

    public FontaSomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.FontAsomTextView);

        this.w = this.g.w(t.getInt(R.styleable.FontAsomTextView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.FontAsomTextView_h,0));
        int fontSize =(int)this.g.setTextSize(t.getInt(R.styleable.FontAsomTextView_fontSize,12));

        String fontAsomeStr = (String) t.getString(R.styleable.FontAsomTextView_asome);//this.g.setTextSize();
        Typeface fontAwsome;
        if ( fontAsomeStr != null && fontAsomeStr.equals("1") ) {
            fontAwsome = Typeface.createFromAsset(c.getAssets(),"fa_regular_400.ttf");
            this.setTypeface(fontAwsome);
        } else if ( fontAsomeStr != null && fontAsomeStr.equals("2")) {
            fontAwsome = Typeface.createFromAsset(c.getAssets(),"fa_solid_900.ttf");
            this.setTypeface(fontAwsome);
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
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
