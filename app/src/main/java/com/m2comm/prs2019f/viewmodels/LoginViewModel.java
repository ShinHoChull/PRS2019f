package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityLoginBinding;
import com.m2comm.prs2019f.model.LoginDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.MainActivity;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class LoginViewModel implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private Activity activity;
    private Context c;
    private Globar g;
    private Custom_SharedPreferences csp;

    private void listenerRegister() {
        this.binding.loginCloseBt.setOnClickListener(this);
        this.binding.lgoinLoginBt.setOnClickListener(this);
    }

    public LoginViewModel(ActivityLoginBinding binding , Context c ,Activity activity) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.listenerRegister();
    }

    public void login () {

        if ( this.binding.loginName.getText().toString().equals("") ) {
            this.g.baseAlertMessage("Alert","성함을 입력하여 주세요.");
        } else if ( this.binding.loginPw.getText().toString().equals("") ) {
            this.g.baseAlertMessage("Alert","면허번호를 입력하여 주세요.");
        } else {
            AndroidNetworking.post(g.baseUrl+g.urls.get("login"))
                    .addQueryParameter("deviceid", csp.getValue("deviceid",""))
                    .addQueryParameter("code", g.code)
                    .addQueryParameter("name", binding.loginName.getText().toString())
                    .addQueryParameter("license_number",binding.loginPw.getText().toString())
                    .setPriority(Priority.LOW)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("object_s","+"+response);
                    try {
                        if ( response.getString("rows").equals("1") ) {
                            JSONObject obj = response.getJSONObject("data");
                            csp.put("sid",obj.getString("regist_sid"));
                            csp.put("name",obj.getString("name"));
                            csp.put("isLogin",true);

                            Intent login = new Intent(c, MainActivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            c.startActivity(login);

                        } else {
                            g.baseAlertMessage("알림","정보를 확인해주세요.");
                        }
                    } catch (Exception e) {
                        g.baseAlertMessage("Error",e.toString());
                    }
                }

                @Override
                public void onError(ANError anError) {
                    g.baseAlertMessage("Error",anError.getErrorDetail());
                }
            });
        }
    }

    public void loginSuccess(String val) {
        Activity a = (Activity)c;

        if (val.contains("인증된")) {
            this.g.baseAlertMessage("Alert",val);
        } else {
            Type listType = new TypeToken<LoginDTO>() {
            }.getType();
            Gson gson = new Gson();
            LoginDTO r = gson.fromJson(val, listType);
            csp.put("sid",r.getSid());
            csp.put("isLogin",true);
            Intent content = new Intent(c, MainActivity.class);
            content.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            content.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.startActivity(content);
            a.finish();
            activity.finish();
        }
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {
            case R.id.lgoin_loginBt:
                this.login();
                break;
            case R.id.login_closeBt:
                a.finish();
                a.overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
                break;
        }

    }
}
