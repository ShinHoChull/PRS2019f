package com.m2comm.prs2019f.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.adapters.MainBottomAdapter;
import com.m2comm.prs2019f.modules.common.Globar;

public class VotingActivity extends AppCompatActivity implements View.OnClickListener , AdapterView.OnItemClickListener {

    private Globar g;
    private TextView main_title;
    private FrameLayout framelayout;
    private ImageView closeBt;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;
    private Fragment fr = null;
    private MainBottomAdapter mba;
    private GridView mainGridview;

    int clickPos = -1;
    int defaultPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        this.init();

        fragmentChage(this.defaultPosition);
        buttonReset(this.defaultPosition);

    }

    private void init () {
        this.g = new Globar(this);
        this.fm = getSupportFragmentManager();

        this.main_title = findViewById(R.id.Main_Title);
        this.framelayout = findViewById(R.id.fragmentBor);
        this.closeBt = findViewById(R.id.voting_closeBt);
        this.closeBt.setOnClickListener(this);
        this.mainGridview = findViewById(R.id.main_gridview);
        this.mainGridview.setOnItemClickListener(this);

        this.mba = new MainBottomAdapter(this,this.getLayoutInflater());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.fragmentChage(position);
        this.buttonReset(position);
    }

    //View 재설정
    private void fragmentChage(int position) {

        if ( this.clickPos == position ) return;

        TitleDTO r = this.g.titles[position];
        this.main_title.setText(r.getMainTitle());

        if (fr != null) {
            fm.beginTransaction().remove(fr).commit();
        }
        fr = r.getFragment();
        if (fr.getClass() == Voting.class) {
            ((Voting)fr).titleDTO = r;
        } else if (fr.getClass() == Question.class) {
            ((Question)fr).titleDTO = r;
        } else if (fr.getClass() == FeedBack.class) {
            ((FeedBack)fr).titleDTO = r;
        }

        this.fragmentTransaction = fm.beginTransaction();
        this.fragmentTransaction.add(R.id.fragmentBor, fr);
        this.fragmentTransaction.commit();

        this.clickPos = position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voting_closeBt:
                finish();
                overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
                break;
        }


    }

    private void buttonReset(int postion) {
        //Adapter
        this.mba.notifyDataSetChanged();
        this.mba.clickPosition = postion;
        this.mainGridview.setAdapter(mba);
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
}
