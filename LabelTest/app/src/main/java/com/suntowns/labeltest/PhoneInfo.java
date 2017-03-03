package com.suntowns.labeltest;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PhoneInfo {
    private String phoneName;
    private String phoneNumber;

    public PhoneInfo(String phoneName, String phoneNumber) {
        setPhoneName(phoneName);
        setPhoneNumber(phoneNumber);
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "phoneName='" + phoneName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
