package com.m2comm.prs2019f.viewmodels;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.google.gson.reflect.TypeToken;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityPhotoBinding;
import com.m2comm.prs2019f.model.MainGetPhotoDTO;
import com.m2comm.prs2019f.modules.common.CustomHandler;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.modules.common.PermissionRequester;
import com.m2comm.prs2019f.views.Img_ZoomInOut;
import com.m2comm.prs2019f.views.PhotoActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoViewModel implements View.OnClickListener {

    private ActivityPhotoBinding activity;
    private ArrayList<MainGetPhotoDTO> urlList;
    private Context c;
    private Globar g;
    private Activity a;
    private Custom_SharedPreferences csp;
    private String strUrl = "";
    private CustomHandler customHandler;
    private boolean isSketch = false;
    File picture;
    private String imagePath;



    private void listenerRegister() {
        this.activity.photoButton.setOnClickListener(this);
        this.activity.photoSelectBt1.setOnClickListener(this);
        this.activity.photoSelectBt2.setOnClickListener(this);
        this.activity.photoSelectBt3.setOnClickListener(this);
        this.activity.photoSelectBt4.setOnClickListener(this);
        this.activity.photoSelectBt5.setOnClickListener(this);
        this.activity.photoPopAlbum.setOnClickListener(this);
        this.activity.photoPopCamera.setOnClickListener(this);
        this.activity.photoPopCancelBt.setOnClickListener(this);
    }

    private void photoColorReset() {
        this.isSketch = true;
        this.activity.photoSelectBt1.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_default_bg_color));
        this.activity.photoSelectBt2.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_default_bg_color));
        this.activity.photoSelectBt3.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_default_bg_color));
        this.activity.photoSelectBt4.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_default_bg_color));
        this.activity.photoSelectBt5.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_default_bg_color));

        this.activity.photoSelectBt1.setTextColor(this.c.getResources().getColor(R.color.photo_tab_default_font_color));
        this.activity.photoSelectBt2.setTextColor(this.c.getResources().getColor(R.color.photo_tab_default_font_color));
        this.activity.photoSelectBt3.setTextColor(this.c.getResources().getColor(R.color.photo_tab_default_font_color));
        this.activity.photoSelectBt4.setTextColor(this.c.getResources().getColor(R.color.photo_tab_default_font_color));
        this.activity.photoSelectBt5.setTextColor(this.c.getResources().getColor(R.color.photo_tab_default_font_color));
        this.activity.photoGridV.removeAllViews();
        this.activity.photoButton.setVisibility(View.GONE);
        this.activity.photoBottom.setVisibility(View.GONE);
    }

    public PhotoViewModel(ActivityPhotoBinding activity, Context c, Activity a , String str) {
        this.activity = activity;
        this.c = c;
        this.a = a;
        this.strUrl = str;
        this.init();
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.customHandler = new CustomHandler(this.c);
        this.g.addFragment_Content_TopV(this.c,false);
        this.g.addFragment_Sub_TopV(this.c, "Photo Gallery");
        this.listenerRegister();

        this.activity.photoGridV.setColumnCount(4);
        this.photoColorReset();
        this.strUrl = this.strUrl.equals("") ? "144" : this.strUrl ;
        if ( this.strUrl.equals("144") ) {
            this.activity.photoSelectBt1.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
            this.activity.photoSelectBt1.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
            this.getPhotoUrl("144");
        }  else {
            this.activity.photoSelectBt2.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
            this.activity.photoSelectBt2.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
        }

    }

    public void photoUpdate() {

        this.activity.photoPopV.setVisibility(View.GONE);
        this.activity.photoGridV.removeAllViews();
        if (this.urlList == null || this.urlList.size() <= 0) {
            this.isSketch = false;
            return;
        }

        int w = this.g.w(90);
        int h = this.g.h(90);
        final LayoutInflater inflater = LayoutInflater.from(this.c);

        int photoSortCount = 0;

        int bigSize = urlList.size() / 5;

        ArrayList<MainGetPhotoDTO> tempArr = new ArrayList<>();

        for (int i = 0, j = bigSize; i<j ; i++) {
            tempArr.add(urlList.get(0));
            this.urlList.remove(0);
        }

        boolean isPhotoSort = true;

        for (int i = 0, j = this.urlList.size() + tempArr.size(); i < j; i++) {
            if ((isPhotoSort && photoSortCount == 0) || (!isPhotoSort && photoSortCount == 2)) {
                if (tempArr.size() > 0) {
                    Log.d("indexI",i+"");
                    //this.urlList.set(i,tempArr.get(0));
                    this.urlList.add(i,tempArr.get(0));
                    tempArr.remove(0);
                }
            }

            if (photoSortCount >= 4) {
                /*
                 * true -> leftPhoto  || false -> rightPhoto
                 * */
                isPhotoSort = isPhotoSort ? false : true;
                photoSortCount = 0;
            } else {
                photoSortCount += 1;
            }
        }
        photoSortCount = 0;
        isPhotoSort = true;

        for (int i = 0, j = this.urlList.size(); i < j; i++) {
            MainGetPhotoDTO r = this.urlList.get(i);

            final View view1 = inflater.inflate(R.layout.photo_gridlayout_item, this.activity.photoGridV, false);
            TextView iv = view1.findViewById(R.id.likeBt);
            TextView iv1 = view1.findViewById(R.id.likeCount);
            ImageView iv2 = view1.findViewById(R.id.grid_photo);

            iv.setText(this.c.getText(R.string.heart));
            iv.setTag(i);
            iv.setOnClickListener(this);
            //photoIMG
            iv2.setTag(i);
            iv2.setOnClickListener(this);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            int tempIndex = bigSize * 5;

            params.width = w;
            params.height = h;
            Picasso.get().load(this.g.mainUrl + "/voting/upload/photo/" + r.getUrl()).resize(w, h).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(iv2);

//            if (i >= tempIndex) {
//                //마지막 포토 리스트를 뿌릴때 균일하게 작업이 되지 않을때를 대비한 작업
//             //   r = this.urlList.get(i);
//                params.width = w;
//                params.height = h;
//                Picasso.get().load(this.g.mainUrl + "/voting/upload/photo/" + r.getUrl()).resize(w, h).placeholder(R.drawable.photo_holder).error(R.drawable.photo_holder).into(iv2);
//            } else if ((isPhotoSort && photoSortCount == 0) || (!isPhotoSort && photoSortCount == 2)) {
//
//               // if (tempArr.size() > 0) {
////                    Log.d("indexI",i+"");
////                    //this.urlList.set(i,tempArr.get(0));
////                    this.urlList.add(i,tempArr.get(0));
////                    tempArr.remove(0);
//                 //   r = this.urlList.get(i);
//
//                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 2);
//                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2);
//                    params.width = w * 2;
//                    params.height = h * 2;
//
//                    Picasso.get().load(this.g.mainUrl + "/voting/upload/photo/" + r.getUrl()).
//                            resize(w * 2, h * 2).placeholder(R.drawable.photo_holder).error(R.drawable.photo_holder).into(iv2);
//              //  }
//            } else {
//                //r = this.urlList.get(i);
//                params.width = w;
//                params.height = h;
//                Picasso.get().load(this.g.mainUrl + "/voting/upload/photo/" + r.getUrl()).resize(w, h).placeholder(R.drawable.photo_holder).error(R.mipmap.ic_launcher).into(iv2);
//            }
//
//            if (photoSortCount >= 4) {
//                /*
//                 * true -> leftPhoto  || false -> rightPhoto
//                 * */
//                isPhotoSort = isPhotoSort ? false : true;
//                photoSortCount = 0;
//            } else {
//                photoSortCount += 1;
//            }

            // likeCount > 0
            if (r != null && r.getCnt() > 0) {
                Typeface fontAwsome = Typeface.createFromAsset(this.c.getAssets(), "fa_solid_900.ttf");
                iv.setTypeface(fontAwsome);
                iv.setTextColor(this.c.getResources().getColor(R.color.main_color_RED));
            } else {
                Typeface fontAwsome = Typeface.createFromAsset(this.c.getAssets(), "fa_regular_400.ttf");
                iv.setTypeface(fontAwsome);
                iv.setTextColor(this.c.getResources().getColor(R.color.main_color_white));
            }
            //photoText
            iv1.setText(String.valueOf(r.getCnt()));
            iv2.setPadding(10, 10, 10, 10);
            view1.setLayoutParams(params);
            this.activity.photoGridV.addView(view1);
        }
        //그리드레이아웃을 그리는 중에 계속 바꿔주게되면 문제가 생겨서 막아놓았다.
        this.isSketch = false;
    }

    //photo Url 가져오기
    public void getPhotoUrl(final String photoCode) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {

                    Log.d("urlurl",g.baseUrl + g.urls.get("photoGet") + "?tab=" + strUrl +
                            "&deviceid=" + csp.getValue("deviceid", "") + "&code=" + g.code);

                    JsonElement je = g.getParser(g.baseUrl + g.urls.get("photoGet") + "?tab=" + strUrl +
                            "&deviceid=" + csp.getValue("deviceid", "") + "&code=" + g.code);
                    Log.d("jeje", je.toString());

                    Type listType = new TypeToken<ArrayList<MainGetPhotoDTO>>() {
                    }.getType();
                    urlList = gson.fromJson(je, listType);
                    msg.what = CustomHandler.PHOTO_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {
                    isSketch = false;
                    Log.d("gogophoto2", "ok :" + e.toString());
                }
            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        MainGetPhotoDTO r = new MainGetPhotoDTO(1, "", 1, "", "");
        if (v.getTag() != null) {
            r = this.urlList.get((int) v.getTag());
        }

        Activity a = (Activity) c;

        switch (v.getId()) {

            case R.id.likeBt:
                int tempLike = 1;
                if (Integer.valueOf(r.getMyfav()) > 0) {
                    tempLike = 0;
                }

                AndroidNetworking.get(this.g.baseUrl + this.g.urls.get("photoLike"))
                        .addQueryParameter("sid", String.valueOf(r.getSid()))
                        .addQueryParameter("code", this.g.code)
                        .addQueryParameter("deviceid", csp.getValue("deviceid", ""))
                        .addQueryParameter("val", String.valueOf(tempLike))
                        .setPriority(Priority.LOW)
                        .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("likelike", response);
                        getPhotoUrl(strUrl);
                    }

                    @Override
                    public void onError(ANError anError) {
                        g.baseAlertMessage("Error", anError.toString());
                    }
                });

                break;
            case R.id.grid_photo:
                if (isSketch) {
                    return;
                }
                Intent zoomActivity = new Intent(this.c, Img_ZoomInOut.class);
                zoomActivity.putExtra("nowUrl", r.getUrl());
                zoomActivity.putExtra("choiceNum", (int) v.getTag());
                zoomActivity.putExtra("array", this.urlList);
                this.c.startActivity(zoomActivity);
                break;

            case R.id.photo_select_bt1:
                if (isSketch) {
                    return;
                }
                photoColorReset();
                TextView tv1 = (TextView) v;
                tv1.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
                tv1.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
                this.strUrl = "144";
                this.getPhotoUrl("144");
                break;

            case R.id.photo_select_bt2:
                if (isSketch) {
                    return;
                }
                photoColorReset();
                TextView tv2 = (TextView) v;
                tv2.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
                tv2.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
                this.strUrl = "145";
                this.getPhotoUrl("145");
                break;

            case R.id.photo_select_bt3:
                if (isSketch) {
                    return;
                }
                photoColorReset();
                TextView tv3 = (TextView) v;
                tv3.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
                tv3.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
                this.strUrl = "146";
                this.getPhotoUrl("146");
                break;

            case R.id.photo_select_bt5:
                if (isSketch) {
                    return;
                }
                photoColorReset();
                TextView tv4 = (TextView) v;
                tv4.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
                tv4.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
                this.strUrl = "-2";
                this.getPhotoUrl("-2");
                break;

            case R.id.photo_select_bt4:
                if (isSketch) {
                    return;
                }
                photoColorReset();
                this.activity.photoButton.setVisibility(View.VISIBLE);
                this.activity.photoBottom.setVisibility(View.VISIBLE);
                TextView tv5 = (TextView) v;
                tv5.setTextColor(this.c.getResources().getColor(R.color.photo_tab_click_font_color));
                tv5.setBackgroundColor(this.c.getResources().getColor(R.color.photo_tab_click_bg_color));
                this.strUrl = "-1";
                this.getPhotoUrl("-1");

                break;

            case R.id.photoButton:
