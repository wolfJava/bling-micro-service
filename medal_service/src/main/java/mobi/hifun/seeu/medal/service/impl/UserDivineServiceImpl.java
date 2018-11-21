package mobi.hifun.seeu.medal.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import mobi.hifun.seeu.common.TimeUtil;
import mobi.hifun.seeu.common.exception.LockException;
import mobi.hifun.seeu.common.lock.RDistributedLock;
import mobi.hifun.seeu.common.util.MailUtil;
import mobi.hifun.seeu.medal.dao.*;
import mobi.hifun.seeu.medal.entity.*;
import mobi.hifun.seeu.newproxy.medal.common.ResponseCode;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.service.UserDivineService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(loadbalance = "roundrobin", timeout = 50000 ,interfaceName = "mobi.hifun.seeu.newproxy.medal.service.UserDivineService")
public class UserDivineServiceImpl implements UserDivineService {

    private static final Logger log = LoggerFactory.getLogger(UserDivineServiceImpl.class);

    @Value("${free.divine.time}")
    private Integer freeDivineTime;

    @Autowired
    private MedalMapper medalMapper;

    @Autowired
    private UserDivineRecordMapper userDivineRecordMapper;

    @Autowired
    private UserDivineShareRecordMapper userDivineShareRecordMapper;

    @Autowired
    private UserFragmentsRecordMapper userFragmentsRecordMapper;

    @Autowired
    private UserFragmentsStorageMapper userFragmentsStorageMapper;

    @Autowired
    private MedalFragmentsStorageMapper medalFragmentsStorageMapper;

    @Autowired
    private MedalAwardProbabilityMapper medalAwardProbabilityMapper;

    @Autowired
    private UnifiedConfigurationMapper unifiedConfigurationMapper;

    @Autowired
    private UserBuyDivineTimeRecordMapper userBuyDivineTimeRecordMapper;

    @Autowired
    private UserBuyDivineTimeStorageMapper userBuyDivineTimeStorageMapper;


    /**
     * 查询用户占卜次数
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> queryUserDivineTime(Long uid) {
        log.info("[queryUserDivineTime][requestParam] uid = {}",uid.toString());

        long startDate = TimeUtil.getAppointDayTime(0,0,0,0);
        long endDate = TimeUtil.getAppointDayTime(23,59,59,999);

        //step1.计算用户每天的抽取数
        Integer totalTime = freeDivineTime;
        List<UserDivineShareRecord> shareRecordList = userDivineShareRecordMapper.queryRecordByUidAndDate(uid,startDate,endDate);
        if (shareRecordList.size()>0){
            totalTime +=1;
        }
        //step2.计算用户已经抽取了多少次
        int usedTime = userUsedTime(uid,startDate,endDate);
        int availableTime = totalTime - usedTime;

        //增加购买的次数
        UserBuyDivineTimeStorage userBuyDivineTimeStorage = userBuyDivineTimeStorageMapper.selectByUid(uid);
        if (userBuyDivineTimeStorage != null){
            availableTime += userBuyDivineTimeStorage.getAvailableDivineNum();
        }
        //返回响应值
        Map<String, Object> timeData = new HashMap<String, Object>();
        timeData.put("totalTime",3);
        timeData.put("availableTime", availableTime);
        timeData.put("isShow", availableTime > 0);
        log.info("[queryUserDivineTime][response] timeData : {}",timeData);
        return ResponseMessage.success(timeData);
    }


    /**
     * 用户可用次数
     * @param uid
     * @param startDate
     * @param endDate
     * @return
     */
    private int userUsedTime(long uid, long startDate, long endDate){
        int usedTime = 0;
        List<UserDivineRecord> recordDivineList = userDivineRecordMapper.queryRecordByUidAndDivineTypeAndDate(uid, (byte) 0, startDate, endDate);
        usedTime += recordDivineList.size();
        List<UserDivineRecord> shareDivineRecordList = userDivineRecordMapper.queryRecordByUidAndDivineTypeAndDate(uid, (byte) 1, startDate, endDate);
        usedTime += shareDivineRecordList.size();
        return usedTime;
    }

