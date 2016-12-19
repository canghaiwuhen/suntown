package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/12/7.
 */

public class UpdateInfo {
    public String version;
    public String description;
    public String url;

    public UpdateInfo(String version, String description, String url) {
        this.version = version;
        this.description = description;
        this.url = url;
    }

    public UpdateInfo() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
