
package com.m2comm.prs2019f.modules.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.views.Bottom;
import com.m2comm.prs2019f.views.ContentTop;
import com.m2comm.prs2019f.views.ContentsActivity;
import com.m2comm.prs2019f.views.LoginActivity;
import com.m2comm.prs2019f.views.SubTop;
import com.m2comm.prs2019f.views.Top;
import com.m2comm.prs2019f.views.Voting;

import java.util.HashMap;
import java.util.Random;

public class Globar {

    public String mainUrl = "http://ezv.kr";
    public String baseUrl = "http://ezv.kr/voting/php";
    public String contentMainUrl = "http://knpa.m2comm.co.kr";
    public String contentUrl = "https://www.plasticsurgery.or.kr/workshop/prs2019/app";
    public String code = "PRS2019";
    public static String CONTENT_TITLE = "PRS 2019";
    public String userCodeUrl = "http://121.254.129.104/voting_0523/insert_device.asp";
    public String eventCode = "PRS2019";
    public String voting_ip = "121.254.129.104";
    public int voting_port = 13001;

    public static double default_W = 360;
    public static double default_H = 640;

    public int deviceW = 0;
    public int deviceH = 0;
    public int statusBar = 0;

    private Context a;
    private WindowManager wm;

    private String[] exts = {"pptx", "docx", "doc", "hwp", "xlsx"};
    private String[] imgExts = {"jpeg", "gif", "jpeg", "jpg", "png"};
    public int[] info_color = {
            Color.rgb(230, 242, 255),
            Color.rgb(255, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(254, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(230, 242, 255),
            Color.rgb(255, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255),
            Color.rgb(254, 231, 233),
            Color.rgb(222, 240, 226),
            Color.rgb(229, 242, 255)
    };

    //각 타이틀.
    public TitleDTO[] titles = {

            new TitleDTO("VOTING", "Voting", "문제를 보시고 아래 번호를 선택해 주세요.",
                    null, true, new Voting())
    };

    public String[] votingMessage = {
            "퀴즈가 진행중이 아닙니다.",
            "제출 되었습니다.",
            "이미 제출되었습니다."
    };

    public Globar(Context c) {
        this.a = c;
        this.wh_setting(c);
    }

    public boolean extPDFSearch(String ext) {
        if (ext.contains("pdf")) {
            return true;
        }
        return false;
    }

    public boolean extSearch(String ext) {
        for (String s : exts) {
            if (ext.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean imgExtSearch(String ext) {
        for (String s : imgExts) {
            if (ext.contains(s)) {
                return true;
            }
        }
        return false;
    }



    public void addFragmentTopV (Context c) {
        FragmentManager fm = ((FragmentActivity)c).getSupportFragmentManager();
        Fragment fr = new Top();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentTop,fr);
        fragmentTransaction.commit();
    }

    public void addFragment_Content_TopV (Context c , boolean isSearch) {
        FragmentManager fm = ((FragmentActivity)c).getSupportFragmentManager();
        Fragment fr = new ContentTop();

        Bundle args = new Bundle();
        args.putBoolean("isSearch",isSearch);
        fr.setArguments(args);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_C_Top,fr);

        fragmentTransaction.commit();
    }

    public void addFragment_BottomV (Context c ) {
        FragmentManager fm = ((FragmentActivity)c).getSupportFragmentManager();
        Fragment fr = new Bottom();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_Bottom,fr);
        fragmentTransaction.commit();
    }

    public void addFragment_Content_BottomV (Context c ,boolean isContent ) {
        FragmentManager fm = ((FragmentActivity)c).getSupportFragmentManager();
        Fragment fr = new Bottom();

        Bundle args = new Bundle();
        args.putBoolean("isContent",isContent);
        fr.setArguments(args);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_C_Bottom,fr);
        fragmentTransaction.commit();
    }

    public void addFragment_Sub_TopV (Context c , String title ) {
        FragmentManager fm = ((FragmentActivity)c).getSupportFragmentManager();
        Fragment fr = new SubTop();

        Bundle args = new Bundle();
        args.putString("title",title);
        fr.setArguments(args);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_S_Top,fr);
        fragmentTransaction.commit();
    }


    private DisplayMetrics dM() {
        DisplayMetrics dm = new DisplayMetrics();
        this.wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    private void wh_setting(Context c) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);

        int statusBarHeight = 0;

