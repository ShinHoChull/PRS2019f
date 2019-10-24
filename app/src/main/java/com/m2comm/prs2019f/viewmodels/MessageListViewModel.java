package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;

import com.m2comm.prs2019f.databinding.FragmentMessageListBinding;

public class MessageListViewModel {

    FragmentMessageListBinding binding;
    Activity activity;
    Context c;


    public MessageListViewModel(FragmentMessageListBinding binding, Activity activity, Context c) {
        this.binding = binding;
        this.activity = activity;
        this.c = c;
        this.init();
    }

    private void init() {

    }
}
