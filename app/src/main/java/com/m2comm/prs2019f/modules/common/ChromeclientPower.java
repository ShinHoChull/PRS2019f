package com.m2comm.prs2019f.modules.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



/*
 *  WebChromeClient 같은경우는 재정의를 해주지 않으면 WebView URL이 그대로 노출이 된다.
 * */

public class ChromeclientPower extends WebChromeClient
{
    Activity activity;
    Context context,subContext;
    WebView webView;
    private int doubleCheckedPopUp = 0;

    //File Upload를 위한 ...

    public ValueCallback<Uri> filePathCallbackNormal;
    public ValueCallback<Uri[]> filePathCallbackLollipop;
    public static final int FILECHOOSER_NORMAL_REQ_CODE = 2833;
    public static final int FILECHOOSER_LOLLIPOP_REQ_CODE = 2779;

    private ValueCallback<Uri> mUploadMessage;
    private static final String TYPE_IMAGE = "image/*";
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    public ChromeclientPower(Activity activity, Context context, WebView webView) {
        this.context = context;
        this.subContext = activity;
        this.webView = webView;
        this.activity = activity;
    }

    // For Android Version < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        //System.out.println("WebViewActivity OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU), n=1");
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(TYPE_IMAGE);
        activity.startActivityForResult(intent, INPUT_FILE_REQUEST_CODE);
    }

    // For 3.0 <= Android Version < 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        //System.out.println("WebViewActivity 3<A<4.1, OS Version : " + Build.VERSION.SDK_INT + "\t openFC(VCU,aT), n=2");
        openFileChooser(uploadMsg, acceptType, "");
    }

    // For 4.1 <= Android Version < 5.0
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {

        mUploadMessage = uploadFile;
        imageChooser();
    }

    // For Android Version 5.0+
    // Ref: https://github.com/GoogleChrome/chromium-webview-samples/blob/master/input-file-example/app/src/main/java/inputfilesample/android/chrome/google/com/inputfilesample/MainFragment.java
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;
        imageChooser();
        return true;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    private void imageChooser() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(getClass().getName(), "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(TYPE_IMAGE);

        Intent[] intentArray;
        if(takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        activity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        Log.d("windowOpen", "open Url ="+view);
        // MainActivity.is_popup = false;

        webView.removeAllViews();

        final WebView childView = new WebView(this.activity);
//        subContext = activity;
//
//
//        childView.getSettings().setJavaScriptEnabled(true);
//        childView.getSettings().setDefaultTextEncodingName("utf-8");
//        childView.getSettings().setUseWideViewPort(true);
//        childView.getSettings().setDomStorageEnabled(true);
//
//
//        //팝업을 만들기위한 과정
//        childView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        childView.requestFocus();
//        childView.setWebChromeClient(this);
//        childView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//
//            /*Window.open 팝업 처리를 위하여 Main 레이아웃에 또다른 레이아웃의 스택을 쌓았다.
//            BackKey 를 누르면 Main 레이아웃이 작동을 하기 때문에 스텍에 올린 레이아웃이 삭제가안되어서
//             Main에서 removeAll을 통해서 스텍이 쌓인것을 모두 날렸다.
//             하지만 팝업창이 재요청이 되지않아 하는 수 없이 카운터를 적용시켜 설정값과 동일하다면 온클로즈윈도우를 호출하여
//              Webview를 destory시켜 버렸다.
//            */
//                if (MainActivity.is_popup == true) {
//                    onCloseWindow(childView);
//                    Log.e("onCloseWindow", "" + "if In");
//                }
//                Log.e("onPageStarted", "onPageStarted");
//            }
//        });
//
//        webView.addView(childView);
//        //팝업창이 최상위 WebViewStack의 첫부분(표시되는)을 focus하는 것 같은 느낌이 든다.
//        //그래서 oncreateWindow가 호출이되면 웹뷰의 page를 강제적으로 맨위로 focus를 주었다.
//
//        webView.pageUp(true);
//
//
//        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//        transport.setWebView(childView);
//        //transport.getWebView().pageUp(true);
//
//        resultMsg.sendToTarget();

        return true;
    }

    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
//        if(MainActivity.is_popup == true) {
//            window.destroy();
//        }

        //매개변수로 받은 webview 객체를 removeAllViews 시킴으로써 클로즈윈도우할때 같이 삭제를 해준다.
        webView.removeAllViews();
        doubleCheckedPopUp = 0;
        Log.e("windowClose", "close");
    }

    //dialogWindow cusomize 가능.
    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             final JsResult result) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(view.getContext()).setTitle("Alert")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,

                        new AlertDialog.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setCancelable(false)
                .create()
                .show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(view.getContext())
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("YES",
                        new AlertDialog.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                result.confirm();
                            }
                        })
                .setNegativeButton("NO",
                        new AlertDialog.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                result.cancel();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();
        return true;
    }
}
