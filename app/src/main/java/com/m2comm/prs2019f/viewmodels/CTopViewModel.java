package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentContentTopBinding;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.ContentsActivity;
import com.m2comm.prs2019f.views.MainActivity;
import com.m2comm.prs2019f.views.MenuActivity;

public class CTopViewModel implements View.OnClickListener {

    private Context c;
    private Globar g;
    private FragmentContentTopBinding activity;
    private boolean isPhoto;

    public CTopViewModel(FragmentContentTopBinding activity, Context c , boolean isPhoto) {
        this.activity = activity;
        this.c = c;
        this.isPhoto = isPhoto;
        this.init();
    }

    private void listenerRegister() {
        this.activity.ctopMenu.setOnClickListener(this);
        this.activity.ctopLogo.setOnClickListener(this);
        this.activity.topSearch.setOnClickListener(this);
    }

    public void init() {
        this.g = new Globar(this.c);
        this.listenerRegister();
        this.activity.ctopLogo.setText( Globar.CONTENT_TITLE );
        this.activity.topSearch.setVisibility(View.GONE);
        if (! this.isPhoto) {
            this.activity.topSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.ctop_menu:
                Intent menuCall= new Intent(this.c, MenuActivity.class);
                menuCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(menuCall);
                a.overridePendingTransition(R.anim.anim_slide_in_left,0);
                break;

            case R.id.ctop_logo:
                Intent content = new Intent(c, MainActivity.class);
                content.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(content);

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
