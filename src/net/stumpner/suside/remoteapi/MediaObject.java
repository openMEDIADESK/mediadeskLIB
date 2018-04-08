package net.stumpner.suside.remoteapi;

import java.util.Date;

/**
 * User: stumpner
 * Date: 17.01.2011
 * Time: 15:31:16
 */
public class MediaObject {

    private int ivid = 0;
    private Date createDate = null;
    private String versionName = "";
    private String versionTitleLng1 = "";
    private String versionTitleLng2 = "";
    private String noteLng1 = "";
    private String noteLng2 = "";

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionTitleLng1() {
        return versionTitleLng1;
    }

    public void setVersionTitleLng1(String versionTitleLng1) {
        this.versionTitleLng1 = versionTitleLng1;
    }

    public String getVersionTitleLng2() {
        return versionTitleLng2;
    }

    public void setVersionTitleLng2(String versionTitleLng2) {
        this.versionTitleLng2 = versionTitleLng2;
    }

    public String getNoteLng1() {
        return noteLng1;
    }

    public void setNoteLng1(String noteLng1) {
        this.noteLng1 = noteLng1;
    }

    public String getNoteLng2() {
        return noteLng2;
    }

    public void setNoteLng2(String noteLng2) {
        this.noteLng2 = noteLng2;
    }

    public int getIvid() {
        return ivid;
    }

    public void setIvid(int ivid) {
        this.ivid = ivid;
    }
}
