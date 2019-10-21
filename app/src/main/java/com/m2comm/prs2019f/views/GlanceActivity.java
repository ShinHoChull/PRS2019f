package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityGlanceBinding;
import com.m2comm.prs2019f.viewmodels.GlanceViewModel;

public class GlanceActivity extends AppCompatActivity {

    ActivityGlanceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(0);
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_glance);
        this.binding.setGlance(this);
        new GlanceViewModel(this.binding , this , this);

    }
}
