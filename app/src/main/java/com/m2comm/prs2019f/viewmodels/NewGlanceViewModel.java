package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityNewGlanceBinding;
import com.m2comm.prs2019f.model.TypeOneDTO;
import com.m2comm.prs2019f.modules.common.ChromeclientPower;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Download;
import com.m2comm.prs2019f.modules.common.Download_PDFViewerActivity;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.MainActivity;
import com.m2comm.prs2019f.views.MenuActivity;
import com.m2comm.prs2019f.views.PopWebviewActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NewGlanceViewModel implements View.OnClickListener {


    ActivityNewGlanceBinding binding;
    Activity activity;
    Context c;
    Custom_SharedPreferences csp;
    Globar g;

    LinearLayout  days, bottom_layout ;
    TextView top_menu_txt, month ;
    int day_type = 0;
    String bottom_menu = "";
    int tab=0;
    ArrayList<TypeOneDTO> tvArr;
    String bgOn , bgOff , fontOn , fontOff;

    private ChromeclientPower chromeclient;

    public NewGlanceViewModel(ActivityNewGlanceBinding binding, Activity activity, Context c) {
        this.binding = binding;
        this.activity = activity;
        this.c = c;
        this.init();
    }

    private void registObjs() {

        this.binding.defaultClickV.setOnClickListener(this);
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.g.addFragment_Content_TopV(this.c,false);
        this.g.addFragment_Sub_TopV(this.c, "Program at a Glance");
        this.g.addFragment_BottomV(this.c);
        this.registObjs();
        this.tvArr = new ArrayList<>();

        this.chromeclient = new ChromeclientPower(activity,c,this.binding.wv);
        this.binding.wv.setWebViewClient(new WebviewCustomClient());
        this.binding.wv.setWebChromeClient(this.chromeclient);
        this.binding.wv.getSettings().setUseWideViewPort(true);
        this.binding.wv.getSettings().setJavaScriptEnabled(true);
        this.binding.wv.getSettings().setLoadWithOverviewMode(true);
        this.binding.wv.getSettings().setDefaultTextEncodingName("utf-8");
        this.binding.wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.binding.wv.getSettings().setSupportMultipleWindows(false);
        this.binding.wv.getSettings().setDomStorageEnabled(true);
        this.binding.wv.getSettings().setBuiltInZoomControls(true);
        this.binding.wv.getSettings().setDisplayZoomControls(false);
        this.binding.wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.binding.wv.getSettings().setTextZoom(90);


        AndroidNetworking.post("http://ezv.kr/voting/php/session/get_set.php")
                .addBodyParameter("tab",""+tab)
                .addBodyParameter("code",this.g.code)
                .addBodyParameter("deviceid",this.csp.getValue("deviceid",""))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject resultList = response;

                            if (!resultList.isNull("mon"))
                            month.setText(resultList.getString("mon"));

                            day_type = resultList.getInt("day_type");
                            bottom_menu = resultList.getString("bottom_menu");
                            LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);
                            lparam.setMargins(1, 0, 0, 0);
                            if(bottom_menu.equals("1") || bottom_menu.equals("2")) {
                                bottom_layout.setVisibility(View.VISIBLE);
                                ImageView sub_layout = new ImageView(c);
                                bottom_layout.addView(sub_layout);
                                sub_layout.setLayoutParams(lparam);
                                sub_layout.setImageResource(R.drawable.bottom_menu01);
                                sub_layout.setBackgroundColor(Color.parseColor("#45455c"));
                                sub_layout.setPadding(50,5,50,5);

                                sub_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent intent = new Intent(Fall2019_Web.this, Fall2019_Web.class);
//                                        intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+Global.F2019CODE+"&tab=-1");
//                                        startActivity(intent);
//                                        overridePendingTransition(0, 0);
//                                        finish();
                                    }
                                });


                                ImageView sub_layout2 = new ImageView(c);
                                bottom_layout.addView(sub_layout2);
                                sub_layout2.setLayoutParams(lparam);
                                sub_layout2.setImageResource(R.drawable.bottom_menu02);
                                sub_layout2.setBackgroundColor(Color.parseColor("#45455c"));
                                sub_layout2.setPadding(50,5,50,5);

                                sub_layout2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent intent = new Intent(Fall2019_Web.this, Fall2019_Web.class);
