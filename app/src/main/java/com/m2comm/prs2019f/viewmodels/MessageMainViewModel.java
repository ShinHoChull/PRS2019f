package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;

import com.m2comm.prs2019f.databinding.FragmentMessageMainBinding;
import com.m2comm.prs2019f.modules.common.Globar;

public class MessageMainViewModel {

    FragmentMessageMainBinding binding;
    Activity activity;
    private Context c;
    private Globar g;


    public MessageMainViewModel(FragmentMessageMainBinding binding, Activity activity, Context c) {
        this.binding = binding;
        this.activity = activity;
        this.c = c;
        this.init();
    }

    private void init () {
        this.g = new Globar(this.c);



    }



}
