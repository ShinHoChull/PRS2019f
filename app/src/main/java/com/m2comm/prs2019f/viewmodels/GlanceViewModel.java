package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityGlanceBinding;
import com.m2comm.prs2019f.model.GlanceTopDTO;
import com.m2comm.prs2019f.model.GlanceTopDTO_type1;
import com.m2comm.prs2019f.model.Glance_sub_top;
import com.m2comm.prs2019f.model.Glance_sub_top_type1;
import com.m2comm.prs2019f.modules.common.ChromeclientPower;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Download;
import com.m2comm.prs2019f.modules.common.Download_PDFViewerActivity;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.PopWebviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GlanceViewModel implements View.OnClickListener {

    private ActivityGlanceBinding binding;
    private Context c;
    private Globar g;
    private Custom_SharedPreferences csp;
    private Activity activity;
    private ChromeclientPower chromeclient;
    private GlanceTopDTO glanceTopDTO;
    private GlanceTopDTO_type1 glanceTopDTO_type1;

    private void listenerRegister() {
        this.binding.bt1V.setOnClickListener(this);
        this.binding.bt2V.setOnClickListener(this);
    }

    public GlanceViewModel( ActivityGlanceBinding binding , Context c , Activity activity ) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.g.addFragment_Content_TopV(this.c,true);
        this.g.addFragment_Sub_TopV(this.c,"Program at a Glance");
        this.g.addFragment_Content_BottomV(this.c , false);
        this.listenerRegister();
        this.reSetBt();
        this.getProgram();

        this.binding.day3.setOnClickListener(this);
        this.binding.day4.setOnClickListener(this);
        this.binding.defaultClickV.setOnClickListener(this);

        this.chromeclient = new ChromeclientPower(activity,c,this.binding.glanceWebview);
        this.binding.glanceWebview.setWebViewClient(new WebviewCustomClient());
        this.binding.glanceWebview.setWebChromeClient(this.chromeclient);
        this.binding.glanceWebview.getSettings().setUseWideViewPort(true);
        this.binding.glanceWebview.getSettings().setJavaScriptEnabled(true);
        this.binding.glanceWebview.getSettings().setLoadWithOverviewMode(true);
        this.binding.glanceWebview.getSettings().setDefaultTextEncodingName("utf-8");
        this.binding.glanceWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.binding.glanceWebview.getSettings().setSupportMultipleWindows(false);
        this.binding.glanceWebview.getSettings().setDomStorageEnabled(true);
        this.binding.glanceWebview.getSettings().setBuiltInZoomControls(true);
        this.binding.glanceWebview.getSettings().setDisplayZoomControls(false);
        this.binding.glanceWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.binding.glanceWebview.getSettings().setTextZoom(90);
    }

    private void getProgram() {
        AndroidNetworking.get(this.g.baseUrl + this.g.urls.get("glance_top"))
                .addQueryParameter("code", this.g.code)
                .setPriority(Priority.LOW)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Log.d("glance_Obj",response + "");
                    if ( response.getString("day_type").equals("1") ) {
                        ArrayList<Glance_sub_top_type1> sub_array = new ArrayList<>();
                        for ( int i = 0, j = response.getJSONArray("day").length(); i < j ; i ++ ) {
                            JSONObject obj =  response.getJSONArray("day").getJSONObject(i);
                            sub_array.add(new Glance_sub_top_type1(obj.getString("tab"),obj.getString("name")));
                        }
                        glanceTopDTO_type1 = new GlanceTopDTO_type1(response.getString("day_type"),
                                response.getString("bottom_menu"),
                                response.getString("bottom_menu_now"),
                                response.getString("bottom_menu_program"),
                                response.getString("bottom_menu_glance"),
                                response.getString("bottom_menu_myfav"),
                                response.getString("bottom_menu_category"),
                                response.getString("session_topmenu_bg"),
                                response.getString("session_topmenu_font"),
                                response.getString("menu_bg"),
                                response.getString("menu_bg_on"),
                                response.getString("menu_font"),
                                response.getString("menu_font_on"),
                                response.getString("session_day_bg"),
                                response.getString("tab"),
                                response.getString("mon"),
                                sub_array);
                    } else {
                        ArrayList<Glance_sub_top> sub_array = new ArrayList<>();
                        for ( int i = 0, j = response.getJSONArray("day").length(); i < j ; i ++ ) {
                            JSONObject obj =  response.getJSONArray("day").getJSONObject(i);
                            sub_array.add(new Glance_sub_top(obj.getString("tab"),obj.getString("day"),obj.getString("week")));
                        }
                        glanceTopDTO = new GlanceTopDTO(response.getString("day_type"),
                                response.getString("bottom_menu"),
                                response.getString("bottom_menu_now"),
                                response.getString("bottom_menu_program"),
                                response.getString("bottom_menu_glance"),
                                response.getString("bottom_menu_myfav"),
                                response.getString("bottom_menu_category"),
                                response.getString("session_topmenu_bg"),
                                response.getString("session_topmenu_font"),
                                response.getString("menu_bg"),
                                response.getString("menu_bg_on"),
                                response.getString("menu_font"),
                                response.getString("menu_font_on"),
                                response.getString("session_day_bg"),
                                response.getString("tab"),
                                response.getString("mon"),
                                sub_array);
                    }

                    buttonSetting();
                }catch (JSONException e) {
                    Log.d("jsonException",e.toString());
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(c,"Program Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buttonSetting() {
        if (glanceTopDTO != null) {
            this.binding.glanceSelectBox.setVisibility(View.VISIBLE);
            this.binding.glanceSelectBoxType2.setVisibility(View.GONE);
            this.binding.glanceSelectBox.setBackgroundColor(Color.parseColor(this.glanceTopDTO.getSession_topmenu_bg()));
            this.tabclick(this.glanceTopDTO.getTab());
        } else {
            this.binding.glanceSelectBox.setVisibility(View.GONE);
            this.binding.glanceSelectBoxType2.setVisibility(View.VISIBLE);
            this.type2Click(this.glanceTopDTO_type1.getTab());
        }
    }

    private void tabclick(String tab) {

        if ( tab.equals("99") ) {
            this.binding.day3Cir.setVisibility(View.VISIBLE);
            this.binding.day3Txt.setTextColor(Color.parseColor("#"+this.glanceTopDTO.getMenu_font_on()));
            this.binding.day4Cir.setVisibility(View.GONE);
            this.binding.day4Txt.setTextColor(Color.parseColor("#"+this.glanceTopDTO.getMenu_font()));
        } else {
            this.binding.day3Cir.setVisibility(View.GONE);
            this.binding.day3Txt.setTextColor(Color.parseColor("#"+this.glanceTopDTO.getMenu_font()));
            this.binding.day4Cir.setVisibility(View.VISIBLE);
            this.binding.day4Txt.setTextColor(Color.parseColor("#"+this.glanceTopDTO.getMenu_font_on()));
        }

        this.binding.glanceWebview.loadUrl(this.urlSetting(this.g.urls.get("glance")+"?tab="+tab));
    }

    private void type2Click(String tab) {
        if ( tab.equals("99") ) {
            this.changeBtColor(this.binding.bt1Text,this.binding.bt1Bottom);
        } else {
            this.changeBtColor(this.binding.bt2Text,this.binding.bt2Bottom);
        }
        this.binding.glanceWebview.loadUrl(this.urlSetting(this.g.urls.get("glance")+"?tab="+tab));
    }



    public void reSetBt() {
        this.binding.bt1Text.setTextColor(Color.BLACK);
        this.binding.bt2Text.setTextColor(Color.BLACK);
        this.binding.bt1Text.setBackgroundColor(this.c.getResources().getColor(R.color.main_color_white));
        this.binding.bt2Text.setBackgroundColor(this.c.getResources().getColor(R.color.main_color_white));
        this.binding.bt1Bottom.setVisibility(View.GONE);
        this.binding.bt2Bottom.setVisibility(View.GONE);
    }

    public String urlSetting(String paramUrl) {
        String deviceid = csp.getValue("deviceid","");
        String url = this.g.baseUrl + paramUrl;

        if (paramUrl.startsWith("http") || paramUrl.startsWith("https")) {
            url = paramUrl;
        }
        if ( paramUrl.contains("?") )url += "&";
        else url += "?";
        url += "deviceid="+deviceid+"&device=android&id=android&login="+csp.getValue("isLogin",false)+"&code="+this.g.code;

        return url;
    }

    private class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            Log.d("aaaaa","alnaljsdlkfslakd");
            String[] urlCut = url.split("/");
            Log.d("NowUrl",url);
            if ( url.startsWith(g.mainUrl) == false ) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
                return true;
            } else if ( g.extPDFSearch(urlCut[urlCut.length - 1]) ) {
                Intent content = new Intent(c, Download_PDFViewerActivity.class);
                content.putExtra("url", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                // view.loadUrl(doc);
                return true;
            } else if ( g.extSearch(urlCut[urlCut.length - 1]) ) { //기타 문서 Search
                new Download(url,c,urlCut[urlCut.length - 1]);
                return true;
            } else if ( g.imgExtSearch(urlCut[urlCut.length - 1]) ) { //이미지 Search
                Intent content = new Intent(c, PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            } else if (url.contains("glance_sub.php")) {
                Intent content = new Intent(c,PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            } else if (urlCut[urlCut.length -1].equals("back.php")) {
                if (binding.glanceWebview.canGoBack()) {
                    binding.glanceWebview.goBack();
                } else {
                    activity.finish();
                    activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                }
                return true;
            }
            return false;
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

    private void changeBtColor(TextView t, LinearLayout l ) {
        t.setTextColor(ContextCompat.getColor(this.c,R.color.main_color_white));
        t.setBackgroundColor(this.c.getResources().getColor(R.color.main_color_blue));
        //l.setBackgroundResource(R.color.main_color_brown);
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.default_clickV:
                this.binding.defaultClickV.setVisibility(View.GONE);
                return;


            case R.id.login_closeBt:
                a.finish();
                a.overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
                break;

            case R.id.bt1V:
                this.reSetBt();
                this.type2Click("99");
                break;

            case R.id.bt2V:
                this.reSetBt();
                this.type2Click("100");
                break;

            case R.id.day3:
                this.tabclick("99");
                break;

            case R.id.day4:
                this.tabclick("100");
                break;
        }
    }
}