//                if (!isLogin()) {
//                    this.g.loginAlertMessage("Alert", "Do you want to sign in?", a);
//                    return;
//                }
                this.activity.photoPopV.setVisibility(View.VISIBLE);

                break;

            case R.id.photo_pop_camera:
                if (isExistCameraApplication()) {
                    // Camera Application을 실행한다.
                    Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 찍은 사진을 보관할 파일 객체를 만들어서 보낸다.
                    picture = savePictureFile();
                    if (picture != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(c, c.getPackageName() + ".provider", picture);
                            cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        } else {
                            cameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                        }

                        a.startActivityForResult(cameraApp, PhotoActivity.PHOTO_CAMERA_RESULT);
                    }
                } else {
                    Toast.makeText(c, "Install the camera app.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.photo_pop_album:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                a.startActivityForResult(intent, PhotoActivity.PHOTO_ALBUM_RESULT);
                break;

            case R.id.photo_pop_cancelBt:
                this.activity.photoPopV.setVisibility(View.GONE);
                break;

        }
    }

    public void cameraPhotoUpload() {
        if (picture.exists()) {
            try {
                //Bitmap myBitmap = BitmapFactory.decodeFile(picture.getAbsolutePath());
                Bitmap myBitmap = rotateBitmap((picture.getAbsolutePath()));
                myBitmap = resizeBitmapImage(myBitmap, 1024);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                String encode = Base64.encodeToString(byteArray, Base64.DEFAULT);

                AndroidNetworking.post(this.g.baseUrl + this.g.urls.get("photoUpload"))
                        .addBodyParameter("img", encode)
                        .addBodyParameter("code", this.g.code)
                        .addBodyParameter("deviceid", this.csp.getValue("deviceid", ""))
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // do anything with response
                                g.msg("Upload completed.");
                                getPhotoUrl("");
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                g.msg("Upload failed.");
                            }
                        });

            } catch (Exception e) {
                g.msg("Upload failed.");
            }

        } else {
            g.msg("There are no photo files..");
        }
    }

    public boolean isLogin() {
        return this.csp.getValue("isLogin", false);
    }

    private boolean isExistCameraApplication() {
        // Android의 모든 Application을 얻어온다.
        PackageManager packageManager = this.c.getPackageManager();
        // Camera Application
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.ACTION_IMAGE_CAPTURE을 처리할 수 있는 App 정보를 가져온다.
        List cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);
        // 카메라 App이 적어도 한개 이상 있는지 리턴
        return cameraApps.size() > 0;
    }

    /**
     * 카메라에서 찍은 사진을 외부 저장소에 저장한다. * * @return
     */
    private File savePictureFile() {
        // 외부 저장소 쓰기 권한을 얻어온다.
        PermissionRequester.Builder requester = new PermissionRequester.Builder(a);
        int result = requester.create().request(Manifest.permission.WRITE_EXTERNAL_STORAGE, 20000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
            }
        });
        // 사용자가 권한을 수락한 경우
        if (result == PermissionRequester.ALREADY_GRANTED || result == PermissionRequester.REQUEST_PERMISSION) {
            // 사진 파일의 이름을 만든다.
            // Date는 java.util 을 Import 한다.
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());

            /** * 사진파일이 저장될 장소를 구한다.
             *
             * 외장메모리에서 사진을 저장하는 폴더를 찾아서
             * * 그곳에 MYAPP 이라는 폴더를 만든다. */
            String filePath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            String fileName = "kk";

            File pictureStorage = new File(filePath + "/" + fileName);
            // 만약 장소가 존재하지 않는다면 폴더를 새롭게 만든다.
            if (!pictureStorage.exists()) {
                /** * mkdir은 폴더를 하나만 만들고, * mkdirs는 경로상에 존재하는 모든 폴더를 만들어준다. */
                pictureStorage.mkdirs();
            }
            try {
                File file = File.createTempFile("prs2019f_", ".png", pictureStorage);
                // ImageView에 보여주기위해 사진파일의 절대 경로를 얻어온다.
                imagePath = file.getAbsolutePath();
                Log.d("file_error", imagePath);
                // 찍힌 사진을 "갤러리" 앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imagePath);
                //Uri contentUri = Uri.fromFile(f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(c, c.getPackageName() + ".provider", f);
                    mediaScanIntent.setDataAndType(contentUri, ".png");
                    mediaScanIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    mediaScanIntent.setDataAndType(Uri.fromFile(f), ".png");
                }

                c.sendBroadcast(mediaScanIntent);
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 사용자가 권한을 거부한 경우
        else {
        }
        return null;
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }


    //회전방지
    public static Bitmap rotateBitmap(String filePath) {

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Matrix matrix = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


}
