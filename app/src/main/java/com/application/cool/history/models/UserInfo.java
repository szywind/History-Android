package com.application.cool.history.models;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public class UserInfo {
    /**
     * nickname :
     * phone :
     * email :
     * gender :
     * accountType :
     */

    private String nickname;
    private String phone;
    private String email;
    private String gender;
    private String accountType;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    public UserInfo(String nickname, String accountType) {
        this.nickname = nickname;
        this.accountType = accountType;
    }

    public UserInfo(String nickname, String email, String phone, String accountType) {
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.accountType = accountType;
    }
}
