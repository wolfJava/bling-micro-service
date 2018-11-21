package mobi.hifun.seeu.medal.entity;

import org.springframework.data.annotation.Transient;

public class Medal {
    private Long id;

    private String name;

    private String thumbnailUrl;

    private String viewUrl;

    private Integer circulation;

    private Byte medalType;

    private Integer needFragmentsNum;

    private Integer price;

    private Integer sort;

    private String introduceCaption;

    private String obtainRouteCaption;

    private String rewardCaption;

    private String destroyCaption;

    private Boolean isDel;

    private Boolean isTransaction;

    private Boolean isDestory;

    private Boolean isCompose;

    private Boolean isObtain;

    private Boolean isSell;

    private Integer divineNum;

    private Integer reserveNum;

    private Integer taskNum;

    private Integer fragmentsWaringNum;

    private String sellCaption;

    private Long sellStartTime;

    private Long sellEndTime;

    private Long createTime;

    private Long updateTime;

    @Transient
    private int divineAvailableFragmentsNum;

    public int getDivineAvailableFragmentsNum() {
        return divineAvailableFragmentsNum;
    }

    public void setDivineAvailableFragmentsNum(int divineAvailableFragmentsNum) {
        this.divineAvailableFragmentsNum = divineAvailableFragmentsNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl == null ? null : thumbnailUrl.trim();
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl == null ? null : viewUrl.trim();
    }

    public Integer getCirculation() {
        return circulation;
    }

    public void setCirculation(Integer circulation) {
        this.circulation = circulation;
    }

    public Byte getMedalType() {
        return medalType;
    }

    public void setMedalType(Byte medalType) {
        this.medalType = medalType;
    }

    public Integer getNeedFragmentsNum() {
        return needFragmentsNum;
    }

    public void setNeedFragmentsNum(Integer needFragmentsNum) {
        this.needFragmentsNum = needFragmentsNum;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIntroduceCaption() {
        return introduceCaption;
    }

    public void setIntroduceCaption(String introduceCaption) {
        this.introduceCaption = introduceCaption == null ? null : introduceCaption.trim();
    }

    public String getObtainRouteCaption() {
        return obtainRouteCaption;
    }

    public void setObtainRouteCaption(String obtainRouteCaption) {
        this.obtainRouteCaption = obtainRouteCaption == null ? null : obtainRouteCaption.trim();
    }

    public String getRewardCaption() {
        return rewardCaption;
    }

    public void setRewardCaption(String rewardCaption) {
        this.rewardCaption = rewardCaption == null ? null : rewardCaption.trim();
    }

    public String getDestroyCaption() {
        return destroyCaption;
    }

    public void setDestroyCaption(String destroyCaption) {
        this.destroyCaption = destroyCaption == null ? null : destroyCaption.trim();
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public Boolean getIsTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(Boolean isTransaction) {
        this.isTransaction = isTransaction;
    }

    public Boolean getIsDestory() {
        return isDestory;
    }

    public void setIsDestory(Boolean isDestory) {
        this.isDestory = isDestory;
    }

    public Boolean getIsCompose() {
        return isCompose;
    }

    public void setIsCompose(Boolean isCompose) {
        this.isCompose = isCompose;
    }

    public Boolean getIsObtain() {
        return isObtain;
    }

    public void setIsObtain(Boolean isObtain) {
        this.isObtain = isObtain;
    }

    public Boolean getIsSell() {
        return isSell;
    }

    public void setIsSell(Boolean isSell) {
        this.isSell = isSell;
    }

    public Integer getDivineNum() {
        return divineNum;
    }

    public void setDivineNum(Integer divineNum) {
        this.divineNum = divineNum;
    }

    public Integer getReserveNum() {
        return reserveNum;
    }

    public void setReserveNum(Integer reserveNum) {
        this.reserveNum = reserveNum;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getFragmentsWaringNum() {
        return fragmentsWaringNum;
    }

    public void setFragmentsWaringNum(Integer fragmentsWaringNum) {
        this.fragmentsWaringNum = fragmentsWaringNum;
    }

    public String getSellCaption() {
        return sellCaption;
    }

    public void setSellCaption(String sellCaption) {
        this.sellCaption = sellCaption == null ? null : sellCaption.trim();
    }

    public Long getSellStartTime() {
        return sellStartTime;
    }

    public void setSellStartTime(Long sellStartTime) {
        this.sellStartTime = sellStartTime;
    }

    public Long getSellEndTime() {
        return sellEndTime;
    }

    public void setSellEndTime(Long sellEndTime) {
        this.sellEndTime = sellEndTime;
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