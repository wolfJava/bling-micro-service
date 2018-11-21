package mobi.hifun.seeu.medal.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import mobi.hifun.seeu.common.exception.LockException;
import mobi.hifun.seeu.common.lock.RDistributedLock;
import mobi.hifun.seeu.medal.dao.*;
import mobi.hifun.seeu.medal.entity.*;
import mobi.hifun.seeu.newproxy.medal.common.ResponseCode;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.service.UserMedalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

@Service(loadbalance = "roundrobin", timeout = 50000,cluster = "failfast" ,interfaceName = "mobi.hifun.seeu.newproxy.medal.service.UserMedalService")
public class UserMedalServiceImpl implements UserMedalService {

    private static final Logger log = LoggerFactory.getLogger(UserMedalServiceImpl.class);


    @Autowired
    private UserMedalMapper userMedalMapper;

    @Autowired
    private UserFragmentsStorageMapper userFragmentsStorageMapper;

    @Autowired
    private MedalMapper medalMapper;

    @Autowired
    private UserFragmentsRecordMapper userFragmentsRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MedalFragmentsStorageMapper medalFragmentsStorageMapper;

    @Autowired
    private MedalCdKeyMapper medalCdKeyMapper;

    /**
     * 查询用户所有勋章
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> queryUserAllMedal(long uid) {
        log.info("[queryUserAllMedal][requestParam] uid = {}", uid);
        //查询勋章信息
        List<Map<String,Object>> medalInfos = userMedalMapper.selectUserAllMedalByUid(uid);
        //查询用户拥有碎片信息
        List<Map<String,Object>> fragmentsInfos = userFragmentsStorageMapper.selectUserFragmentsByUid(uid);
        //响应信息
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("medalInfos",medalInfos);
        responseData.put("fragmentsInfos",fragmentsInfos);
        log.info("[queryUserDivineTime][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }

    /**
     * 勋章合成
     * @param uid
     * @param medalId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> composeMedal(long uid,long medalId) throws Exception {
        log.info("[composeMedal][requestParam] uid = {}, medalId = {}", uid, medalId);
        //step1.判断用户碎片数量是否足够
        UserFragmentsStorage userFragmentsStorage = userFragmentsStorageMapper.selectByUidAndMedalId(uid,medalId);
        if (userFragmentsStorage == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        if (!medal.getIsCompose()){
            return ResponseMessage.error(ResponseCode.MEDAL_NOT_SUPPORT_COMPOSE);
        }
        if (medal.getNeedFragmentsNum() > userFragmentsStorage.getFragmentsAvailableNum()){
            return ResponseMessage.error(ResponseCode.FRAGMENTS_NOT_ENOUGH);
        }
        //step2.添加用户勋章记录
        UserMedal userMedal = saveUserMedal(medalId, uid, (byte)0,null);

        //step3.更新用户碎片记录
        userFragmentsStorage.setFragmentsAvailableNum(userFragmentsStorage.getFragmentsAvailableNum() - medal.getNeedFragmentsNum());
        userFragmentsStorage.setFragmentsUsedNum(userFragmentsStorage.getFragmentsUsedNum() + medal.getNeedFragmentsNum());
        userFragmentsStorage.setUpdateTime(System.currentTimeMillis()/1000);
        userFragmentsStorageMapper.updateByPrimaryKey(userFragmentsStorage);

        //step4.更新用户勋章碎片记录
        saveUserFragmentsRecord(medalId,uid, (byte)1, medal.getNeedFragmentsNum());

        //step5.更新勋章碎片记录
        updateMedalFragmentsStorage(medalId);

        //step6.返回响应值
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("medalName", medal.getName());
        responseData.put("userMedalNo", userMedal.getMedalNo());
        responseData.put("userMedalId", userMedal.getId());
        log.info("[composeMedal][responseData] {}",responseData);
        return ResponseMessage.success(responseData);
    }

    /**
     * 添加用户勋章记录
     * @param medalId
     * @param uid
     * @param obtainType
     * @return
     */
    private UserMedal saveUserMedal(long medalId, long uid, byte obtainType, Integer price){
        UserMedal userMedal = new UserMedal();
        userMedal.setMedalId(medalId);
        userMedal.setUid(uid);
        userMedal.setObtainType(obtainType);
        userMedal.setMedalNo(getAtomicLongId(medalId+""));
        userMedal.setBuyPrice(price);
        userMedal.setCreateTime(System.currentTimeMillis()/1000);
        userMedalMapper.insert(userMedal);
        return userMedal;
    }

