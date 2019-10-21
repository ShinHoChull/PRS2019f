package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentTopBinding;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.ContentsActivity;
import com.m2comm.prs2019f.views.GlanceActivity;
import com.m2comm.prs2019f.views.MenuActivity;

public class TopViewModel implements View.OnClickListener {

    private Context c;
    private Globar g;
    private FragmentTopBinding activity;
    private Custom_SharedPreferences csp;

    public TopViewModel(FragmentTopBinding activity, Context c) {
        this.activity = activity;
        this.c = c;
        this.init();
    }

    private void listenerRegister() {
        this.activity.topMenu.setOnClickListener(this);
        this.activity.topLogo.setOnClickListener(this);
        this.activity.topSearch.setOnClickListener(this);
        this.activity.topCal.setOnClickListener(this);
    }

    public void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.listenerRegister();
        
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.top_menu:
                Intent menuCall= new Intent(this.c, MenuActivity.class);
                menuCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(menuCall);
                a.overridePendingTransition(R.anim.anim_slide_in_left,0);
                break;

            case R.id.top_logo:

                break;

            case R.id.top_cal:
                Intent glance = new Intent(this.c, GlanceActivity.class);
                a.startActivity(glance);
                break;


            case R.id.top_search:
                Intent search = new Intent(this.c, ContentsActivity.class);
                search.putExtra("paramUrl", this.g.urls.get("search"));
                a.startActivity(search);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }

    }
}
