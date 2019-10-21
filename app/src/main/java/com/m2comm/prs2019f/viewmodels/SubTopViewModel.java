package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentSubTopBinding;
import com.m2comm.prs2019f.modules.common.Globar;

public class SubTopViewModel implements View.OnClickListener {

    private Context c;
    private Globar g;
    private FragmentSubTopBinding activity;
    private String title;

    public SubTopViewModel(FragmentSubTopBinding activity, Context c , String title) {
        this.activity = activity;
        this.c = c;
        this.title = title;
        this.init();
    }

    private void listenerRegister() {
        this.activity.subTopBack.setOnClickListener(this);
    }

    public void init() {
        this.g = new Globar(this.c);
        this.listenerRegister();
        activity.subTopLogo.setText(this.title);

    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.sub_top_back:
                ((Activity) this.c).finish();
                a.overridePendingTransition(R.anim.anim_slide_in_left,0);
                break;
        }
    }
}
