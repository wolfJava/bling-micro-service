package mobi.hifun.seeu.medal.entity;

public class UserMedal {
    private Long id;

    private Long medalId;

    private String medalNo;

    private Long uid;

    private Byte obtainType;

    private Integer buyPrice;

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

    public String getMedalNo() {
        return medalNo;
    }

    public void setMedalNo(String medalNo) {
        this.medalNo = medalNo == null ? null : medalNo.trim();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getObtainType() {
        return obtainType;
    }

    public void setObtainType(Byte obtainType) {
        this.obtainType = obtainType;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Integer buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}