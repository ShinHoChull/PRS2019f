package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;

import com.m2comm.prs2019f.databinding.FragmentMessageMainBinding;

public class MessageMainViewModel {

    FragmentMessageMainBinding binding;
    Activity activity;
    Context c;


    public MessageMainViewModel(FragmentMessageMainBinding binding, Activity activity, Context c) {
        this.binding = binding;
        this.activity = activity;
        this.c = c;
    }



}
