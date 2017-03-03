package com.suntown.cloudmonitoring.weight;

/**
 * @param
 * @author ldm
 * @description 录音实体类
 * @time 2016/6/25 11:04
 */
public class Recorder {
    public float time;//时间长度
    public String filePath;//文件路径

    public Recorder(float time, String filePath) {
        super();
        this.time = time;
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}