    /**
     * 保存用户碎片记录
     * @param medalId
     * @param uid
     * @param operationType
     * @param num
     * @return
     */
    private long saveUserFragmentsRecord(long medalId, long uid, byte operationType, int num){
        UserFragmentsRecord userFragmentsRecord = new UserFragmentsRecord();
        userFragmentsRecord.setNum(num);
        userFragmentsRecord.setUid(uid);
        userFragmentsRecord.setOperationType(operationType);
        userFragmentsRecord.setMedalId(medalId);
        userFragmentsRecord.setCreateTime(System.currentTimeMillis()/1000);
        userFragmentsRecordMapper.insert(userFragmentsRecord);
        return userFragmentsRecord.getId();
    }

    /**
     * 更新勋章总库数量
     */
    public void updateMedalFragmentsStorage(long medalId) throws LockException {
        RDistributedLock lock = new RDistributedLock(medalId+"");
        long start = System.currentTimeMillis();
        try {
            lock.lock();
            MedalFragmentsStorage medalFragmentsStorage = medalFragmentsStorageMapper.selectByMedalId(medalId);
            medalFragmentsStorage.setAvailableMedalNum(medalFragmentsStorage.getAvailableMedalNum() - 1);
            medalFragmentsStorage.setUsedMedalNum(medalFragmentsStorage.getUsedMedalNum() + 1);
            medalFragmentsStorage.setUpdateTime(System.currentTimeMillis()/1000);
            medalFragmentsStorageMapper.updateByPrimaryKey(medalFragmentsStorage);
        }finally {
            lock.unlock();
            long end = System.currentTimeMillis();
            System.err.println(end - start);
        }
    }

    /**
     * 获取同意编号
     * @param key
     * @return
     */
    public String getAtomicLongId(String key) {
        RedisAtomicInteger entityIdCounter = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
        int increment = entityIdCounter.getAndIncrement() + 1;
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return decimalFormat.format(increment);
    }


    /**
     * 购买勋章前校验
     * @param uid
     * @param medalId
     * @return
     */
    @Override
    public Map<String, Object> checkBuyMedalBefore(long uid, long medalId) {
        log.info("[checkBuyMedalBefore][requestParam] uid = {}", uid);
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        //校验
        Map<String, Object> responseData = checkBuyMedalBefor(medal,uid);
        if (responseData != null){
            return responseData;
        }
        //step6.返回响应值
        responseData = new HashMap<String, Object>();
        responseData.put("price", medal.getPrice());
        log.info("[checkBuyMedalBefore][responseData] {}",responseData);
        return ResponseMessage.success(responseData);
    }