    /**
     * 用户进行占卜
     * @param uid
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> userDivine(Long uid)  throws Exception {
        log.info("[userDivine][requestParam] uid = {}",uid.toString());
        long startDate = TimeUtil.getAppointDayTime(0,0,0,0);
        long endDate = TimeUtil.getAppointDayTime(23,59,59,999);
        //step1.计算用户每天的抽取数
        //免费次数
        Integer freeTime = freeDivineTime;
        List<UserDivineShareRecord> shareRecordList = userDivineShareRecordMapper.queryRecordByUidAndDate(uid, startDate, endDate);
        //分享次数
        Integer shareTime = 0;
        if (shareRecordList.size()>0){
            shareTime = 1;
        }
        //购买次数
        UserBuyDivineTimeStorage userBuyDivineTimeStorage = userBuyDivineTimeStorageMapper.selectByUid(uid);
        Integer availableDivineNum = 0;
        if (userBuyDivineTimeStorage != null){
            availableDivineNum = userBuyDivineTimeStorage.getAvailableDivineNum();
        }
        //step2.计算用户已经抽取了多少次
        int usedTime = userUsedTime(uid,startDate,endDate);
        //step3.判断用户是否超过抽取次数

        int totalTime = freeTime + shareTime + availableDivineNum;
        if(usedTime>=totalTime){
            return ResponseMessage.error(ResponseCode.OUT_DIVINE_LIMIT);
        }
        //step4.随机生成抽奖碎片
        //step4.1 随机获取勋章
        List<Medal> medals = medalMapper.selectByIsCompose(true);
        if (medals.size()<1){
            return ResponseMessage.error(ResponseCode.FRAGMENTS_STORAGE_EMPLY);
        }
        //step4.2 生成占卜记录
        long divineRecordId = 0l;

        List<UserDivineRecord> freeList = userDivineRecordMapper.queryRecordByUidAndDivineTypeAndDate(uid, (byte) 0, startDate, endDate);
        List<UserDivineRecord> shareList = userDivineRecordMapper.queryRecordByUidAndDivineTypeAndDate(uid, (byte) 1, startDate, endDate);


        if (freeList.size() < freeTime){
            divineRecordId = getNewUserDivineRecord(uid, (byte)0);
        }else if (shareList.size()<1 &&  shareTime == 1 ){
            divineRecordId = getNewUserDivineRecord(uid, (byte)1);
        }else {
            divineRecordId = getNewUserDivineRecord(uid, (byte)2);
            userBuyDivineTimeStorage.setAvailableDivineNum(userBuyDivineTimeStorage.getAvailableDivineNum() -1 );
            userBuyDivineTimeStorage.setUsedDivineNum(userBuyDivineTimeStorage.getUsedDivineNum() + 1);
            userBuyDivineTimeStorage.setUpdateTime(System.currentTimeMillis()/1000);
            userBuyDivineTimeStorageMapper.updateByPrimaryKey(userBuyDivineTimeStorage);
        }

        //step4.3 随机生成碎片
        Medal medal = randomDivineMedal(medals);
        int num = generateFragmentsNum(medal);
        long userFragmentsRecordId = getNewUserFragmentsRecord(uid, medal.getId(), divineRecordId,num,(byte)0);
        //step4.4 修改用户勋章碎片库信息
        updateUserFragmentsStorageByDivine(uid, medal.getId(), num);
        //step4.5 修改勋章碎片库信息
        MedalFragmentsStorage medalFragmentsStorage = updateMedalFragmentsStorageByDivine(medal.getId(), num,0);
        //做碎片预警
        int availableNum = medalFragmentsStorage.getDivineAvailableFragmentsNum() + medalFragmentsStorage.getTaskAvailableFragmentsNum();
        int oldAvailableNum = availableNum + num;
        if (oldAvailableNum > medal.getFragmentsWaringNum() && medal.getFragmentsWaringNum() >= availableNum){
            fragmentswarning(medal);
        }
        //step5.返回响应信息
        Map<String, Object> resultData = new HashMap<String, Object>();
        resultData.put("medalId",medal.getId());
        resultData.put("medalName", medal.getName()+"碎片");
        resultData.put("number",num);
        log.info("[userDivine][response] resultData : {}",resultData);
        return ResponseMessage.success(resultData);
    }

    /**
     * 随机占卜的勋章
     * @return
     */
    private Medal randomDivineMedal(List<Medal> medals){
        List<UnifiedConfiguration> confs = new ArrayList<UnifiedConfiguration>();
        for (Medal medal : medals){
            UnifiedConfiguration unifiedConfiguration = unifiedConfigurationMapper.selectByTypeAndLabel("medal_award_probability",medal.getId()+"");
            confs.add(unifiedConfiguration);
        }
        int totalPro = 0;
        List<Integer> proSection = new ArrayList<Integer>();
        proSection.add(0);
        for (UnifiedConfiguration unifiedConfiguration : confs) {
            totalPro += Integer.parseInt(unifiedConfiguration.getValue());
            proSection.add(totalPro);
        }
        Random random = new Random();
        int randomPro = random.nextInt(totalPro);
        //判断取到的随机数在哪个奖品的概率区间中
        int medalsIndexOf = 0;
        for (int i = 0,size = proSection.size(); i < size; i++) {
            if(randomPro >= proSection.get(i)
                    && randomPro < proSection.get(i + 1)){
                medalsIndexOf = i;
            }
        }
        return medals.get(medalsIndexOf);
    }


