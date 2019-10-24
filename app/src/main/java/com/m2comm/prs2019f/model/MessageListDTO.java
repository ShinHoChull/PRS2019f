package com.m2comm.prs2019f.model;

public class MessageListDTO {
    String sid;
    String name;
    String deviceId;
    String office;

    public MessageListDTO(String sid, String name, String deviceId, String office) {
        this.sid = sid;
        this.name = name;
        this.deviceId = deviceId;
        this.office = office;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOffice() {
        return office;
    }
}
