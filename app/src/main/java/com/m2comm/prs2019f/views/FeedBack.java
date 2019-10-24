package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentFeedBackBinding;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.viewmodels.FeedBackViewModel;


public class FeedBack extends Fragment {

    FragmentFeedBackBinding binding;

    private WebView wv;
    private Globar g;
    public TitleDTO titleDTO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_back,container,false);
        new FeedBackViewModel(binding,getContext(),this.titleDTO);

        return binding.getRoot();

    }

}
