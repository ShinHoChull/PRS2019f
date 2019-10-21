package com.m2comm.prs2019f.views;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentBottomBinding;
import com.m2comm.prs2019f.viewmodels.BottomViewModel;


public class Bottom extends Fragment {


    FragmentBottomBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setWindowAnimations(0);
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom,container,false);
        new BottomViewModel(this.binding,getContext());
        return this.binding.getRoot();
    }
}
