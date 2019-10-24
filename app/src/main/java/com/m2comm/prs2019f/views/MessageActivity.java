package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityMessageBinding;
import com.m2comm.prs2019f.viewmodels.MessageViewModel;

public class MessageActivity extends AppCompatActivity {


    ActivityMessageBinding binding;
    MessageViewModel mvm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_message);
        this.binding.setMessage(this);
        this.mvm = new MessageViewModel(this.binding , this , this , getSupportFragmentManager());
    }
}
