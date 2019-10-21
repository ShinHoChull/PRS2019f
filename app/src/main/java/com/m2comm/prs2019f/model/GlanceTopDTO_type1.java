package com.m2comm.prs2019f.model;

import java.util.ArrayList;

public class GlanceTopDTO_type1 {
    private String day_Type;
    private String bottom_menu;
    private String bottom_menu_now;
    private String bottom_menu_program;
    private String bottom_menu_glance;
    private String bottom_menu_myfav;
    private String bottom_menu_category;
    private String session_topmenu_bg;
    private String session_topmenu_font;
    private String menu_bg;
    private String menu_bg_on;
    private String menu_font;
    private String menu_font_on;
    private String session_day_bg;
    private String tab;
    private String mon;
    private ArrayList<Glance_sub_top_type1> subDTOS;


    public GlanceTopDTO_type1(String day_Type, String bottom_menu, String bottom_menu_now, String bottom_menu_program, String bottom_menu_glance, String bottom_menu_myfav, String bottom_menu_category, String session_topmenu_bg, String session_topmenu_font, String menu_bg, String menu_bg_on, String menu_font, String menu_font_on, String session_day_bg, String tab, String mon , ArrayList<Glance_sub_top_type1> subDTOS) {
        this.day_Type = day_Type;
        this.bottom_menu = bottom_menu;
        this.bottom_menu_now = bottom_menu_now;
        this.bottom_menu_program = bottom_menu_program;
        this.bottom_menu_glance = bottom_menu_glance;
        this.bottom_menu_myfav = bottom_menu_myfav;
        this.bottom_menu_category = bottom_menu_category;
        this.session_topmenu_bg = session_topmenu_bg;
        this.session_topmenu_font = session_topmenu_font;
        this.menu_bg = menu_bg;
        this.menu_bg_on = menu_bg_on;
        this.menu_font = menu_font;
        this.menu_font_on = menu_font_on;
        this.session_day_bg = session_day_bg;
        this.tab = tab;
        this.mon = mon;
        this.subDTOS = subDTOS;
    }

    public ArrayList<Glance_sub_top_type1> getSubDTOS() {
        return subDTOS;
    }

    public String getDay_Type() {
        return day_Type;
    }

    public String getBottom_menu() {
        return bottom_menu;
    }

    public String getBottom_menu_now() {
        return bottom_menu_now;
    }

    public String getBottom_menu_program() {
        return bottom_menu_program;
    }

    public String getBottom_menu_glance() {
        return bottom_menu_glance;
    }

    public String getBottom_menu_myfav() {
        return bottom_menu_myfav;
    }

    public String getBottom_menu_category() {
        return bottom_menu_category;
    }

    public String getSession_topmenu_bg() {
        return session_topmenu_bg;
    }

    public String getSession_topmenu_font() {
        return session_topmenu_font;
    }

    public String getMenu_bg() {
        return menu_bg;
    }

    public String getMenu_bg_on() {
        return menu_bg_on;
    }

    public String getMenu_font() {
        return menu_font;
    }

    public String getMenu_font_on() {
        return menu_font_on;
    }

    public String getSession_day_bg() {
        return session_day_bg;
    }

    public String getTab() {
        return tab;
    }

    public String getMon() {
        return mon;
    }
}
