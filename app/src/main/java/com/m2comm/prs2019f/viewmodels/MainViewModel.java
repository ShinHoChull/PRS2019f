package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityMainBinding;
import com.m2comm.prs2019f.model.MessageDTO;
import com.m2comm.prs2019f.modules.adapters.CustomGridViewAdapter;
import com.m2comm.prs2019f.modules.common.CustomHandler;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.ContentsActivity;
import com.m2comm.prs2019f.views.GlanceActivity;
import com.m2comm.prs2019f.views.PhotoActivity;
import com.m2comm.prs2019f.views.QuestionActivity;
import com.m2comm.prs2019f.views.VotingActivity;

import java.util.Arrays;

public class MainViewModel implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ActivityMainBinding binding;
    private Context c;
    private Activity activity;
    private Globar g;
    private Custom_SharedPreferences csp;
    private CustomHandler customHandler;
    private String notiStr;
    private int newNotiSid;

    private void listenerRegister() {
        this.binding.mainGrid.setOnItemClickListener(this);
        this.binding.mainNoticeV.setOnClickListener(this);
        this.binding.mainVoting.setOnClickListener(this);
    }

    public MainViewModel(ActivityMainBinding binding, Context c, Activity activity) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
        this.g.addFragmentTopV(this.c);
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.customHandler = new CustomHandler(this.c);
        this.g.addFragment_BottomV(this.c);
        this.listenerRegister();

        LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomGridViewAdapter cga = new CustomGridViewAdapter(this.c, inflater);
        this.binding.mainGrid.setAdapter(cga);

        AndroidNetworking.get(g.baseUrl + g.urls.get("getNoti"))
                .addQueryParameter("deviceid", csp.getValue("deviceid", ""))
                .addQueryParameter("code", g.code)
                .setPriority(Priority.LOW)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(response);
                JsonArray ja = je.getAsJsonArray();
                for (int i = 0 , j = ja.size(); i < j; i++) {
                    JsonObject jsonObject = ja.get(i).getAsJsonObject();
                    notiStr = jsonObject.get("subject").getAsString();
                    newNotiSid = jsonObject.get("sid").getAsInt();
                }

                Message msg = customHandler.obtainMessage();
                msg.what = CustomHandler.MAIN_NOTICE;
                customHandler.sendMessage(msg);
            }

            @Override
            public void onError(ANError anError) {
                Message msg = customHandler.obtainMessage();
                msg.obj = new MessageDTO("Failed to fetch data.(Main Noti Error)",
                        anError.toString());
                msg.what = CustomHandler.ALERT_WINDOW_CODE;
                customHandler.sendMessage(msg);
            }
        });

        FirebaseInstanceId.getInstance().getToken();
        this.binding.mainVoting.setY(this.g.y(95));
    }

    public void reSetting() {

        AndroidNetworking.get("http://ezv.kr/knpa/knpa.txt")
                .addQueryParameter("deviceid", csp.getValue("deviceid", ""))
                .addQueryParameter("code", g.code)
                .setPriority(Priority.LOW)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                if ( response.toUpperCase().equals("Y") ) {
                    binding.mainVoting.setVisibility(View.VISIBLE);
                } else {
                    binding.mainVoting.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(ANError anError) {
                binding.mainVoting.setVisibility(View.GONE);
            }
        });
    }

    public void tokenResume() {
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            csp.put("token", FirebaseInstanceId.getInstance().getToken());
            new Thread() {
                @Override
                public void run() {
                    super.run();

                    Message msg = customHandler.obtainMessage();
                    try {
                        g.getParser(g.baseUrl + g.urls.get("setToken") + "?deviceid=" + csp.getValue("deviceid", "") +
                                "&device=android&code=" + g.code + "&token=" + FirebaseInstanceId.getInstance().getToken());

                    } catch (Exception e) {
                        msg.obj = new MessageDTO("Failed to fetch data.(Token Error)",
                                e.toString());
                        msg.what = CustomHandler.ALERT_WINDOW_CODE;
                        customHandler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }


    public void notiChange() {
        this.binding.mainNotiTextV.setText(this.notiStr);
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity) c;
        switch (v.getId()) {
            case R.id.main_noticeV:
                Intent content = new Intent(c, ContentsActivity.class);
                content.putExtra("paramUrl", this.g.urls.get("notiView") + "?sid="+this.newNotiSid);
                c.startActivity(content);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.main_voting:

                Intent voting = new Intent(c, QuestionActivity.class);
                c.startActivity(voting);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                return;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity a = (Activity) c;
        Intent content = new Intent(c, ContentsActivity.class);
        int[] loginArr = {2, 4, 8};
//        if (Arrays.binarySearch(loginArr , position) >= 0 ) {
//            if ( !this.csp.getValue("isLogin",false)) {
//                this.g.loginMove(this.activity);
//                return;
//            }
//        }

        if ( position == 0 || position == 6 ) {

            content.putExtra("content", true);

        } else if ( position == 5 ) {
            Intent photo = new Intent(this.c, PhotoActivity.class);
            photo.putExtra("choice","99");
            this.c.startActivity(photo);
            return;

        } else if ( position == 1 ) {
            Intent bell = new Intent(c, GlanceActivity.class);
            a.startActivity(bell);
            a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

            return;

        } else if (position == 7) {
            Intent voting = new Intent(c , VotingActivity.class);
            a.startActivity(voting);

            return;
        }

        content.putExtra("paramUrl", this.g.mainUrls[position]);
        c.startActivity(content);
        a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }
}
