package mobi.hifun.seeu.newproxy.medal.common;

public enum ResponseCode {

    FAIL(0, "系统异常，请重试"),
    OK(1, "请求成功"),
    //20000 参数相关
    PARAM_EMPLY(20001, "参数不能为空"),
    PARAM_INVALID(20002, "无效的参数"),
    COMPOSE_FRAGMENTS_NUM_INVALID(20003, "勋章可合成时，请输入需要合成碎片数量"),
    MEDAL_NUM_INVALID(20004, "勋章数量配比不正确"),


    //30000 业务相关
    MEDAL_NAME_ALREADY(30001,"勋章名称已存在"),
    NO_DATA_FOUND(30002,"未找到数据"),
    OUT_DIVINE_LIMIT(30003,"超过占卜次数"),
    FRAGMENTS_NOT_ENOUGH(30004,"碎片数量不足"),
    FRAGMENTS_STORAGE_EMPLY(30005,"碎片库已空"),
    CDKEY_ALREADY(30006,"cdkey已存在"),
    MEDAL_NOT_SUPPORT_CDKEY(30007,"该勋章不支持cdkey兑换"),
    MEDAL_AVAILABLE_NUM_EMPLY(30008,"勋章可用数量已不足"),
    CDKEY_USED(30009,"cdkey已使用"),
    MEDAL_NOT_SUPPORT_TRANSCACTION(30010,"该勋章不支持交易"),

    ALREADY_BUY(30011,"已购买"),
    TRANSCACTION_NOT_SET(30012,"交易密码未设置"),

    MEDAL_NOT_SUPPORT_COMPOSE(30013,"该勋章不支持合成"),
    SELL_TIME_NOT_SUPPORT(30014,"不在售卖期"),
    TASK_MEDAL_ALREADY_OBTAIN(30015,"任务勋章已经获得"),
    ALREADY_BUY_MEDAL(30016,"勋章已经购买"),
    APP_VERSION_NOT_MATCH_CLIENT (30017,"版本号与客户端不匹配")

    ;

    ResponseCode(int number, String message) {
        this.code = number;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
