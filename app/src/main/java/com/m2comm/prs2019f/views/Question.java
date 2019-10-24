package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentQuestionBinding;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.viewmodels.QuestionViewModel;


public class Question extends Fragment {

    private FragmentQuestionBinding binding;
    public TitleDTO titleDTO;
    private QuestionViewModel qvm;
    private Custom_SharedPreferences csp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question,container,false);
        this.csp = new Custom_SharedPreferences(getContext());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (  getArguments().getString("sid") != null && !getArguments().getString("sid").equals("") ) {
            this.csp.put("isSelect",true);
            this.qvm = new QuestionViewModel(binding, getContext(),this.titleDTO , getActivity() , getArguments().getString("sid"));
        } else {
            this.qvm = new QuestionViewModel(binding, getContext(),this.titleDTO , getActivity());
        }

        if ( getArguments().getBoolean("isResult") ) {
            this.qvm.resultSetting(getArguments().getString("room",""),
                    getArguments().getString("return",""),
                    getArguments().getString("speaker",""),
                    getArguments().getString("session_sid",""),
                    getArguments().getString("sub_sid",""));
        }

    }

}
