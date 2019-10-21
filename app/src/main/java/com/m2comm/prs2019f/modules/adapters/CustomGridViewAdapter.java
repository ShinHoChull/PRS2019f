package com.m2comm.prs2019f.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.Globar;


public class CustomGridViewAdapter extends BaseAdapter {

    private Integer[] mainIcon = {
            R.drawable.main_ico_1,
            R.drawable.main_ico_2,
            R.drawable.main_ico_3,
            R.drawable.main_ico_4,
            R.drawable.main_ico_5,
            R.drawable.main_ico_6,
            R.drawable.main_ico_7,
            R.drawable.main_ico_8,
            R.drawable.main_ico_9,
    };

    private String[] names = {
            "인사말 & 환영연",
            "일정",
            "초록보기",
            "Highlights",
            "피드백",
            "대회장 안내",
            "공지사항",
            "전시안내",
            "포토갤러리"
    };

    private Globar g;
    private Context c;
    private LayoutInflater inflater;


    public CustomGridViewAdapter(Context c, LayoutInflater inflater) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.mainIcon.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null ) {
            convertView = this.inflater.inflate(R.layout.main_gridview_child,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.main_gridview_img);
            TextView textView = (TextView)convertView.findViewById(R.id.main_gridview_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.width = this.g.w(110);
            param.height = this.g.h(110);
            convertView.setLayoutParams(param);

            textView.setText(this.names[position]);
            imageView.setImageResource(this.mainIcon[position]);
        }

        return convertView;
    }
}
