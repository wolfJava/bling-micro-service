package mobi.hifun.seeu.medal.entity;

import java.util.Date;

public class UserFragmentsRecord {
    private Long id;

    private Long medalId;

    private Long uid;

    private Integer num;

    private Byte operationType;

    private Long divineId;

    private Long createTime;

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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public Long getDivineId() {
        return divineId;
    }

    public void setDivineId(Long divineId) {
        this.divineId = divineId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}