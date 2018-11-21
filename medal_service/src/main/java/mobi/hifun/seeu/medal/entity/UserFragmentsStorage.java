package mobi.hifun.seeu.medal.entity;

public class UserFragmentsStorage {
    private Long id;

    private Long medalId;

    private Long uid;

    private Integer fragmentsTotalNum;

    private Integer fragmentsAvailableNum;

    private Integer fragmentsUsedNum;

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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getFragmentsTotalNum() {
        return fragmentsTotalNum;
    }

    public void setFragmentsTotalNum(Integer fragmentsTotalNum) {
        this.fragmentsTotalNum = fragmentsTotalNum;
    }

    public Integer getFragmentsAvailableNum() {
        return fragmentsAvailableNum;
    }

    public void setFragmentsAvailableNum(Integer fragmentsAvailableNum) {
        this.fragmentsAvailableNum = fragmentsAvailableNum;
    }

    public Integer getFragmentsUsedNum() {
        return fragmentsUsedNum;
    }

    public void setFragmentsUsedNum(Integer fragmentsUsedNum) {
        this.fragmentsUsedNum = fragmentsUsedNum;
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