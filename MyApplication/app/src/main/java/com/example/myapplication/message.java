package com.example.myapplication;

public class message {
    String msg,senderid, curenttime;
    long timesmap;

    public message(){
    }
    public message(String msg,String sid,String ct,long timesmap){
        this.msg= msg;
        this.senderid=sid;
        this.curenttime=ct;
        this.timesmap=timesmap;
    }



    public String getMsg(){
        return msg;
    }
    public String getSenderid(){
        return senderid;
    }
    public long getTimesmap(){
        return timesmap;
    }
    public String getCurenttime(){
        return curenttime;
    }
    public void setCurenttime(String ct){
        this.curenttime= ct;
    }
    public void setTimesmap(long ct){
        this.timesmap= ct;
    }
    public void setmesg(String ct){
        this.msg= ct;
    }
    public void setSenderid(String ct){
        this.senderid= ct;
    }
}
