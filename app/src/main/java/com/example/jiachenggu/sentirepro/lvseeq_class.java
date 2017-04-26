package com.example.jiachenggu.sentirepro;

/**
 * Created by Jiacheng Gu on 2017/4/13.
 */

public class lvseeq_class {
    private  String ptntype;
    private  String ptnemot;
    private  String ptncode;
    private  int imgid;
    public lvseeq_class(String ptntype,String ptnemot,String ptncode,int imgid){
        this.ptntype = ptntype;
        this.ptnemot = ptnemot;
        this.ptncode = ptncode;
        this.imgid = imgid;
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


    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }


}
