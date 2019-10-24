package com.m2comm.prs2019f.viewmodels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.m2comm.prs2019f.databinding.FragmentFeedBackBinding;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;


public class FeedBackViewModel {

    private FragmentFeedBackBinding activity;
    private Globar g;
    private Context c;
    public TitleDTO titleDTO;
    private Custom_SharedPreferences csp;

    public FeedBackViewModel(FragmentFeedBackBinding binding , Context c , TitleDTO titleDTO) {
        this.activity = binding;
        this.c = c;
        this.titleDTO = titleDTO;
        this.init();
    }

    public void init() {

        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);

        this.activity.feedbackWebview.setWebViewClient(new WebviewCustomClient());
        this.activity.feedbackWebview.getSettings().setUseWideViewPort(true);
        this.activity.feedbackWebview.getSettings().setJavaScriptEnabled(true);
        this.activity.feedbackWebview.getSettings().setLoadWithOverviewMode(true);
        this.activity.feedbackWebview.getSettings().setDefaultTextEncodingName("utf-8");
        this.activity.feedbackWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.activity.feedbackWebview.getSettings().setSupportMultipleWindows(false);
        this.activity.feedbackWebview.getSettings().setDomStorageEnabled(true);
        this.activity.feedbackWebview.getSettings().setBuiltInZoomControls(true);
        this.activity.feedbackWebview.getSettings().setDisplayZoomControls(false);
        this.activity.feedbackWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        this.activity.feedbackWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
                        setTitle("알림").
                        setMessage(message).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        }).create();
                dialog.show();
                result.confirm();
                return true;
            }
        });

        this.activity.feedbackWebview.loadUrl(titleDTO.getUrl()+"&name="+csp.getValue("name","")+
                "&office="+csp.getValue("office","")+
                "&deviceid="+csp.getValue("deviceid","")+
                "&regist_sid="+csp.getValue("sid","")+
                "&judge="+csp.getValue("judge",""));
    }

    public class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("onPageStarted",url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("onLoadResource",url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("onPageFinished",url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(c, "서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT ).show();
            view.loadUrl("about:blank");
        }
    }

}
