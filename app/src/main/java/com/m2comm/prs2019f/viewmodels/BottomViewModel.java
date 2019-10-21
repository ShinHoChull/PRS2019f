package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentBottomBinding;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.ContentsActivity;
import com.m2comm.prs2019f.views.GlanceActivity;
import com.m2comm.prs2019f.views.MainActivity;

public class BottomViewModel implements View.OnClickListener {

    private Context c;
    private Globar g;
    private FragmentBottomBinding binding;

    public BottomViewModel(FragmentBottomBinding binding, Context c ) {
        this.binding = binding;
        this.c = c;
        this.init();
    }

    private void listenerRegister() {
        this.binding.home.setOnClickListener(this);
        this.binding.search.setOnClickListener(this);
        this.binding.now.setOnClickListener(this);
        this.binding.fav.setOnClickListener(this);
        this.binding.program.setOnClickListener(this);
    }

    public void init() {
        this.g = new Globar(this.c);
        this.listenerRegister();
    }

    @Override
    public void onClick(View v) {
        Activity a = (Activity)this.c;
        switch (v.getId()) {

            case R.id.home:
                Intent content = new Intent(c, MainActivity.class);
                content.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                a.startActivity(content);
                break;

            case R.id.program:

                Intent bell = new Intent(c, GlanceActivity.class);
                a.startActivity(bell);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

//                Intent bell = new Intent(c, ContentsActivity.class);
//                bell.putExtra("paramUrl", this.g.urls.get("program"));
//                a.startActivity(bell);
//                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.now:
                Intent now = new Intent(this.c, ContentsActivity.class);
                now.putExtra("paramUrl", this.g.urls.get("now"));
                a.startActivity(now);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.search:
                Intent search = new Intent(this.c, ContentsActivity.class);
                search.putExtra("paramUrl", this.g.urls.get("search"));
                a.startActivity(search);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.fav:
                Intent fav = new Intent(this.c, ContentsActivity.class);
                fav.putExtra("paramUrl", this.g.urls.get("mySchedule"));
                a.startActivity(fav);
                a.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }
    }
}
