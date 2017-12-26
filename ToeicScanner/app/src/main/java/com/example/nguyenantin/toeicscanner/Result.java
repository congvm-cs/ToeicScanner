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
    int crread = 0;
    int crlisten = 0;
    public Result() {
    }

    public Result(String stt, String result, String result2) {
        this.result = result;
        this.result2 = result2;
        this.crlisten = 0;
        this.crread = 0;
        this.stt = stt;
    }
//    public Result(JSONObject object) {
//        try {
//            this.id = object.getString("made");
//            this.result = object.getString("result");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

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
