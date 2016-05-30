package com.example.apaul5.nytimessearch.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by apaul5 on 5/24/16.
 */
public class RequestModel implements Serializable{
    public String sort;

    public String beginDate;

    public String deskValue = "art";

    public String getSort() {
        return sort;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getDeskValue() {
        return deskValue;
    }

    public RequestModel(String sort, String beginDate, String deskValue) {
        this.sort = sort;
        this.beginDate = beginDate;
        this.deskValue = deskValue;
    }

    public RequestModel() {

    }
    public String toString(){
        return "Date "+ beginDate.toString()+ "  sort :" +sort;
    }
}
