package com.m2comm.prs2019f.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityLoginBinding;
import com.m2comm.prs2019f.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;
    LoginViewModel lvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        this.binding.setLogin(this);
        this.lvm = new LoginViewModel(this.binding , this , this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v == null) return super.dispatchTouchEvent(event);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }

    public void loginSuccess(String val) {
        this.lvm.loginSuccess(val);
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
    }
}
