package com.m2comm.prs2019f.model;

public class BannerDTO {

    private int sid;
    private int gubun;
    private String image;
    private String linkurl;

    public BannerDTO(int sid, int gubun, String image, String linkurl) {
        this.sid = sid;
        this.gubun = gubun;
        this.image = image;
        this.linkurl = linkurl;
    }

    public void setGubun(int gubun) {
        this.gubun = gubun;
    }

    public int getGubun() {
        return gubun;
    }

    public int getSid() {
        return sid;
    }

    public String getImgUrl() {
        return image;
    }

    public String getLinkUrl() {
        return linkurl;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setImgUrl(String imgUrl) {
        this.image = imgUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkurl = linkUrl;
    }
}
