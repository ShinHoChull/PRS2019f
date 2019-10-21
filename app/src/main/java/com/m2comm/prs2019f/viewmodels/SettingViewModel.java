package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.m2comm.prs2019f.BuildConfig;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivitySettingBinding;
import com.m2comm.prs2019f.model.MessageDTO;
import com.m2comm.prs2019f.modules.common.CustomHandler;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;

public class SettingViewModel implements View.OnClickListener {


    ActivitySettingBinding binding;
    private Activity activity;
    private Globar g;
    private Context c;
    private Custom_SharedPreferences csp;
    private CustomHandler customHandler;
    private String push;


    public SettingViewModel(ActivitySettingBinding binding , Context c , Activity activity ) {
        this.binding = binding;
        this.activity = activity;
        this.c = c;
        this.init();
    }

    private void init () {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.customHandler = new CustomHandler(this.c);

        this.g.addFragment_Content_TopV(this.c , false);
        this.g.addFragment_Sub_TopV(this.c , "설정");
        this.listenerRegister();

        this.getPush();

        this.binding.versionName.setText("V"+ BuildConfig.VERSION_NAME);
    }

    private void listenerRegister() {
        //this.activity.settingLogout.setOnClickListener(this);
        this.binding.settingOp1.setOnClickListener(this);
    }

    public void getPush () {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {

                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("getPush")+"?deviceid="+csp.getValue("deviceid","")+
                            "&code="+g.code);

                    Log.d("settingJE",je.getAsString());
                    push = je.getAsString();
                    msg.obj = je.getAsString();
                    msg.what = CustomHandler.SETTING_GET_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {

                    msg.obj = new MessageDTO("Failed to fetch data.(Setting Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void setPush() {
        if (this.push.equals("Y")) {
            this.push = "N";
        } else {
            this.push = "Y";
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl+g.urls.get("setPush")+"?deviceid="+csp.getValue("deviceid","")+
                            "&code="+g.code+"&val="+push);

                    push = je.getAsString();
                    msg.obj = je.getAsString();
                    msg.what = CustomHandler.SETTING_GET_CODE;
                    customHandler.sendMessage(msg);

                } catch (Exception e) {

                    msg.obj = new MessageDTO("Failed to fetch data.(Setting Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();

    }


    public void settingChange (String val) {
        Log.d("settingJEMeth",val);
        if (val.equals("Y")) {
            this.binding.settingOp1.setChecked(true);
        } else {
            this.binding.settingOp1.setChecked(false);
        }
    }


    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {
            case R.id.setting_op1:
                setPush();
                break;

//            case R.id.setting_logout :
//                this.csp.remove("userid");
//                this.csp.remove("reg_num");
//                this.csp.remove("isLogin");
//                this.g.msg("You have been signed out.");
//
//                Intent content = new Intent(c, MainActivity.class);
//                content.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                content.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                a.startActivity(content);
//                a.finish();
//                a.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
//                break;
//            case R.id.setting_op1:
//                setPush();
//                break;
        }

    }
}
