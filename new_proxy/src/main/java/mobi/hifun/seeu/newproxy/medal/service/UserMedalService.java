package mobi.hifun.seeu.newproxy.medal.service;

import java.util.Map;

/**
 * 用户勋章接口
 * @author huhao 2018年08月23日12:00:30
 */
public interface UserMedalService {


    /**
     * 根据入口获取勋章信息
     * @param uid
     * @param medalId
     * @param userMedalId 用户勋章id
     * @return
     */
    public Map<String, Object> queryMineMedalDetail(long uid, long medalId, Long userMedalId);


    /**
     * 首页勋章信息详情
     * @param uid
     * @param medalId
     * @return
     */
    public Map<String, Object> queryIndexMedalDetail(long uid, long medalId, boolean isCanReceiveByTask);


    /**
     * 合成勋章
     * @param uid
     * @param medalId
     * @return
     */
    public Map<String, Object> composeMedal(long uid,long medalId) throws Exception ;

    /**
     * 领取勋章，根据cdkey来领取
     * @param uid
     * @param cdKey
     * @return
     */
    public Map<String, Object> obtainMedal(long uid,long medalId, String cdKey) throws Exception ;

    /**
     * 查询用户所有勋章
     * @param uid
     * @return
     */
    public Map<String, Object> queryUserAllMedal(long uid);

    /**
     * 查询用户勋章交易详情
     * @param userMedalId
     * @return
     */
    public Map<String, Object> queryUserMedalObtainRecordDetail(long userMedalId);

    /**
     * 查询用户勋章获取记录
     * @param uid
     * @return
     */
    public Map<String, Object> queryUserMedalObtainRecord(long uid);


    /**
     * 完成任务获得成就勋章
     * @param uid
     * @param medalId
     * @return
     */
    public Map<String, Object> completeTaskMedal(long uid, long medalId);

    /**
     * 购买勋章前验证
     * @param uid
     * @param medalId
     * @return
     */
    public Map<String, Object> checkBuyMedalBefore(long uid, long medalId);


    /**
     * 购买勋章
     * @param uid
     * @param medalId
     * @return
     */
    public Map<String, Object> buyMedal(long uid, long medalId) throws Exception ;




}
