package com.m2comm.prs2019f.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentVotingBinding;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class VotingViewModel implements View.OnTouchListener {

    private FragmentVotingBinding activity;
    private Globar g;
    private Context c;
    private TitleDTO titleDTO;

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    private Custom_SharedPreferences csp;


    public VotingViewModel(FragmentVotingBinding activity , Context c , TitleDTO titleDTO) {
        this.activity = activity;
        this.c = c;
        this.titleDTO = titleDTO;
        this.init();
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.listenerRegster();
        this.activity.votingSubText.setText(this.titleDTO.getSubTitle());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listenerRegster() {
        this.activity.votingBt1.setOnTouchListener(this);
        this.activity.votingBt2.setOnTouchListener(this);
        this.activity.votingBt3.setOnTouchListener(this);
        this.activity.votingBt4.setOnTouchListener(this);
        this.activity.votingBt5.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button b = (Button) v.findViewById(v.getId());
        final String num = String.valueOf(b.getText()).replace("번", "");
        Log.d("NumNum",num);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                v.setBackgroundResource(R.drawable.voting_button_click_radius);
                break;

            case MotionEvent.ACTION_UP:
                v.setBackgroundResource(R.drawable.voting_button_radius);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            setSocket(num);
                        } catch (IOException e) {
                            Log.e("socket_error", e.toString());
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
        }
        return true;
    }

    public void setSocket(String num) throws IOException {

        //소켓 연결 안될때 처리하기
        this.socket = new Socket(this.g.voting_ip, this.g.voting_port);
        os = this.socket.getOutputStream();
        osw = new OutputStreamWriter(os);
        bw = new BufferedWriter(osw);

        is = this.socket.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);

        String msg = "<voting><request><device_type>user</device_type><type>quiz_result</type>" +
                "<id>"+ csp.getValue("deviceid","")
                +"</id><lectureid>0</lectureid><quizid>0</quizid><selected>"
                + num +"</selected></request></voting>";
        Log.d("votingMsg",msg);

        String no_padding = Base64.encodeToString(msg.getBytes(), Base64.NO_PADDING);
        no_padding = no_padding.replace("\n", "");
        no_padding = no_padding.replace("\r", "");

        bw.write(no_padding);
        bw.flush();

        String s = new String(Base64.decode(br.readLine().getBytes(), 0));
        int code = 0; // 0 => errorCode
        if (s.contains("already")) {
            code = 2;
        } else if (s.contains("successfully")) {
            code = 1;
        }
        handler.sendEmptyMessage(code);

        bw.close();
        osw.close();
        os.close();
        br.close();
        isr.close();
        is.close();
        socket.close();

    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(c,g.votingMessage[msg.what],Toast.LENGTH_SHORT).show();
        }
    };

}
