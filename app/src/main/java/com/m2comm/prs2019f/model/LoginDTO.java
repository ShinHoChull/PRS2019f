package com.m2comm.prs2019f.model;

public class LoginDTO {

    String sid;

    public LoginDTO(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
