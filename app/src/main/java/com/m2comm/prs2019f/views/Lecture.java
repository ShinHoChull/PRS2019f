package com.m2comm.prs2019f.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.model.SideMenuClass;
import com.m2comm.prs2019f.modules.adapters.SideMenuAdapter;
import com.m2comm.prs2019f.modules.common.AnimatedExpandableListView;
import com.m2comm.prs2019f.modules.common.Globar;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class Lecture extends Activity implements View.OnClickListener {

    LectureAdapter lectureAdapter;
    Intent intent;
    AnimatedExpandableListView menu_list;
    SideMenuAdapter sidemenuadapter;
    Globar g;
    String sid;
    int lastClickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        getWindow().setWindowAnimations(0);

        this.g = new Globar(this);
        intent = getIntent();
        this.sid = intent.getStringExtra("sid");
        ImageView close = (ImageView)findViewById(R.id.close);
        close.setOnClickListener(this);

        lectureAdapter = new LectureAdapter(this, R.layout.lecture_item);

        menu_list = (AnimatedExpandableListView) findViewById(R.id.menu_list);
        sidemenuadapter = new SideMenuAdapter(this, R.layout.voting_menu_item);
        menu_list.setAdapter(sidemenuadapter);
        Log.d("Lecture_sidsid",this.sid);
        AndroidNetworking.get(g.urls.get("lecture_list"))
                .addQueryParameter("session_sid",this.sid)
                .setPriority(Priority.LOW)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d("?R??R?R",response);
                jsonPaser(response);
            }

            @Override
            public void onError(ANError anError) {
                Log.d("lecture_error",anError.getErrorDetail());
            }
        });

        menu_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long ids) {

                v.setBackgroundColor(Color.parseColor("#f8f8f8"));
                Boolean isExpand = (!menu_list.isGroupExpanded(groupPosition));
                if (menu_list.isGroupExpanded(lastClickedPosition))
                    menu_list.collapseGroupWithAnimation(lastClickedPosition);
                menu_list.setSelection(groupPosition);
                if (sidemenuadapter.SideMenuList.get(groupPosition).SubSideMenuList == null) {
                    return true;
                }
                if (isExpand) {
                    menu_list.expandGroupWithAnimation(groupPosition);
                    v.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }
                lastClickedPosition = groupPosition;
                menu_list.setSelection(groupPosition);
                return true;
            }
        });

        menu_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long ids) {

                v.setBackgroundColor(Color.parseColor("#aaaaaa"));
                ImageView btn = (ImageView) v.findViewById(R.id.btn);
                btn.setColorFilter(getResources().getColor(R.color.select_border));

                ImageView input_check = (ImageView) v.findViewById(R.id.input_check);
                input_check.setVisibility(View.VISIBLE);
                intent.putExtra("speaker",sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).speaker);
                intent.putExtra("room",sidemenuadapter.getGroup(groupPosition).room);
                intent.putExtra("session_sid",sidemenuadapter.getGroup(groupPosition).sid);
                intent.putExtra("sub_sid",sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).sid);
                intent.putExtra("return",sidemenuadapter.getGroup(groupPosition).SubSideMenuList.get(childPosition).title);
                intent.putExtra("index", 1);
                setResult(RESULT_OK, intent);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        finish();
                    }
                }, 100);

                return false;
            }
        });

    }

    private void jsonPaser(String obj) {
        try {
            JSONArray resultList = new JSONArray((String)obj);

            for(int i=0;i<resultList.length();i++)
            {ArrayList<submenu> subSideMenuClass = new ArrayList<submenu>();
                try {
                    JSONArray subList = resultList.getJSONObject(i).getJSONArray("sub");

                    for(int j=0;j<subList.length();j++)
                    {
                        subSideMenuClass.add(new submenu(subList.getJSONObject(j).getString("title"), subList.getJSONObject(j).getString("sid"),subList.getJSONObject(j).getString("speaker") ));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                sidemenuadapter.add(new SideMenuClass(resultList.getJSONObject(i).getString("theme"), resultList.getJSONObject(i).getString("room"), resultList.getJSONObject(i).getString("sid"),  subSideMenuClass));
            }
            sidemenuadapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"준비중입니다.",Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
    }



    public class submenu {
        public String title;
        public String sid;
        public String speaker;

        submenu(String title, String sid , String speaker)
        {
            this.title = title;
            this.sid = sid;
            this.speaker = speaker;
        }
    }

    public class LectureItem {
        public String name;
        public int type;

        LectureItem(String name, int type)
        {
            this.name = name;
            this.type = type;
        }
    }

    public class LectureAdapter extends ArrayAdapter {

        List lectureList = new ArrayList();
        public LectureAdapter (Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        public void add(LectureItem object) {
            lectureList.add(object);
            super.add(object);
        }


        @Override
        public int getCount() {
            return lectureList.size();
        }

        @Override
        public LectureItem getItem(int index) {
            return (LectureItem) lectureList.get(index);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LectureItem LectureItem = (LectureItem) lectureList.get(position);
            View row = convertView;

            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.lecture_item, parent, false);
            TextView name = (TextView) row.findViewById(R.id.name);
            name.setText(LectureItem.name);
            /*
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.setBackgroundColor(Color.parseColor("#aaaaaa"));
                    LinearLayout btn = (LinearLayout) v.findViewById(R.id.btn);
                    btn.setBackgroundResource(R.drawable.btn_o);
                    intent.putExtra("return", LectureItem. LectureItem.name);
                    intent.putExtra("index", 1);
                    setResult(RESULT_OK, intent);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            finish();
                        }
                    }, 100);
                }
            });
            */
            return row;
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.close :
                finish();
                break;
        }

    }
}
