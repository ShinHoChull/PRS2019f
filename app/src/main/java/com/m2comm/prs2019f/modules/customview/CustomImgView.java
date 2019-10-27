package com.m2comm.prs2019f.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class CustomImgView extends android.support.v7.widget.AppCompatImageView {

    private int x,y,w,h;
    private WindowManager wm;

    public int deviceW = 0;
    public int deviceH = 0;
    private Context a;

    public CustomImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.a = c;
        wm = (WindowManager) c.getSystemService(c.WINDOW_SERVICE);
        this.deviceW = this.getDeviceW();
        this.deviceH = this.getDeviceH();
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomImgView);

        int color = t.getColor(R.styleable.CustomImgView_imgDefaultColor, 0);
        this.w = this.w(t.getInt(R.styleable.CustomImgView_w,0));
        this.h = this.h(t.getInt(R.styleable.CustomImgView_h,0));

        int width = w(t.getInt(R.styleable.CustomImgView_imgW,0));
        int height = h(t.getInt(R.styleable.CustomImgView_imgH,0));

        if ( width > 0 && height > 0) {
            Bitmap b = BitmapFactory.decodeResource(getResources(),t.getResourceId(R.styleable.CustomImgView_img,R.mipmap.ic_launcher));
            b = Bitmap.createScaledBitmap(b,width,height,true);
            this.setImageBitmap(b);
            this.setScaleType(ScaleType.CENTER_INSIDE);
        } else {
            this.setImageResource(t.getResourceId(R.styleable.CustomImgView_img,R.mipmap.ic_launcher));
        }

        if(color != 0) {
            this.setColorFilter(color);
        }

        boolean touch = t.getBoolean(R.styleable.CustomImgView_touch,false);
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

    private DisplayMetrics dM() {
        DisplayMetrics dm = new DisplayMetrics();
        this.wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    private int getDeviceW() {
        return this.dM().widthPixels;
    }

    private int getDeviceH() {
        return this.dM().heightPixels - this.getStatusBarHeight();
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


}
