package mobi.hifun.seeu.medal.entity;

public class MedalFragmentsStorage {
    private Long id;

    private Long medalId;

    private Integer totalMedalNum = 0;

    private Integer usedMedalNum = 0;

    private Integer availableMedalNum = 0;

    private Integer reserveMedalNum = 0;

    private Integer destroyMedalNum = 0;

    private Integer divineAvailableFragmentsNum = 0;

    private Integer totalFragmentsNum = 0;

    private Integer destoryFragmentsNum = 0;

    private Integer taskAvailableFragmentsNum = 0;

    private Integer reserveFragmentsNum = 0;

    private Integer usedFragmentsNum = 0;

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

    public Integer getTotalMedalNum() {
        return totalMedalNum;
    }

    public void setTotalMedalNum(Integer totalMedalNum) {
        this.totalMedalNum = totalMedalNum;
    }

    public Integer getUsedMedalNum() {
        return usedMedalNum;
    }

    public void setUsedMedalNum(Integer usedMedalNum) {
        this.usedMedalNum = usedMedalNum;
    }

    public Integer getAvailableMedalNum() {
        return availableMedalNum;
    }

    public void setAvailableMedalNum(Integer availableMedalNum) {
        this.availableMedalNum = availableMedalNum;
    }

    public Integer getReserveMedalNum() {
        return reserveMedalNum;
    }

    public void setReserveMedalNum(Integer reserveMedalNum) {
        this.reserveMedalNum = reserveMedalNum;
    }

    public Integer getDestroyMedalNum() {
        return destroyMedalNum;
    }

    public void setDestroyMedalNum(Integer destroyMedalNum) {
        this.destroyMedalNum = destroyMedalNum;
    }

    public Integer getDivineAvailableFragmentsNum() {
        return divineAvailableFragmentsNum;
    }

    public void setDivineAvailableFragmentsNum(Integer divineAvailableFragmentsNum) {
        this.divineAvailableFragmentsNum = divineAvailableFragmentsNum;
    }

    public Integer getTotalFragmentsNum() {
        return totalFragmentsNum;
    }

    public void setTotalFragmentsNum(Integer totalFragmentsNum) {
        this.totalFragmentsNum = totalFragmentsNum;
    }

    public Integer getDestoryFragmentsNum() {
        return destoryFragmentsNum;
    }

    public void setDestoryFragmentsNum(Integer destoryFragmentsNum) {
        this.destoryFragmentsNum = destoryFragmentsNum;
    }

    public Integer getTaskAvailableFragmentsNum() {
        return taskAvailableFragmentsNum;
    }

    public void setTaskAvailableFragmentsNum(Integer taskAvailableFragmentsNum) {
        this.taskAvailableFragmentsNum = taskAvailableFragmentsNum;
    }

    public Integer getReserveFragmentsNum() {
        return reserveFragmentsNum;
    }

    public void setReserveFragmentsNum(Integer reserveFragmentsNum) {
        this.reserveFragmentsNum = reserveFragmentsNum;
    }

    public Integer getUsedFragmentsNum() {
        return usedFragmentsNum;
    }

    public void setUsedFragmentsNum(Integer usedFragmentsNum) {
        this.usedFragmentsNum = usedFragmentsNum;
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