package mobi.hifun.seeu.medal.entity;

import java.util.Date;

public class UserDivineRecord {
    private Long id;

    private Long uid;

    private Byte divineType;

    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getDivineType() {
        return divineType;
    }

    public void setDivineType(Byte divineType) {
        this.divineType = divineType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}