    /**
     * 碎片预警值
     * @param medal
     */
    private void fragmentswarning(Medal medal){

        List<UnifiedConfiguration> configurations = unifiedConfigurationMapper.selectByTypeOrderByValueAsc("fragments_warning");
        StringBuilder email = new StringBuilder();
        for (int i = 0; i < configurations.size(); i++) {
            email.append(configurations.get(i).getValue());
            if (i < (configurations.size()-1)){
                email.append(",");
            }
        }
        String content = medal.getName() + "碎片已超过预警值，请及时处理！";
        try {
            MailUtil.sendMail(email.toString(), "碎片预警", content);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 生成一次新的占卜记录
     * @param uid
     * @param divineType
     * @return
     */
    private long getNewUserDivineRecord(long uid,byte divineType){
        UserDivineRecord userDivineRecord = new UserDivineRecord();
        userDivineRecord.setUid(uid);
        userDivineRecord.setDivineType(divineType);
        userDivineRecord.setCreateTime(System.currentTimeMillis()/1000);
        userDivineRecordMapper.insert(userDivineRecord);
        return userDivineRecord.getId();
    }

    private int generateFragmentsNum(Medal medal){
        List<MedalAwardProbability> medalAwardProbabilityList = medalAwardProbabilityMapper.selectLessThanNum(medal.getDivineAvailableFragmentsNum());
        int totalPro = 0;
        List<Integer> proSection = new ArrayList<Integer>();
        proSection.add(0);
        for (MedalAwardProbability medalAwardProbability : medalAwardProbabilityList) {
            totalPro += medalAwardProbability.getProbability();
            proSection.add(totalPro);
        }
        Random random = new Random();
        int randomPro = random.nextInt(totalPro);
        //判断取到的随机数在哪个奖品的概率区间中
        int num = 0;
        for (int i = 0,size = proSection.size(); i < size; i++) {
            if(randomPro >= proSection.get(i)
                    && randomPro < proSection.get(i + 1)){
                num = medalAwardProbabilityList.get(i).getNum();
            }
        }
        return num;
    }

    /**
     * 生成一次新的碎片记录
     * @param uid
     * @param medalId
     * @param divineRecordId
     * @param number
     * @param operatorType
     * @return
     */
    private long getNewUserFragmentsRecord(long uid, long medalId, long divineRecordId,int number,byte operatorType ){
        UserFragmentsRecord userDivineRecord = new UserFragmentsRecord();
        userDivineRecord.setMedalId(medalId);
        userDivineRecord.setUid(uid);
        userDivineRecord.setDivineId(divineRecordId);
        userDivineRecord.setNum(number);
        userDivineRecord.setOperationType(operatorType);
        userDivineRecord.setCreateTime(System.currentTimeMillis()/1000);
        return userFragmentsRecordMapper.insert(userDivineRecord);
    }

    /**
     * 更新用户碎片库
     * @param uid
     * @param medalId
     * @param number
     */
    private void updateUserFragmentsStorageByDivine(long uid, long medalId, int number) throws LockException {
        UserFragmentsStorage userFragmentsStorage = userFragmentsStorageMapper.selectByUidAndMedalId(uid,medalId);
        if (userFragmentsStorage == null){
            userFragmentsStorage = new UserFragmentsStorage();
            userFragmentsStorage.setUid(uid);
            userFragmentsStorage.setMedalId(medalId);
            userFragmentsStorage.setFragmentsTotalNum(number);
            userFragmentsStorage.setFragmentsAvailableNum(number);
            userFragmentsStorage.setFragmentsUsedNum(0);
            userFragmentsStorage.setCreateTime(System.currentTimeMillis()/1000);
            userFragmentsStorage.setUpdateTime(userFragmentsStorage.getCreateTime());
            userFragmentsStorageMapper.insert(userFragmentsStorage);
        }else {
            userFragmentsStorage.setFragmentsTotalNum(userFragmentsStorage.getFragmentsTotalNum() + number);
            userFragmentsStorage.setFragmentsAvailableNum(userFragmentsStorage.getFragmentsAvailableNum() + number);
            userFragmentsStorage.setUpdateTime(System.currentTimeMillis()/1000);
            userFragmentsStorageMapper.updateByPrimaryKey(userFragmentsStorage);
        }
    }

    /**
     * 占卜碎片，更新勋章碎片库
     * @param medalId
     * @param number
     * @param type 0 占卜 1 任务领取
     */
    private MedalFragmentsStorage updateMedalFragmentsStorageByDivine(long medalId, int number, int type) throws LockException {
        RDistributedLock lock = new RDistributedLock(medalId+"");
        long start = System.currentTimeMillis();
        try {
            lock.lock();
            MedalFragmentsStorage medalFragmentsStorage = medalFragmentsStorageMapper.selectByMedalId(medalId);
            medalFragmentsStorage.setUsedFragmentsNum(medalFragmentsStorage.getUsedFragmentsNum() + number);
            if(type == 0){
                medalFragmentsStorage.setDivineAvailableFragmentsNum(medalFragmentsStorage.getDivineAvailableFragmentsNum() - number);
            }else {
                medalFragmentsStorage.setTaskAvailableFragmentsNum(medalFragmentsStorage.getTaskAvailableFragmentsNum() - number);
            }
            medalFragmentsStorage.setUpdateTime(System.currentTimeMillis()/1000);
            medalFragmentsStorageMapper.updateByPrimaryKey(medalFragmentsStorage);
            return medalFragmentsStorage;
        }finally {
            lock.unlock();
            long end = System.currentTimeMillis();
            System.err.println(end - start);
        }
    }

    /**
     * 增加分享记录
     * @param uid
     * @return
     */
    @Override
    public Map<String, Object> addShareRecord(Long uid) {
        log.info("[addShareRecord][requestParam] uid = {}",uid.toString());
        UserDivineShareRecord userDivineShareRecord = new UserDivineShareRecord();
        userDivineShareRecord.setUid(uid);
        userDivineShareRecord.setCreateTime(System.currentTimeMillis()/1000);
        userDivineShareRecordMapper.insert(userDivineShareRecord);
        return ResponseMessage.success();
    }


    /**
     * 查询占卜次数类型
     * @return
     */
    @Override
    public Map<String, Object> queryDivineTimeType(long uid) {
        log.info("[queryDivineTimeType] uid={}",uid);
        List<UnifiedConfiguration> configurations = unifiedConfigurationMapper.selectByTypeOrderByLableAsc("buy_divine_time");
        List<Map<String, Object>> divineTypes = new ArrayList<Map<String, Object>>();
        long startDate = TimeUtil.getAppointDayTime(0,0,0,0);
        long endDate = TimeUtil.getAppointDayTime(23,59,59,999);
        for (UnifiedConfiguration unifiedConfiguration : configurations) {
            Map<String, Object> divineType = new HashMap<String, Object>();
            List<UserBuyDivineTimeRecord> records = userBuyDivineTimeRecordMapper.selectRecordByUidAndNumAndDate(uid,Integer.parseInt(unifiedConfiguration.getLabel()), startDate, endDate);
            divineType.put("isShow",records.size() < 1);
            divineType.put("value",unifiedConfiguration.getValue());
            divineType.put("label",unifiedConfiguration.getLabel());
            divineType.put("configId",unifiedConfiguration.getId());
            divineTypes.add(divineType);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", divineTypes);
        log.info("[queryDivineTimeType][response] {}",resultMap);
        //响应信息
        return ResponseMessage.success(resultMap);
    }


    @Override
    public Map<String, Object> queryUserDivineNum(long uid) {
        log.info("[queryUserDivineNum][requestParam] uid = {}", uid);

        long startDate = TimeUtil.getAppointDayTime(0,0,0,0);
        long endDate = TimeUtil.getAppointDayTime(23,59,59,999);
        //今天占卜记录
        List<UserDivineRecord> todayRecords = userDivineRecordMapper.selectRecordByUidAndDate(uid,startDate,endDate);
        //用户所有占卜记录
        List<UserDivineRecord> userAllRecords = userDivineRecordMapper.selectRecordByUid(uid);
        //响应信息
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("todayNum",todayRecords.size());
        responseData.put("totalNum",userAllRecords.size());
        log.info("[queryUserDivineNum][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }


    /**
     * 查询占卜次数类型的价格
     * @param configId
     * @return
     */
    @Override
    public Map<String, Object> queryDivineTimeTypePrice(long uid, long configId) {
        log.info("[queryDivineTimeTypePrice][requestParam] configId = {}", configId);
        //校验配置数据是否存在，是否已经购买过
        UnifiedConfiguration unifiedConfiguration = unifiedConfigurationMapper.selectByPrimaryKey(configId);
        Map<String, Object> responseData = checkBuyDivineTimeBefor(uid, unifiedConfiguration);
        if (responseData != null){
            return responseData;
        }
        //响应信息
        responseData = new HashMap<String, Object>();
        responseData.put("price",unifiedConfiguration.getValue());
        log.info("[queryDivineTimeTypePrice][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }

    private Map<String, Object> checkBuyDivineTimeBefor(long uid, UnifiedConfiguration unifiedConfiguration){
        if (unifiedConfiguration == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        //校验今日是否已经购买
        long startDate = TimeUtil.getAppointDayTime(0,0,0,0);
        long endDate = TimeUtil.getAppointDayTime(23,59,59,999);
        List<UserBuyDivineTimeRecord> userBuyDivineTimeRecords = userBuyDivineTimeRecordMapper.selectRecordByUidAndNumAndDate(uid,Integer.valueOf(unifiedConfiguration.getLabel()), startDate, endDate);
        if (userBuyDivineTimeRecords.size()>0){
            return ResponseMessage.error(ResponseCode.ALREADY_BUY);
        }
        return null;
    }



    /**
     * 购买占卜次数
     * @param uid
     * @param configId
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> buyDivineTime(long uid, long configId) {
        log.info("[buyDivineTime][requestParam] uid={} , configId={}", uid, configId);
        //校验配置数据是否存在，是否已经购买过
        UnifiedConfiguration unifiedConfiguration = unifiedConfigurationMapper.selectByPrimaryKey(configId);
        Map<String, Object> responseData = checkBuyDivineTimeBefor(uid, unifiedConfiguration);
        if (responseData != null){
            return responseData;
        }

        //保存购买占卜记录
        UserBuyDivineTimeRecord userBuyDivineTimeRecord = new UserBuyDivineTimeRecord();
        userBuyDivineTimeRecord.setBuyNum(Integer.valueOf(unifiedConfiguration.getLabel()));
        userBuyDivineTimeRecord.setBuyPrice(Integer.valueOf(unifiedConfiguration.getValue()));
        userBuyDivineTimeRecord.setUid(uid);
        userBuyDivineTimeRecord.setCreateTime(System.currentTimeMillis()/1000);
        userBuyDivineTimeRecordMapper.insert(userBuyDivineTimeRecord);

        //更新用户购买库存
        UserBuyDivineTimeStorage userBuyDivineTimeStorage = userBuyDivineTimeStorageMapper.selectByUid(uid);
        if (userBuyDivineTimeStorage == null){
            userBuyDivineTimeStorage = new UserBuyDivineTimeStorage();
            userBuyDivineTimeStorage.setTotalDivineNum(Integer.valueOf(unifiedConfiguration.getLabel()));
            userBuyDivineTimeStorage.setAvailableDivineNum(Integer.valueOf(unifiedConfiguration.getLabel()));
            userBuyDivineTimeStorage.setUid(uid);
            userBuyDivineTimeStorage.setCreateTime(System.currentTimeMillis()/1000);
            userBuyDivineTimeStorage.setUpdateTime(userBuyDivineTimeStorage.getCreateTime());
            userBuyDivineTimeStorageMapper.insert(userBuyDivineTimeStorage);
        }else {
            userBuyDivineTimeStorage.setTotalDivineNum(userBuyDivineTimeStorage.getTotalDivineNum() + Integer.valueOf(unifiedConfiguration.getLabel()));
            userBuyDivineTimeStorage.setAvailableDivineNum(userBuyDivineTimeStorage.getAvailableDivineNum() + Integer.valueOf(unifiedConfiguration.getLabel()));
            userBuyDivineTimeStorage.setUpdateTime(System.currentTimeMillis()/1000);
            userBuyDivineTimeStorageMapper.updateByPrimaryKey(userBuyDivineTimeStorage);
        }
        //响应信息
        responseData = new HashMap<String, Object>();
        responseData.put("num",unifiedConfiguration.getLabel());
        log.info("[buyDivineTime][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);
    }

    @Override
    public Map<String, Object> getFragmentsStoragesByTask(long uid, int num) throws Exception  {
        log.info("[getFragmentsStoragesByTask][requestParam] uid = {} , num = {}", uid, num);
        //根据可以完成的勋章进行领取
        List<Medal> medals = medalMapper.selectByIsCompose(true);
        if (medals.size()<1){
            return ResponseMessage.error(ResponseCode.FRAGMENTS_STORAGE_EMPLY);
        }

        //随机生成碎片
        Medal medal = randomDivineMedal(medals);
        long userFragmentsRecordId = getNewUserFragmentsRecord(uid, medal.getId(), 0,num,(byte)2);
        //修改用户勋章碎片库信息
        updateUserFragmentsStorageByDivine(uid, medal.getId(), num);
        //修改勋章碎片库信息
        MedalFragmentsStorage medalFragmentsStorage = updateMedalFragmentsStorageByDivine(medal.getId(), num,1);
        //做碎片预警
        int availableNum = medalFragmentsStorage.getDivineAvailableFragmentsNum() + medalFragmentsStorage.getTaskAvailableFragmentsNum();
        int oldAvailableNum = availableNum + num;
        if (oldAvailableNum > medal.getFragmentsWaringNum() && medal.getFragmentsWaringNum() >= availableNum){
            fragmentswarning(medal);
        }
        //响应信息
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("medalName", medal.getName());
        responseData.put("number", num);
        log.info("[getFragmentsStoragesByTask][response] responseData : {}",responseData);
        return ResponseMessage.success(responseData);

    }



}
