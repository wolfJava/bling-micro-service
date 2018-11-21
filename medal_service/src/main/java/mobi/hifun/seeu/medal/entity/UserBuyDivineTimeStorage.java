package mobi.hifun.seeu.medal.entity;

public class UserBuyDivineTimeStorage {
    private Long id;

    private Long uid;

    private Integer totalDivineNum = 0;

    private Integer availableDivineNum = 0;

    private Integer usedDivineNum = 0;

    private Long createTime;

    private Long updateTime;

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

    public Integer getTotalDivineNum() {
        return totalDivineNum;
    }

    public void setTotalDivineNum(Integer totalDivineNum) {
        this.totalDivineNum = totalDivineNum;
    }

    public Integer getAvailableDivineNum() {
        return availableDivineNum;
    }

    public void setAvailableDivineNum(Integer availableDivineNum) {
        this.availableDivineNum = availableDivineNum;
    }

    public Integer getUsedDivineNum() {
        return usedDivineNum;
    }

    public void setUsedDivineNum(Integer usedDivineNum) {
        this.usedDivineNum = usedDivineNum;
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