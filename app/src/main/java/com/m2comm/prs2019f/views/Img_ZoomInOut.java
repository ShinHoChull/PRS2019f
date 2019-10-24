package com.m2comm.prs2019f.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.model.MainGetPhotoDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class Img_ZoomInOut extends AppCompatActivity implements View.OnClickListener {

    private ImageView img;
    private ArrayList<MainGetPhotoDTO> photoList;
    private int choiceNum;
    private String url;

    //next & back button & close Bt
    private ImageView nBt, bBt, cBt;

    //private count Text
    private TextView countTv , photoHeart , photoSaveBt , photoDel , photoLikeText ;
    // circle progress Bar
    CircleProgressView mCircleView;

    private LinearLayout bottomV , detail_delete , heartV , saveV;

    private Globar g;
    private Context c;

    private Custom_SharedPreferences csp;

    //backView
    //private LinearLayout backViewTop, backViewBottom;


    private void viewReset() {
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);
        this.c = this;
        this.photoList = new ArrayList<>();

        this.photoHeart = findViewById(R.id.photo_detail_heart);
        this.photoDel = findViewById(R.id.photo_detail_trash);
        this.photoSaveBt = findViewById(R.id.photo_detail_download);
        this.bottomV = findViewById(R.id.photo_detail_bottomV);
        this.photoLikeText = findViewById(R.id.photo_detail_heart_text);
        this.detail_delete = findViewById(R.id.detail_delete);
        this.heartV = findViewById(R.id.heartV);
        this.saveV = findViewById(R.id.saveV);

        Typeface fontAwsome = Typeface.createFromAsset(this.c.getAssets(),"fa_solid_900.ttf");


        photoDel.setTypeface(fontAwsome);
        photoDel.setTextColor(this.c.getResources().getColor(R.color.main_color_black));

        photoSaveBt.setTypeface(fontAwsome);
        photoSaveBt.setTextColor(this.c.getResources().getColor(R.color.main_color_black));


        this.img = findViewById(R.id.Zoom_img);
        this.nBt = findViewById(R.id.photo_detail_next);
        this.bBt = findViewById(R.id.photo_detail_back);
        this.cBt = findViewById(R.id.photo_detail_close);
        this.countTv = findViewById(R.id.photo_detail_text);

        this.mCircleView = findViewById(R.id.circleView);
        this.mCircleView.setSpinningBarLength(180);
        this.mCircleView.setShowTextWhileSpinning(true);
        this.mCircleView.setText("");
        this.mCircleView.setTextMode(TextMode.TEXT);
        this.mCircleView.setUnitVisible(false);
    }

    private void listenerRegister() {
        this.nBt.setOnClickListener(this);
        this.bBt.setOnClickListener(this);
        this.cBt.setOnClickListener(this);
        this.photoSaveBt.setOnClickListener(this);
        this.photoHeart.setOnClickListener(this);
        this.detail_delete.setOnClickListener(this);
        this.heartV.setOnClickListener(this);
        this.saveV.setOnClickListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ( newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ) {
            //세로
            this.bottomV.setVisibility(View.VISIBLE);
        } else if ( newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            //가로.
            this.bottomV.setVisibility(View.INVISIBLE);
        }
    }

    private void getPhoto() {
        this.heartReset();

        mCircleView.setVisibility(View.VISIBLE);
        this.mCircleView.spin();
        Picasso.get().load(this.g.mainUrl+"/voting/upload/photo/"+this.url).resize(this.g.w(360), 0).error(R.mipmap.ic_launcher).into(img, new Callback() {
            @Override
            public void onSuccess() {
                mCircleView.stopSpinning();
                mCircleView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        this.changeText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_zoom_in_out);
        this.viewReset();
        this.listenerRegister();
        Intent intent = new Intent(this.getIntent());
        this.url = intent.getStringExtra("nowUrl");
        this.choiceNum = intent.getIntExtra("choiceNum", 0);
        this.photoList = (ArrayList<MainGetPhotoDTO>) intent.getSerializableExtra("array");
        this.getPhoto();

        this.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                photoSave();
                return false;
            }
        });
    }

    private void heartReset() {
        MainGetPhotoDTO r = this.photoList.get(this.choiceNum);
        if (r.getCnt() > 0) {
            Typeface fontAwsome = Typeface.createFromAsset(this.c.getAssets(),"fa_solid_900.ttf");
            this.photoHeart.setTypeface(fontAwsome);
            this.photoHeart.setTextColor(this.c.getResources().getColor(R.color.main_color_RED));
        } else {
            Typeface fontAwsome = Typeface.createFromAsset(this.c.getAssets(),"fa_regular_400.ttf");
            this.photoHeart.setTypeface(fontAwsome);
            this.photoHeart.setTextColor(this.c.getResources().getColor(R.color.main_color_gray));
        }

        this.photoLikeText.setText(String.valueOf(r.getCnt())+" Like");

        if(csp.getValue("deviceid","").equals(r.getDeviceid())) {
            this.detail_delete.setVisibility(View.VISIBLE);
        }
    }

    private void photoSave () {
        new MaterialDialog.Builder(c).title("Image")
                .content("Do you want to download the image?")
                .positiveText("OK").negativeText("Cancel").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                FileOutputStream out = null;
                try {
                    Date d = new Date();
                    out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/"+String.valueOf(d.getTime())+".png");
                    BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                    String.valueOf(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "descripton"));
                    g.msg("complete.");
                } catch (FileNotFoundException e) {
                    g.msg("Save image is fail. Retry after few minite.");
                    e.printStackTrace();
                }
            }
        }).show();
    }

    private void nextPhoto() {
        this.choiceNum = this.choiceNum + 1;
        if (this.choiceNum >= this.photoList.size()) {
            this.choiceNum = 0;
        }
        this.url = this.photoList.get(this.choiceNum).getUrl();
        this.getPhoto();
    }

    private void backPhoto() {
        this.choiceNum = this.choiceNum - 1;
        if (this.choiceNum < 0) {
            this.choiceNum = this.photoList.size() - 1;
        }
        this.url = this.photoList.get(this.choiceNum).getUrl();
        this.getPhoto();
    }

    private void changeText() {
        this.countTv.setText((this.choiceNum + 1) + "/" + this.photoList.size());
    }

    @Override
    public void onClick(View v) {
        final MainGetPhotoDTO r = this.photoList.get(this.choiceNum);
        switch (v.getId()) {
            case R.id.detail_delete :

                new MaterialDialog.Builder(this).title("Alert")
                        .content("Are you sure you want to delete this photo?")
                        .positiveText("OK").negativeText("Cancle").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        AndroidNetworking.get(g.baseUrl+g.urls.get("photoDel"))
                                .addQueryParameter("image",r.getUrl())
                                .addQueryParameter("deviceid", csp.getValue("deviceid",""))
                                .setPriority(Priority.LOW)
                                .build().getAsString(new StringRequestListener() {

                            @Override
                            public void onResponse(String response) {
                                g.msg("Delete completed.");
                                finish();
                            }

                            @Override
                            public void onError(ANError anError) {
                                g.baseAlertMessage("Error",anError.toString());
                            }
                        });
                    }
                }).show();


                break;

            case R.id.photo_detail_next:
                this.nextPhoto();
                break;
            case R.id.photo_detail_back:
                this.backPhoto();
                break;
            case R.id.photo_detail_close:
                finish();
                break;
            case R.id.saveV:
            case R.id.photo_detail_download:
                this.photoSave();
                break;
            case R.id.heartV:
            case R.id.photo_detail_heart:
                int tempLike = 1;
                if (Integer.valueOf(r.getMyfav()) > 0 ) {
                    r.setMyfav("0");
                    tempLike = 0;
                } else {
                    r.setMyfav("1");
                }

                AndroidNetworking.get(this.g.baseUrl+this.g.urls.get("photoLike"))
                        .addQueryParameter("sid",String.valueOf(r.getSid()))
                        .addQueryParameter("code", this.g.code)
                        .addQueryParameter("deviceid", csp.getValue("deviceid",""))
                        .addQueryParameter("val", String.valueOf(tempLike))
                        .setPriority(Priority.LOW)
                        .build().getAsString(new StringRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("likelike2",response);
                        r.setCnt(Integer.valueOf(response));
                        getPhoto();
                    }

                    @Override
                    public void onError(ANError anError) {
                        g.baseAlertMessage("Error",anError.toString());
                    }
                });
                break;
        }
    }

}