        int resId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            statusBarHeight = c.getResources().getDimensionPixelSize(resId);
        }

        WindowManager windowManager = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if ( Build.MODEL.equals("SM-G973N") || Build.MODEL.equals("SM-G977N") || Build.MODEL.equals("SM-N971N") ) {
            statusBarHeight = 0;
        }
        this.deviceW = size.x;
        this.deviceH = size.y - statusBarHeight;
    }

    public int x(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int y(int x) {
        return (int) (this.deviceH * (x / default_H));
    }

    public int w(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int h(int x) {
        return (int) (this.deviceH * (x / default_H));
    }

    public int dpToPixel(int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, a.getResources().getDisplayMetrics());
        return px;
    }

    //View get X , Y
    public Point getPointView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    public double setTextSize(int fontize) {
        double size_formatter = (double) fontize / this.deviceW;
        return (int) this.deviceW * size_formatter;
    }

    public void msg(String msg) {
        Toast.makeText(this.a, msg, Toast.LENGTH_SHORT).show();
    }


    public JsonElement getParser(String url) throws Exception {
        HttpConnection hc = new HttpConnection();
        String getData = hc.request(url);
        JsonParser json = new JsonParser();
        return json.parse(getData);
    }

    public String[] mainUrls = {
            "/message1.php", "/session/glance.php", "/session/list.php",
            "/faculty/list.php", "/abstract/category.php", ""
            ,"/location.php","","/booth/list.php"
    };

    public String[][] linkUrl = {
            {"/about/greetings.php","/about/welcome.php"},
            {"/session/glance.php?code="+this.code,"/session/list.php?tab=99&code="+this.code,"/session/list.php?tab=100&code="+this.code},
            {"/abstract/category.php?tab=1","/abstract/category.php?tab=2"},//Faculty
            {"/program/highlights.php"},
            {"/feedback/view.php?title=피드백"},
            {"/about/floor.php","/about/map.php"},
            {"/bbs/list.php?code="+this.code},
            {"/booth/list.php?code="+this.code},
    };

    public HashMap<String, String> urls = new HashMap<String, String>() {
        {
            put("question","/question/post.php");
            put("glance_top","/session/get_set.php?code="+code);
            put("glance", "/session/glance.php");
            put("glance2", "/program/glance.php?tab=12");
            put("now", "/session/list.php?tab=-1&code="+code);
            put("program", "/session/list.php");
            put("schedule", "/program/favorite.php");
            put("search", "/session/list.php?tab=-3&code="+code);
            put("fav","/session/list.php?tab=-2&code="+code);
            put("getNoti", "/bbs/get_list.php");
            put("notiView","/bbs/view.php");
            put("mySchedule", "/session/list.php?tab=-2&code="+code);
            put("lecture_list","http://ezv.kr/voting/php/session/get_session.php?code="+code);

            put("intro", "/banner/list.php?gubun=2&code="+code);//intro
            put("session", "/session/list.php?code="+code);
            put("abstract", "/abstract/list.php?code="+code); //산하연구회 소개
            put("faculty", "/faculty/list.php?code="+code);//산하연구회 회칙
            put("notice", "/bbs/list.php?code="+code);
            put("getPhoto", "/photo.php");
            put("photo","http://knpa.m2comm.co.kr/upload/gallery/");
            put("login", "/login/knpa2019f.php");

            put("photoUpload", "/photo/photo_upload.php");
            put("photoGet", "/photo/get_photo.php");
            put("photoLike", "/photo/set_favor.php");
            put("photoDel", "/photo/del_photo.php");

            put("getPush", "/bbs/get_push.php");
            put("setPush", "/bbs/set_push.php");

            put("setToken", "/token.php");
            put("detailNoti", "/bbs/view.php?code="+code);

            put("boothEvent", "/contents/booth_attend.php");
            put("myMemo", "/session/list.php?tab=-6&code="+code);
            put("MainPhotoTitleList", "/photo/index.php");//MainPhoto title 리스트

            put("researchPhoto", "/research/photo.php");
            put("notiView", "/bbs/view.php");
            put("appInfo", "/app_info.php");
        }
    };

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public String zeroPoint(String data) {
        data = data.trim();
        if (data.length() == 1) {
            data = "0" + data;
        }
        return data;
    }

    //기본알럿창
    public void baseAlertMessage(String subject, String content) {
        new MaterialDialog.Builder(this.a).title(subject)
                .content(content)
                .positiveText("OK").negativeText("Cancle").theme(Theme.LIGHT).show();
    }

    //로그인 Alert
    public void notiAlertMessage(String subject, String content , final Activity activity, final String url) {

        new MaterialDialog.Builder(a).title(subject)
                .content(content)
                .positiveText("OK").negativeText("Cancle").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent(activity, ContentsActivity.class);
                intent.putExtra("paramUrl", url);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.startActivity(intent);

                (activity).finish();
            }

        }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                (activity).finish();
            }
        }).show();
    }


    //로그인 Alert
    public void loginMove(final Activity activity) {
        Intent login = new Intent(a, LoginActivity.class);
        a.startActivity(login);
        activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
    }


    //로그인 Alert
    public void loginAlertMessage(String subject, String content, final Activity activity) {
        new MaterialDialog.Builder(this.a).title(subject)
                .content(content)
                .positiveText("OK").negativeText("Cancle").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent content = new Intent(a, LoginActivity.class);
                a.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                (activity).finish();
            }
        }).show();
    }


}
