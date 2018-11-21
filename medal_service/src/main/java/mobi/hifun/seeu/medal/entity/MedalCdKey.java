package mobi.hifun.seeu.medal.entity;

import java.util.Date;

public class MedalCdKey {
    private Long id;

    private Long medalId;

    private String ckKey;

    private Boolean isUsed = false;

    private Long uid;

    private Long userMedalId;

    private Long createTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedalId() {
        return medalId;
    }

    public void setMedalId(Long medalId) {
        this.medalId = medalId;
    }

    public String getCkKey() {
        return ckKey;
    }

    public void setCkKey(String ckKey) {
        this.ckKey = ckKey == null ? null : ckKey.trim();
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUserMedalId() {
        return userMedalId;
    }

    public void setUserMedalId(Long userMedalId) {
        this.userMedalId = userMedalId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}