//                                        intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+Global.F2019CODE);
//                                        startActivity(intent);
//                                        overridePendingTransition(0, 0);
//                                        finish();
                                    }
                                });


                                ImageView sub_layout3 = new ImageView(c);
                                bottom_layout.addView(sub_layout3);
                                sub_layout3.setLayoutParams(lparam);
                                sub_layout3.setImageResource(R.drawable.bottom_menu03);
                                sub_layout3.setBackgroundColor(Color.parseColor("#45455c"));
                                sub_layout3.setPadding(50,5,50,5);

                                sub_layout3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Intent intent = new Intent(Fall2019_Web.this, Fall2019_Web.class);
//                                        intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+Global.F2019CODE+"&tab=-2");
//                                        startActivity(intent);
//                                        overridePendingTransition(0, 0);
//                                        finish();
                                    }
                                });

                                if(bottom_menu.equals("2")) {

                                    ImageView sub_layout4 = new ImageView(c);
                                    bottom_layout.addView(sub_layout4);
                                    sub_layout4.setLayoutParams(lparam);
                                    sub_layout4.setImageResource(R.drawable.bottom_menu02);
                                    sub_layout4.setBackgroundColor(Color.parseColor("#45455c"));
                                    sub_layout4.setPadding(50,5,50,5);

                                    sub_layout4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            Intent intent = new Intent(Fall2019_Web.this, Fall2019_Web.class);
//                                            intent.putExtra("page", "http://ezv.kr/voting/php/session/list.php?code="+Global.F2019CODE+"&tab=-5");
//                                            startActivity(intent);
//                                            overridePendingTransition(0, 0);
//                                            finish();
                                        }
                                    });
                                }
                            }

                            final JSONArray day_info = new JSONArray(resultList.getString("day"));
                            Log.d("dayInfo",day_info.length()+"");
                            if(day_type==1 && day_info.length()>1) {
                                Log.d("dayType1","ok");
                                binding.dayType1.setVisibility(View.VISIBLE);

                                LinearLayout.LayoutParams lparam2 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);


                                for(int i=0;i<day_info.length();i++) {
                                    final int finalI = i;
                                    TextView day_text = new TextView(c);
                                    binding.dayType1.addView(day_text);
                                    day_text.setLayoutParams(lparam2);
                                    day_text.setTextSize(13);

                                    if(day_info.getJSONObject(i).getInt("tab") == resultList.getInt("tab")) {
                                        binding.wv.loadUrl(urlSetting(g.urls.get("glance")+"?tab="+day_info.getJSONObject(i).getInt("tab")));
                                        bgOn = "#"+resultList.getString("menu_bg_on");
                                        fontOn = "#"+resultList.getString("menu_font_on");
                                        day_text.setTextColor(Color.parseColor("#"+resultList.getString("menu_font_on")));
                                        day_text.setBackgroundColor(Color.parseColor("#"+resultList.getString("menu_bg_on")));
                                    } else {
                                        bgOff = "#"+resultList.getString("menu_bg");
                                        fontOff = "#"+resultList.getString("menu_font");
                                        day_text.setTextColor(Color.parseColor("#"+resultList.getString("menu_font")));
                                        day_text.setBackgroundColor(Color.parseColor("#"+resultList.getString("menu_bg")));
                                    }
                                    day_text.setText(day_info.getJSONObject(i).getString("name"));
                                    day_text.setGravity(Gravity.CENTER);
                                    day_text.setTag(i);
                                    tvArr.add(new TypeOneDTO(day_text , String.valueOf(day_info.getJSONObject(i).getInt("tab"))));

                                    day_text.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tabChange((TextView)view);
//                                            Intent intent = new Intent(Fall2019_Web.this, Fall2019_Web.class);
//                                            try {
//                                                intent.putExtra("tab", day_info.getJSONObject(finalI).getInt("tab"));
//                                                intent.putExtra("page", "http://ezv.kr/voting/php/session/glance.php?code="+Global.F2019CODE+"&tab="+day_info.getJSONObject(finalI).getInt("tab"));
//                                                intent.putExtra("popup", false);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            activity.startActivity(intent);
//                                            activity.overridePendingTransition(0, 0);
//                                            activity.finish();
                                        }
                                    });

                                }
                            }

                            else if(day_type==2 && day_info.length()>1) {
                                binding.dayType2.setVisibility(View.VISIBLE);
                                for(int i=0;i<day_info.length();i++) {

                                    LinearLayout day_layout = new LinearLayout(c);
                                    days.addView(day_layout);

                                    day_layout.getLayoutParams().width = dpToPx(40);
                                    day_layout.getLayoutParams().height = dpToPx(42);
                                    day_layout.setGravity(Gravity.CENTER);
                                    day_layout.setOrientation(LinearLayout.VERTICAL);


                                    TextView day_text = new TextView(c);
                                    day_layout.addView(day_text);

                                    day_text.setGravity(Gravity.CENTER);
                                    day_text.setTextSize(10);
                                    day_text.setText(day_info.getJSONObject(i).getString("week"));
                                    day_text.setTypeface(null, Typeface.BOLD);
                                    day_text.setTextColor(Color.parseColor("#282828"));

                                    FrameLayout temp = new FrameLayout(c);
                                    day_layout.addView(temp);
                                    temp.getLayoutParams().width = dpToPx(25);
                                    temp.setPadding(0,5,0,0);

                                    ImageView day_bg = new ImageView(c);
                                    temp.addView(day_bg);

                                    day_bg.getLayoutParams().width = dpToPx(25);
                                    day_bg.getLayoutParams().height = dpToPx(25);
                                    day_bg.setImageResource(R.drawable.cir);
                                    day_bg.setColorFilter(Color.parseColor("#e3e5ec"));


                                    TextView day_text2 = new TextView(c);
                                    temp.addView(day_text2);

                                    day_text2.getLayoutParams().width = dpToPx(25);
                                    day_text2.getLayoutParams().height = dpToPx(25);
                                    day_text2.setGravity(Gravity.CENTER);
                                    day_text2.setTextSize(13);
                                    day_text2.setText(day_info.getJSONObject(i).getString("day"));
                                    if(day_info.getJSONObject(i).getInt("tab")==-1){
                                        day_text2.setTextColor(Color.parseColor("#9fa1a9"));
                                    }else {

                                        final int finalI = i;
                                        day_layout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });

                                        day_text2.setTypeface(null, Typeface.BOLD);
                                        if(day_info.getJSONObject(i).getInt("tab") == resultList.getInt("tab")) {
                                            day_text2.setTextColor(Color.parseColor("#ffffff"));
                                            day_bg.setColorFilter(Color.parseColor("#" + resultList.getString("session_day_bg")));
                                        }else {
                                            day_text2.setTextColor(Color.parseColor("#282828"));
                                        }
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            Log.d("glance_error",e.toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void tabChange(TextView v) {
        Log.d("TagString",v.getTag()+"");
        int tag = (int)v.getTag();
        for ( int i = 0 , j = tvArr.size() ; i < j; i ++ ) {
            if (i == tag) {
                Log.d("TabString",tvArr.get(i).getTab()+"");
                tvArr.get(i).getTv().setTextColor(Color.parseColor(this.fontOn));
                tvArr.get(i).getTv().setBackgroundColor(Color.parseColor(this.bgOn));
                this.binding.wv.loadUrl(this.urlSetting(this.g.urls.get("glance")+"?tab="+tvArr.get(i).getTab()));
            } else {
                tvArr.get(i).getTv().setTextColor(Color.parseColor(this.fontOff));
                tvArr.get(i).getTv().setBackgroundColor(Color.parseColor(this.bgOff));
            }
        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.default_clickV:
                this.binding.defaultClickV.setVisibility(View.GONE);
                return;


        }
    }

    private int dpToPx(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, activity.getResources().getDisplayMetrics());
        return px;
    }

    private class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
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
                if (binding.wv.canGoBack()) {
                    binding.wv.goBack();
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

}
