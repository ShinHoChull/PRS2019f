package com.m2comm.prs2019f.model;

import android.widget.TextView;

public class TypeOneDTO {
    TextView tv;
    String tab;

    public TypeOneDTO(TextView tv, String tab) {
        this.tv = tv;
        this.tab = tab;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public TextView getTv() {
        return tv;
    }

    public String getTab() {
        return tab;
    }
}
