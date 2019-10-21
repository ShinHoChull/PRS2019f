package com.m2comm.prs2019f.model;

public class Glance_sub_top {
    private String tab;
    private String name;
    private String week;

    public Glance_sub_top(String tab, String name, String week) {
        this.tab = tab;
        this.name = name;
        this.week = week;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTab() {
        return tab;
    }

    public String getName() {
        return name;
    }

    public String getWeek() {
        return week;
    }
}
