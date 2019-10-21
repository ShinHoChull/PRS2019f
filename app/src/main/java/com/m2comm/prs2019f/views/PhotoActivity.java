package com.m2comm.prs2019f.views;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityPhotoBinding;
import com.m2comm.prs2019f.model.MainGetPhotoDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.viewmodels.PhotoViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityPhotoBinding binding;

    GridLayout gridLayout;
    private ArrayList<MainGetPhotoDTO> urlList;
    private Globar g;
    private Custom_SharedPreferences csp;
    public static int PHOTO_ALBUM_RESULT = 1;
    public static int PHOTO_CAMERA_RESULT = 2;
    private PhotoViewModel photoVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("gogoPhotoActivity","ok");
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_photo);
        this.binding.setPhoto(this);
        Intent intent = new Intent(this.getIntent());

        this.photoVM = new PhotoViewModel(this.binding , this,this , intent.getStringExtra("choice"));

        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);
    }


    public void photoChange() {
        this.photoVM.photoUpdate();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ( requestCode == PHOTO_ALBUM_RESULT && resultCode == RESULT_OK ) {

            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encode = Base64.encodeToString(byteArray,Base64.DEFAULT);

                AndroidNetworking.post(this.g.baseUrl+this.g.urls.get("photoUpload"))
                        .addBodyParameter("img",encode)
                        .addBodyParameter("code",this.g.code)
                        .addBodyParameter("deviceid",this.csp.getValue("deviceid",""))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // do anything with response
                                Log.d("photo_response",response.toString());
                                g.msg("Upload completed.");
                                photoVM.getPhotoUrl("");
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("photo_error",error.getErrorDetail());
                                g.msg("Upload failed.");
                            }
                        });

            } catch (Exception e) {
                Log.e("photo_result_error",e.toString());
            }
        } else if ( requestCode == PHOTO_CAMERA_RESULT && resultCode == RESULT_OK ) {
            this.photoVM.cameraPhotoUpload();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        photoVM.getPhotoUrl("");
    }

    private void photoUpdate() {

        int w = getPxFromDp(90,this);
        int h = getPxFromDp(90,this);
        final LayoutInflater inflater = LayoutInflater.from(this);

        for ( int i = 0 , j = this.urlList.size(); i < j; i++ ) {

            MainGetPhotoDTO r = this.urlList.get(i);

            final View view1 = inflater.inflate(R.layout.photo_gridlayout_item,this.gridLayout,false);
            ImageView iv = view1.findViewById(R.id.likeBt);
            ImageView iv2 = view1.findViewById(R.id.grid_photo);
            iv.setTag(i);
            iv.setOnClickListener(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            if ( i == 6 ) {
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED , 2);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED , 2);
                params.width = w * 2;
                params.height = h * 2;
                Picasso.get().load(this.g.mainUrl+r.getUrl()).resize(w*2,h*2).error(R.mipmap.ic_launcher).into(iv2);
            } else {
                params.width = w;
                params.height = h;
                Picasso.get().load(this.g.mainUrl+r.getUrl()).resize(w,h).error(R.mipmap.ic_launcher).into(iv2);
            }

            params.setMargins(1,1,1,1);
            view1.setBackgroundColor(getRandomColor());
            view1.setLayoutParams(params);
            this.gridLayout.addView(view1);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"tagId"+v.getTag(),Toast.LENGTH_SHORT).show();
    }

    public static int getPxFromDp(float dp  , Context c) {
        int px = 0;
        Context appContext = c;
        px = (int) (dp * appContext.getResources().getDisplayMetrics().density);
        return px;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    //photo Url 가져오기
    private void getPhotoUrl( final String photoCode) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("getMainPhoto")+"?code="+photoCode);
                    Type listType = new TypeToken<ArrayList<MainGetPhotoDTO>>(){}.getType();
                    urlList = gson.fromJson(je,listType);
                    handler.sendEmptyMessage(1);

                } catch (Exception e) {

                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    photoUpdate();
                    break;
            }
        }
    };

}