    private Map<String, Object> checkBuyMedalBefor(Medal medal, long uid){
        if (medal == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        //不支持售卖
        if (!medal.getIsSell()){
            return ResponseMessage.error(ResponseCode.MEDAL_NOT_SUPPORT_TRANSCACTION);
        }
        //判断是否在售卖期
        Long start = medal.getSellStartTime();
        Long end = medal.getSellEndTime();
        Long now = System.currentTimeMillis()/1000;
        if (now < start  || end < now ){
            return ResponseMessage.error(ResponseCode.SELL_TIME_NOT_SUPPORT);
        }
        //判断是否购买过
        List<UserMedal> userMedals = userMedalMapper.selectByUidAndMedalId(uid,medal.getId());
        if (userMedals.size() > 0){
            return ResponseMessage.error(ResponseCode.ALREADY_BUY_MEDAL);
        }

        MedalFragmentsStorage medalFragmentsStorage = medalFragmentsStorageMapper.selectByMedalId(medal.getId());
        //勋章数量不足
        if (medalFragmentsStorage.getAvailableMedalNum() <= 0){
            return ResponseMessage.error(ResponseCode.MEDAL_AVAILABLE_NUM_EMPLY);
        }
        return null;
    }

    /**
     * 购买勋章
     * @param uid
     * @param medalId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> buyMedal(long uid, long medalId) throws Exception {
        log.info("[buyMedal][requestParam] uid = {}", uid);
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        //校验
        Map<String, Object> responseData = checkBuyMedalBefor(medal,uid);
        if (responseData != null){
            return responseData;
        }
        //保存勋章
        UserMedal userMedal = saveUserMedal(medalId,uid,(byte)2,medal.getPrice());
        //更新勋章
        updateMedalFragmentsStorage(medalId);
        //返回响应值
        responseData = new HashMap<String, Object>();
        responseData.put("medalName", medal.getName());
        responseData.put("userMedalNo", userMedal.getMedalNo());
        responseData.put("userMedalId", userMedal.getId());
        log.info("[composeMedal][responseData] {}",responseData);
        return ResponseMessage.success(responseData);
    }


    /**
     * 交易记录
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> queryUserMedalObtainRecord(long uid) {
        log.info("[queryUserMedalObtainRecord][requestParam] uid = {}", uid);
        List<Map<String,Object>> userMedalRecords = userMedalMapper.selectUserMedalTransactionRecordByUid(uid);
        //响应信息
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("list",userMedalRecords);
        log.info("[queryUserMedalObtainRecord][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }


    @Override
    public Map<String, Object> queryUserMedalObtainRecordDetail(long userMedalId) {
        UserMedal userMedal = userMedalMapper.selectByPrimaryKey(userMedalId);
        if (userMedal == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        Medal medal = medalMapper.selectByPrimaryKey(userMedal.getMedalId());
        //响应信息
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("medalName",medal.getName());
        responseData.put("buyPrice",userMedal.getBuyPrice()== null ? 0:userMedal.getBuyPrice());
        responseData.put("createTime",userMedal.getCreateTime());
        responseData.put("status",userMedal.getObtainType());
        responseData.put("medalId",medal.getId());
        responseData.put("userMedalId",userMedalId);
        log.info("[queryUserMedalObtainRecordDetail][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }

    /**
     * 完成任务领取勋章
     * @param uid
     * @param medalId
     * @return
     */
    @Override
    public Map<String, Object> completeTaskMedal(long uid, long medalId) {
        log.info("[completeTaskMedal][requestParam] uid = {}", uid);
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        if (medal == null) {
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        if (medal.getIsTransaction() || medal.getIsCompose() || medal.getIsSell() || medal.getIsObtain() || medal.getIsDel()){
            return ResponseMessage.error(ResponseCode.PARAM_INVALID);
        }

        List<UserMedal> userMedals = userMedalMapper.selectByUidAndMedalId(uid,medalId);
        if (userMedals.size() > 0){
            return ResponseMessage.error(ResponseCode.TASK_MEDAL_ALREADY_OBTAIN);
        }
        saveUserMedal(medal.getId(),uid, (byte)3,null);
        return ResponseMessage.success();
    }

    /**
     * 我的勋章信息详情页
     * @param uid
     * @param medalId
     * @param userMedalId
     * @return
     */
    @Override
    public Map<String, Object> queryMineMedalDetail(long uid, long medalId, Long userMedalId) {
        log.info("[queryMineMedalDetail][requestParam] uid = {} , medalId = {} , userMedalId = {}",medalId,uid,userMedalId);
        Map<String, Object> medalData = new HashMap<String, Object>();
        medalData.put("userMedalNo", "");
        medalData.put("obtainDate", "");


        //校验勋章是否存在
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        if (medal == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        int operationType = 0;//没有按钮
        boolean operationStatus = false; //安装状态不可点
        UserFragmentsStorage userFragmentsStorage = userFragmentsStorageMapper.selectByUidAndMedalId(uid,medalId);
        if (userMedalId != null){//勋章
            //校验用户是否有这枚勋章
            UserMedal userMedal = userMedalMapper.selectByPrimaryKey(userMedalId);
            if (userMedal == null){
                return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
            }
            medalData.put("userMedalNo", userMedal.getMedalNo());
            medalData.put("obtainDate", DateFormatUtils.format(userMedal.getCreateTime(),"yyyy-MM-dd"));
        }else{
            operationType = 1;//合成按钮
            if (userFragmentsStorage != null && (userFragmentsStorage.getFragmentsAvailableNum() >= medal.getNeedFragmentsNum())){
                operationStatus = true;
            }
        }
        medalData.put("needFragmentsNum", medal.getNeedFragmentsNum());
        medalData.put("medalId", medal.getId());
        medalData.put("circulation", medal.getCirculation());
        medalData.put("medalName", medal.getName());
        List<UserMedal> userMedalList = userMedalMapper.selectByUidAndMedalId(uid,medalId);
        medalData.put("userMedalNum", userMedalList.size());
        medalData.put("userFragmentsNum", userFragmentsStorage != null ? userFragmentsStorage.getFragmentsAvailableNum() : 0 );

        //操作类型
        medalData.put("operationType", operationType);
        //操作状态
        medalData.put("operationStatus", operationStatus);

        List<Map<String, Object>> captions = new ArrayList<Map<String, Object>>();
        //介绍
        if (StringUtils.isNotBlank(medal.getIntroduceCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","介绍");
            data.put("content",medal.getIntroduceCaption());
            captions.add(data);
        }
        //奖励
        if (StringUtils.isNotBlank(medal.getRewardCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","奖励");
            data.put("content",medal.getRewardCaption());
            captions.add(data);
        }
        //分解
        if (StringUtils.isNotBlank(medal.getDestroyCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","分解");
            data.put("content",medal.getDestroyCaption());
            captions.add(data);
        }
        //获得途径
        if (StringUtils.isNotBlank(medal.getObtainRouteCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","获得途径");
            data.put("content",medal.getObtainRouteCaption());
            captions.add(data);
        }
        medalData.put("caption",captions);
        log.info("[queryMineMedalDetail][response] medalData : {}",medalData);
        return ResponseMessage.success(medalData);
    }


    @Override
    public Map<String, Object> queryIndexMedalDetail(long uid, long medalId, boolean isCanReceiveByTask) {
        log.info("[queryIndexMedalDetail][requestParam] uid = {} , medalId = {} , isCanReceiveByTask = {}",medalId,uid,isCanReceiveByTask);
        Map<String, Object> medalData = new HashMap<String, Object>();
        //校验勋章是否存在
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        if (medal == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        int operationType = 0;//没有按钮
        boolean operationStatus = false; //安装状态不可点
        boolean isCanReceive = false; //安装状态不可点
        List<UserMedal> userMedals = userMedalMapper.selectByUidAndMedalId(uid,medalId);
        if (medalId == 5l){
            operationType = 1; //展示领取按钮
            //是否可以领取
            if (isCanReceiveByTask){
                //可以领取
                isCanReceive = true;
                if (userMedals.size() < 1){
                    operationStatus = true;
                }
            }else {
                //不能领取
                isCanReceive = false;
            }
        }else {
            if (medal.getIsSell()){
                operationType = 2;//售卖
                long now = System.currentTimeMillis()/1000;
                if (medal.getSellStartTime() < now && now <= medal.getSellEndTime() && userMedals.size() < 1){
                    operationStatus = true;
                }
            }
        }

        if (medal.getIsObtain()){
            operationType = 3; //展示领取按钮
            operationStatus = false;
        }

        UserFragmentsStorage userFragmentsStorage = userFragmentsStorageMapper.selectByUidAndMedalId(uid,medalId);
        medalData.put("medalId", medal.getId());
        medalData.put("price",medal.getPrice());
        medalData.put("circulation", medal.getCirculation());
        medalData.put("medalName", medal.getName());
        List<UserMedal> userMedalList = userMedalMapper.selectByUidAndMedalId(uid,medalId);
        medalData.put("userMedalNum", userMedalList.size());
        medalData.put("userFragmentsNum", userFragmentsStorage != null ? userFragmentsStorage.getFragmentsAvailableNum() : 0 );

        //操作类型
        medalData.put("operationType", operationType);
        //操作状态
        medalData.put("operationStatus", operationStatus);
        medalData.put("isCanReceive", isCanReceive);

        List<Map<String, Object>> captions = new ArrayList<Map<String, Object>>();
        //介绍
        if (StringUtils.isNotBlank(medal.getIntroduceCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","介绍");
            data.put("content",medal.getIntroduceCaption());
            captions.add(data);
        }
        //奖励
        if (StringUtils.isNotBlank(medal.getRewardCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","奖励");
            data.put("content",medal.getRewardCaption());
            captions.add(data);
        }
        //分解
        if (StringUtils.isNotBlank(medal.getDestroyCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","分解");
            data.put("content",medal.getDestroyCaption());
            captions.add(data);
        }
        //获得途径
        if (StringUtils.isNotBlank(medal.getObtainRouteCaption())){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("title","获得途径");
            data.put("content",medal.getObtainRouteCaption());
            captions.add(data);
        }
        medalData.put("caption",captions);
        log.info("[queryMineMedalDetail][response] medalData : {}",medalData);
        return ResponseMessage.success(medalData);
    }







    /**
     * 根据cdkey领取勋章
     * @param uid
     * @param medalId
     * @param cdKey
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Map<String, Object> obtainMedal(long uid,long medalId, String cdKey) throws Exception {
        log.info("[obtainMedal][requestParam] uid = {}, medalId = {}，cdKey = {}", uid, medalId, cdKey);
        //step1.查询CDK是否合法
        MedalCdKey medalCdKey = medalCdKeyMapper.selectByCdKeyAndMedalId(cdKey,medalId);
        if (medalCdKey == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        if (medalCdKey.getIsUsed()){
            return ResponseMessage.error(ResponseCode.CDKEY_USED);
        }

        //step2.增加用户获取勋章记录
        UserMedal userMedal = saveUserMedal(medalId, uid, (byte)1,null);
        //step3.修改CDK状态
        medalCdKey.setIsUsed(true);
        medalCdKey.setUid(uid);
        medalCdKey.setUserMedalId(userMedal.getId());
        medalCdKey.setUpdateTime(System.currentTimeMillis()/1000);
        medalCdKeyMapper.updateByPrimaryKey(medalCdKey);

        //查询勋章名称
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("medalName", medal.getName());
        responseData.put("userMedalNo", userMedal.getMedalNo());
        log.info("[obtainMedal][responseData] {}",responseData);
        return ResponseMessage.success(responseData);

    }












}
