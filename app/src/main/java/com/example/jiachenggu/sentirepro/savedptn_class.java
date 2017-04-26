package com.example.jiachenggu.sentirepro;

/**
 * Created by Jiacheng Gu on 2017/4/7.
 */

public class savedptn_class {
    private  String ptnname;
    private  String ptntype;
    private  String ptnemot;
    private  String ptncode;
    private  int scorehap;
    private  int scoresad;
    private  int scoreang;
    private  int scorecon;
    private  int scoresup;
    private  int scoredis;
    private  int scorefea;
    private  int scorenod;
    private  int scoresha;
    private  int ptntimes;
    private  int imgid;
    public savedptn_class(String ptnname,String ptntype,String ptnemot,String ptncode,
                          int ptntimes,
                          int scorehap,int scoresad,int scoreang,int scorecon,
                          int scoresup,int scoredis,int scorefea,int scorenod,
                          int scoresha,int imgid){
        this.ptnname = ptnname;
        this.ptntype = ptntype;
        this.ptnemot = ptnemot;
        this.ptncode = ptncode;
        this.ptntimes = ptntimes;
        this.scorehap = scorehap;
        this.scoresad = scoresad;
        this.scoreang = scoreang;
        this.scorecon = scorecon;
        this.scoresup = scoresup;
        this.scoredis = scoredis;
        this.scorefea = scorefea;
        this.scorenod = scorenod;
        this.scoresha = scoresha;
        this.imgid = imgid;
    }

    public String getPtnname() {
        return ptnname;
    }

    public void setPtnname(String ptnname) {
        this.ptnname = ptnname;
    }

    public String getPtntype() {
        return ptntype;
    }

    public void setPtntype(String ptntype) {
        this.ptntype = ptntype;
    }

    public String getPtnemot() {
        return ptnemot;
    }

    public void setPtnemot(String ptnemot) {
        this.ptnemot = ptnemot;
    }

    public String getPtncode() {
        return ptncode;
    }

    public void setPtncode(String ptncode) {
        this.ptncode = ptncode;
    }




    public int getPtntimes() {
        return ptntimes;
    }

    public void setPtntimes(int ptntimes) {
        this.ptntimes = ptntimes;
    }

    public int getScorehap() {
        return scorehap;
    }

    public void setScorehap(int scorehap) {
        this.scorehap = scorehap;
    }


    public int getScoresad() {
        return scoresad;
    }

    public void setScoresad(int scoresad) {
        this.scoresad = scoresad;
    }


    public int getScoreang() {
        return scoreang;
    }

    public void setScoreang(int scoreang) {
        this.scoreang = scoreang;
    }


    public int getScorecon() {
        return scorecon;
    }

    public void setScorecon(int scorecon) {
        this.scorecon = scorecon;
    }


    public int getScoresup() {
        return scoresup;
    }

    public void setScoresup(int scoresup) {
        this.scoresup = scoresup;
    }


    public int getScoredis() {
        return scoredis;
    }

    public void setScoredis(int scoredis) {
        this.scoredis = scoredis;
    }


    public int getScorefea() {
        return scorefea;
    }

    public void setScorefea(int scorefea) {
        this.scorefea = scorefea;
    }


    public int getScorenod() {
        return scorenod;
    }

    public void setScorenod(int scorenod) {
        this.scorenod = scorenod;
    }


    public int getScoresha() {
        return scoresha;
    }

    public void setScoresha(int scoresha) {
        this.scoresha = scoresha;
    }




    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }



}