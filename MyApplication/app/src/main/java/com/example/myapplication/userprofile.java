package com.example.myapplication;

public class userprofile {
    public String  uname, usid;
    public String token;

    public userprofile(){
    }
    public userprofile(String uname,String uid){
        this.usid=uid;
        this.uname=uname;
    }
  /*  public userprofile(String uname,String uid, String token){
        this.usid=uid;
        this.uname=uname;
        this.token = token;
    }*/


    public String getuname(){
      return uname;
  }
    public String getsuid(){
        return usid;
    }

    public void setUid(String uid) {
        this.usid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

}
