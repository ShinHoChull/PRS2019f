package com.m2comm.prs2019f.modules.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.m2comm.prs2019f.model.MessageListDTO;

import java.util.ArrayList;

public class MessageListAdapter extends BaseAdapter {

    private ArrayList<MessageListDTO> listDTOS = new ArrayList<>();
    private Activity activity;
    private Context c;

    public MessageListAdapter(ArrayList<MessageListDTO> listDTOS, Activity activity, Context c) {
        this.listDTOS = listDTOS;
        this.activity = activity;
        this.c = c;
    }

    @Override
    public int getCount() {
        return 0;
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
        return null;
    }

}
