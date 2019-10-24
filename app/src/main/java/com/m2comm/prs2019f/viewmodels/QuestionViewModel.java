package com.m2comm.prs2019f.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.databinding.FragmentQuestionBinding;
import com.m2comm.prs2019f.model.QuestionModel;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.m2comm.prs2019f.views.Lecture;


public class QuestionViewModel implements View.OnClickListener{

    private FragmentQuestionBinding binding;
    private Custom_SharedPreferences csp;
    private TitleDTO titleDTO;
    private Context c;
    private Globar g;
    private Activity a;
    private QuestionModel model;

    boolean isCheck=true;
    String room="";
    String sid = "";
    String name = "";
    String lecture = "";
    String session_sid = "";
    String sub_sid = "";

    public QuestionViewModel(FragmentQuestionBinding binding , Context c , TitleDTO titleDTO , Activity a) {
        this.binding = binding;
        this.c = c;
        this.a = a;
        this.titleDTO = titleDTO;
        this.init();
    }

    public QuestionViewModel(FragmentQuestionBinding binding , Context c , TitleDTO titleDTO , Activity a , String sid) {
        this.binding = binding;
        this.c = c;
        this.a = a;
        this.titleDTO = titleDTO;
        this.sid = sid;
        this.init();
    }

    private void init () {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.model = new QuestionModel();
        this.binding.qaSubText.setText(this.titleDTO.getSubTitle());
        this.binding.questionSendBt.setOnClickListener(this);
        this.binding.questionSelectboxBt.setOnClickListener(this);

        Log.d("questionSid",this.sid);
    }

    public void resultSetting(String room , String returnV , String speaker , String session_sid , String sub_sid) {

        this.binding.questionSelectboxBt.setText(returnV);
        this.room = room;
        this.lecture = returnV;
        this.name = speaker;
        this.session_sid = session_sid;
        this.sub_sid = sub_sid;
        if (csp.getValue("isSelect",false)) {
            this.sid = session_sid;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.question_selectboxBt:

                Intent intent = new Intent(this.c, Lecture.class);
                intent.putExtra("sid",this.sid);
                this.a.startActivityForResult(intent,0);

                break;

            case R.id.question_sendBt:

                if (this.binding.questionEdittext.getText().toString().equals("")) {
                    Toast.makeText(this.c, this.model.getQuestionMessage(), Toast.LENGTH_SHORT).show();
                    return;
                } else if (room.equals("")) {
                    Toast.makeText(this.c, "Please select a session speaker.", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                }
                this.isCheck = false;
                AndroidNetworking.post(this.g.baseUrl + this.g.urls.get("question"))
                        .addBodyParameter("question",binding.questionEdittext.getText().toString())
                        .addBodyParameter("room",this.room)
                        .addBodyParameter("mobile","Y")
                        .addBodyParameter("code",g.code)
                        .addBodyParameter("lecture",this.lecture)
                        .addBodyParameter("id",csp.getValue("deviceid",""))
                        .addBodyParameter("name",csp.getValue("name",this.name))
                        .addBodyParameter("job",csp.getValue("job",""))
                        .addBodyParameter("license",csp.getValue("license",""))
                        .addBodyParameter("session",this.session_sid)
                        .addBodyParameter("sub",this.sub_sid)
                        .setPriority(Priority.LOW)
                        .build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        isCheck = true;
                        binding.questionEdittext.setText("");
                        Toast.makeText(c, "Has been sent.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("question",anError.getErrorDetail());
                    }
                });
                break;
        }
    }
}
