package com.m2comm.prs2019f.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView selectBox , send;
    private EditText editText;
    private ImageView closeBt;
    String room="";
    private Globar g;
    private Custom_SharedPreferences csp;
    boolean isCheck=true;
    String sid = "";
    String name = "";
    String lecture = "";
    String session_sid = "";
    String sub_sid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);
        this.selectBox = findViewById(R.id.question_selectboxBt);
        this.editText = findViewById(R.id.question_edittext);
        this.send = findViewById(R.id.question_sendbt);
        this.closeBt = findViewById(R.id.question_close);

        this.selectBox.setOnClickListener(this);
        this.send.setOnClickListener(this);
        this.closeBt.setOnClickListener(this);


        Intent intent = new Intent(this.getIntent());
        this.sid = "";//intent.getStringExtra("sid");//String.valueOf(intent.getIntExtra("sid",0));
        Log.d("questionActivity",this.sid);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.question_selectboxBt:

                Intent intent = new Intent(this, Lecture.class);
                intent.putExtra("sid",this.sid);
                startActivityForResult(intent,0);
                break;

            case R.id.question_sendbt:
                this.sendQuestion();
                break;

            case R.id.question_close:
                finish();
                overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
                break;
        }
    }

    private void sendQuestion() {
        if ( this.editText.getText().toString().equals("") ) {
            Toast.makeText(this, "질문을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (room.equals("")) {
            Toast.makeText(this, "세션 연자를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isCheck) {
            return;
        }

        this.isCheck = false;
        AndroidNetworking.post(this.g.baseUrl + this.g.urls.get("question"))
                .addBodyParameter("question",this.editText.getText().toString())
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
                editText.setText("");
                Toast.makeText(getApplicationContext(), "전송되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError)
            {
                isCheck = true;
                Toast.makeText(getApplicationContext(), "전송이 실패되었습니다.", Toast.LENGTH_SHORT).show();
                Log.d("question",anError.getErrorDetail());
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v == null) return super.dispatchTouchEvent(event);
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selectBox.setText(data.getStringExtra("return"));
            room = data.getStringExtra("room");
            this.lecture = data.getStringExtra("return");
            this.name = data.getStringExtra("speaker");
            this.session_sid = data.getStringExtra("session_sid");
            this.sub_sid = data.getStringExtra("sub_sid");
        }
    }


}
