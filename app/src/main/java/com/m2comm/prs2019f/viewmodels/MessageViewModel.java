package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.ActivityMessageBinding;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.Message_list_Fragment;
import com.m2comm.prs2019f.views.Message_main_Fragment;

public class MessageViewModel implements View.OnClickListener {

    ActivityMessageBinding binding;
    Context c;
    Activity activity;
    Custom_SharedPreferences csp;
    Globar g;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;
    private Fragment fr = null;

    private int defaultBt = 0;


    private void registObjs() {
        this.binding.messageList.setOnClickListener(this);
        this.binding.messageMs.setOnClickListener(this);
    }

    public MessageViewModel(ActivityMessageBinding binding, Context c, Activity aCtivity , FragmentManager fm) {
        this.binding = binding;
        this.c = c;
        this.activity = aCtivity;
        this.fm = fm;
        this.init();
    }

    private void init() {
        this.csp = new Custom_SharedPreferences(this.c);
        this.g = new Globar(this.c);
        this.registObjs();
        this.buttonChange(this.defaultBt);
        this.fragmentChange(this.defaultBt);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.message_list:
                this.buttonChange(0);
                this.fragmentChange(0);
                break;

            case R.id.message_ms:
                this.buttonChange(1);
                this.fragmentChange(1);
                break;

        }
    }

    private void buttonChange(int tab) {
        if ( tab == 0 ) {
            this.binding.messageList.setTextColor(this.c.getResources().getColor(R.color.main_color_white));
            this.binding.messageList.setBackgroundColor(Color.parseColor("#513492"));

            this.binding.messageMs.setTextColor(this.c.getResources().getColor(R.color.main_color_black));
            this.binding.messageMs.setBackgroundColor(Color.parseColor("#F6F6F6"));
        } else {
            this.binding.messageMs.setTextColor(this.c.getResources().getColor(R.color.main_color_white));
            this.binding.messageMs.setBackgroundColor(Color.parseColor("#513492"));

            this.binding.messageList.setTextColor(this.c.getResources().getColor(R.color.main_color_black));
            this.binding.messageList.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }
    }

    private void fragmentChange(int tab) {


        if (fr != null) {
            fm.beginTransaction().remove(fr).commit();
        }
        if (tab == 0) {
            fr = new Message_main_Fragment();
        } else {
            fr = new Message_list_Fragment();
        }

        this.fragmentTransaction = fm.beginTransaction();
        this.fragmentTransaction.add(R.id.messageFragment, fr);
        this.fragmentTransaction.commit();


    }

}
