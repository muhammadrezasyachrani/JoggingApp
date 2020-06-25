package com.muhammadreza.joggingapp;

public class sessionClass {

    private int sessionid;
    private String sessionname;
    private String totaltime;
    private String kilometers;
    private String date;
    private String starttime;

    public sessionClass(int sessionid, String sessionname, String totaltime, String kilometers, String date) {
        this.sessionid = sessionid;
        this.sessionname = sessionname;
        this.totaltime = totaltime;
        this.kilometers = kilometers;
        this.date = date;
    }

    public int getSessionid() {
        return sessionid;
    }

    public String getSessionname() {
        return sessionname;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public String getKilometers() {
        return kilometers;
    }

    public String getDate() {
        return date;
    }

    public void setSessionid(int sessionid) {
        this.sessionid = sessionid;
    }

    public void setSessionname(String sessionname) {
        this.sessionname = sessionname;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public void setKilometers(String kilometers) {
        this.kilometers = kilometers;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
