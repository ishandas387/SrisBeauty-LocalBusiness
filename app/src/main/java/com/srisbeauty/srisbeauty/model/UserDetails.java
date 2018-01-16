package com.srisbeauty.srisbeauty.model;

/**
 * Created by ishan on 04-11-2017.
 */

public class UserDetails {

    String key;
    String userEmail;

    public boolean isAdminstrator() {
        return adminstrator;
    }

    public void setAdminstrator(boolean adminstrator) {
        this.adminstrator = adminstrator;
    }

    boolean adminstrator = false;
    String userName;
    String uId;
    public boolean admin;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {

        return key;
    }





    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserDetails( String userEmail, boolean adminstrator, String userName, String uId,String userToken) {

        this.userEmail = userEmail;
        this.adminstrator = adminstrator;
        this.userName = userName;
        this.uId = uId;
        this.key = userToken;
    }

    public String getUserName() {

        return userName;
    }







    public UserDetails() {
    }


    public String getUserEmail() {
        return userEmail;
    }




    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }




    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuId() {

        return uId;
    }
}
