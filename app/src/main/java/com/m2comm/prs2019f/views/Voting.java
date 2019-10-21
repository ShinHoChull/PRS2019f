package com.m2comm.prs2019f.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentVotingBinding;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.viewmodels.VotingViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Voting extends Fragment {

    private Globar g;
    public TitleDTO titleDTO;

    private TextView subTitle;
    private Button bt1, bt2, bt3, bt4, bt5;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;

    FragmentVotingBinding binding;

    public Voting() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting,container,false);
        new VotingViewModel(binding,getContext(),this.titleDTO);
        return binding.getRoot();
    }

//    public static String getStringFromInputStream(InputStream stream) throws IOException
//    {
//        int n = 0;
//        char[] buffer = new char[1024 * 4];
//        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
//        StringWriter writer = new StringWriter();
//        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
//        return Base64.decode(writer.toString(),0);
//    }

}
