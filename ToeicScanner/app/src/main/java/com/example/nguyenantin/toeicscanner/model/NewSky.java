package com.example.nguyenantin.toeicscanner.model;

/**
 * Created by nguyenantin on 12/26/17.
 */

public class NewSky {
    private String dapan;
    private String made;
    private String cau;
    private String token;

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public void setCau(String cau) {
        this.cau = cau;
    }


    public String getMade() {
        return made;
    }
    public String getCau() {
        return cau;
    }
    public String getDapan() {
        return dapan;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
