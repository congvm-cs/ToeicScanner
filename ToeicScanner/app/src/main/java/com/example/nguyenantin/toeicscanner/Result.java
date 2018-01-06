package com.example.nguyenantin.toeicscanner;

/**
 * Created by nguyenantin on 12/18/17.
 */

public class Result
{
    private String result;
    private String result2;
    private String id;
    private String stt;

    public Result() {
    }

    public Result(String stt, String result, String result2) {
        this.result = result;
        this.result2 = result2;
        this.stt = stt;
    }

    public Result(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getStt() {
        return stt;
    }
    public void setStt(String stt) {
        this.stt = stt;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String ten) {
        this.result2 = result2;
    }

}
