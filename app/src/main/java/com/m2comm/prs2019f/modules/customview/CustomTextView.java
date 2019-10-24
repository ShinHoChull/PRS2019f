package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;

public class CustomTextView extends AppCompatTextView {

    private int x,y,w,h;
    private Context a;

    public int deviceW = 0;
    public int deviceH = 0;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.a = c;
        this.init();

        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomTextView);

        this.w = this.w(t.getInt(R.styleable.CustomTextView_w,0));
        this.h = this.h(t.getInt(R.styleable.CustomTextView_h,0));
        int fontSize =(int)this.setTextSize(t.getInt(R.styleable.CustomTextView_fontSize,14));
        boolean isPhoto = (boolean) t.getBoolean(R.styleable.CustomTextView_isPhoto,false);//this.g.setTextSize();
        String fontStyle = t.getString(R.styleable.CustomTextView_fontType) == null ? "":t.getString(R.styleable.CustomTextView_fontType);

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

        if ( isPhoto == true ) {
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        } else {
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
        }

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

    private void init() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.a.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);



        WindowManager windowManager = (WindowManager)this.a.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int statusheight = this.getStatusBarHeight();

        if ( Build.MODEL.equals("SM-G973N") || Build.MODEL.equals("SM-G977N") || Build.MODEL.equals("SM-N971N") ) {
            statusheight = 0;
        }

        this.deviceW = size.x;
        this.deviceH = size.y - statusheight;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = this.a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int w(int x) {
        return (int) (this.deviceW * (x / Globar.default_W));
    }

    public int h(int x) {
        return (int) (this.deviceH * (x / Globar.default_H));
    }

    public double setTextSize(int fontize) {
        double size_formatter = (double) fontize / Globar.default_W;
        double deviceW;
        if (this.deviceW > 720) {
            deviceW = (double) this.deviceW / 3.3;
        } else {
            deviceW = (double) this.deviceW / 2;
        }
        return (int) deviceW * size_formatter;
    }

}
