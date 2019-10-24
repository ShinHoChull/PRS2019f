package com.m2comm.prs2019f.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.model.TitleDTO;
import com.m2comm.prs2019f.modules.common.Globar;


public class MainBottomAdapter extends BaseAdapter {

    private Globar g;
    private Context c;
    private LayoutInflater inflater;
    public int clickPosition;

    public MainBottomAdapter(Context c, LayoutInflater inflater) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.g.titles.length;
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

        if (convertView == null) {

            TitleDTO r = this.g.titles[position];
            convertView = this.inflater.inflate(R.layout.bottom_grid_item, parent, false);
            TextView textView = convertView.findViewById(R.id.bottom_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if (param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.height = this.g.h(60);
            convertView.setLayoutParams(param);
            textView.setText(r.getBottomTitle());
            if (this.clickPosition == position)
                textView.setBackgroundColor(this.g.bottomBt_Click_Color);
            else
                textView.setBackgroundColor(this.g.bottomBt_Default_Color);
        }

        return convertView;
    }


}
