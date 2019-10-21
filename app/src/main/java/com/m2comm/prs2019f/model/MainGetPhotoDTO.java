package com.m2comm.prs2019f.model;

import java.io.Serializable;

public class MainGetPhotoDTO implements Serializable {

    private int sid;
    private String url;
    private int cnt;
    private String myfav;
    private String deviceid;

    public MainGetPhotoDTO(int sid, String url, int cnt, String myfav, String deviceid) {
        this.sid = sid;
        this.url = url;
        this.cnt = cnt;
        this.myfav = myfav;
        this.deviceid = deviceid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setMyfav(String myfav) {
        this.myfav = myfav;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public int getSid() {
        return sid;
    }

    public String getUrl() {
        return url;
    }

    public int getCnt() {
        return cnt;
    }

    public String getMyfav() {
        return myfav;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
