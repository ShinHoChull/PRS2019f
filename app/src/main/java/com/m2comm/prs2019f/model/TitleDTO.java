package com.m2comm.prs2019f.model;

import android.support.v4.app.Fragment;

public class TitleDTO {

    private String mainTitle;
    private String bottomTitle;
    private String subTitle;
    private String url;
    private boolean isVoting_Question;
    private Fragment fragment;

    public TitleDTO(String mainTitle, String bottomTitle, String subTitle, String url, boolean isVoting_Question , Fragment fragment) {
        this.mainTitle = mainTitle;
        this.bottomTitle = bottomTitle;
        this.subTitle = subTitle;
        this.url = url;
        this.isVoting_Question = isVoting_Question;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }
    public void setBottomTitle(String bottomTitle) {
        this.bottomTitle = bottomTitle;
    }
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setVoting_Question(boolean voting_Question) {
        isVoting_Question = voting_Question;
    }

    public String getMainTitle() {
        return mainTitle;
    }
    public String getBottomTitle() {
        return bottomTitle;
    }
    public String getSubTitle() {
        return subTitle;
    }
    public String getUrl() {
        return url;
    }
    public boolean isVoting_Question() {
        return isVoting_Question;
    }


}
