package mobi.hifun.seeu.newproxy.medal.service;

import mobi.hifun.seeu.newproxy.medal.request.MedalVo;

import java.util.Map;

/**
 * 用户占卜接口
 * @author huhao 2018年08月23日12:00:30
 */
public interface UserDivineService {

    /**
     * 查询用户占卜次数
     * @param uid
     * @return
     */
    public Map<String, Object> queryUserDivineTime(Long uid);


    /**
     * 用户占卜接口
     * @param uid
     * @return
     */
    public Map<String, Object> userDivine(Long uid) throws Exception;


    /**
     * 添加分享记录
     * @param uid
     * @return
     */
    public Map<String, Object> addShareRecord(Long uid);


    /**
     * 查询用户占卜数量信息
     * @param uid
     * @return
     */
    public Map<String, Object> queryUserDivineNum(long uid);

    /**
     * 查询占卜数量类型
     * @return
     */
    public Map<String, Object> queryDivineTimeType(long uid);

    /**
     * 查询占卜数量类型的bling价格
     * @return
     */
    public Map<String, Object> queryDivineTimeTypePrice(long uid, long configId);

    /**
     * 购买占卜次数
     * @param uid
     * @param configId
     * @return
     */
    public Map<String, Object> buyDivineTime(long uid, long configId);


    /**
     * 根据任务获取碎片
     * @param uid
     * @param num
     * @return
     */
    public Map<String, Object> getFragmentsStoragesByTask(long uid, int num) throws Exception  ;


}
