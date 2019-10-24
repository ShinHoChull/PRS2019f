package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityNewGlanceBinding;
import com.m2comm.prs2019f.viewmodels.NewGlanceViewModel;


public class New_GlanceActivity extends AppCompatActivity {

    ActivityNewGlanceBinding binding;
    NewGlanceViewModel ngvm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_new_glance);
        this.binding.setNewGlance(this);
        this.ngvm = new NewGlanceViewModel(this.binding , this , this);
    }
}
