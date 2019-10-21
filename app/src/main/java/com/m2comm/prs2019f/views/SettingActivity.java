package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivitySettingBinding;
import com.m2comm.prs2019f.viewmodels.SettingViewModel;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SettingViewModel svm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        this.binding.setSetting(this);
        this.svm = new SettingViewModel(this.binding , this , this);



    }


    public void settingChange(String val) {
        this.svm.settingChange(val);
    }






}
