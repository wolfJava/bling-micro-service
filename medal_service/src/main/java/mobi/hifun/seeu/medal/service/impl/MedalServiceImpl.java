package mobi.hifun.seeu.medal.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import mobi.hifun.seeu.medal.dao.*;
import mobi.hifun.seeu.medal.entity.*;
import mobi.hifun.seeu.newproxy.medal.common.ResponseCode;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.request.MedalVo;
import mobi.hifun.seeu.newproxy.medal.service.MedalService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service(loadbalance = "roundrobin", timeout = 50000,cluster = "failfast" ,interfaceName = "mobi.hifun.seeu.newproxy.medal.service.MedalService")
public class MedalServiceImpl implements MedalService {

    private static final Logger log = LoggerFactory.getLogger(MedalServiceImpl.class);

    @Autowired
    private MedalMapper medalMapper;

    @Autowired
    private MedalFragmentsStorageMapper medalFragmentsStorageMapper;

    @Autowired
    private MedalCdKeyMapper medalCdKeyMapper;

    @Autowired
    private MedalAwardProbabilityMapper medalAwardProbabilityMapper;

    @Autowired
    private UnifiedConfigurationMapper unifiedConfigurationMapper;

    /**
     * 添加勋章
     * @param medalVo
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> insertMedal(MedalVo medalVo) {
        log.info("[insertMedal][requestParam] {}",medalVo.toString());
        //step1.保存勋章主信息
        Medal medal = medalMapper.selectByName(medalVo.getName());
        if (medal != null){
            return ResponseMessage.error(ResponseCode.MEDAL_NAME_ALREADY);
        }
        medal = new Medal();
        BeanUtils.copyProperties(medalVo,medal);
        medal.setCreateTime(System.currentTimeMillis()/1000);
        medal.setUpdateTime(medal.getCreateTime());
        medalMapper.insert(medal);

        //step2.保存勋章总量以及碎片总量信息
        MedalFragmentsStorage medalFragmentsStorage = new MedalFragmentsStorage();
        medalFragmentsStorage.setMedalId(medal.getId());
        medalFragmentsStorage.setTotalMedalNum(medal.getCirculation());
        medalFragmentsStorage.setAvailableMedalNum(medal.getCirculation() - medal.getReserveNum());
        medalFragmentsStorage.setReserveMedalNum(medal.getReserveNum() );

        if (medal.getIsCompose()){
            medalFragmentsStorage.setTotalFragmentsNum(medal.getCirculation() * medal.getNeedFragmentsNum());
            medalFragmentsStorage.setTaskAvailableFragmentsNum(medal.getTaskNum() * medal.getNeedFragmentsNum());
            medalFragmentsStorage.setDivineAvailableFragmentsNum(medal.getDivineNum() * medal.getNeedFragmentsNum());
            medalFragmentsStorage.setReserveFragmentsNum(medal.getReserveNum() * medal.getNeedFragmentsNum());
        }
        medalFragmentsStorage.setCreateTime(System.currentTimeMillis()/1000);
        medalFragmentsStorage.setUpdateTime(medalFragmentsStorage.getCreateTime());
        medalFragmentsStorageMapper.insert(medalFragmentsStorage);
        //插入勋章默认抽取几率
        if(medal.getIsCompose()){
            insertMedalAwardProbabilityDefault(medal.getId());
        }
        return ResponseMessage.success();
    }

    /**
     * 插入默认的勋章抽取概率
     * @param medalId
     */
    private void insertMedalAwardProbabilityDefault(long medalId){
        List<MedalAwardProbability> records = new ArrayList<>();
        int size = 4;
        for (int i = 0; i < size ; i++) {
            MedalAwardProbability record = new MedalAwardProbability();
            record.setMedalId(medalId);
            record.setNum(i+1);
            record.setProbability( (size - i) * 10);
            record.setCreateTime(System.currentTimeMillis()/1000);
            record.setUpdateTime(record.getCreateTime());
            records.add(record);

        }
        medalAwardProbabilityMapper.insertBatch(records);
    }

    /**
     * 勋章首页信息展示
     * @return
     */
    @Override
    public Map<String, Object> queryMedalIndexInfo() {
        Map<String, Object> medalDataMap = new HashMap<String, Object>();
        List<Medal> medalList = medalMapper.selectByDel(false);
        for (Medal medal : medalList){
            List<Map<String, Object>> medals = (List<Map<String, Object>>) medalDataMap.get(medal.getMedalType().toString());


            if (medals == null){
                medals = new ArrayList<Map<String, Object>>();
            }
            Map<String, Object> medalData = new HashMap<String, Object>();
            medalData.put("medalId",medal.getId());
            medalData.put("medalName", medal.getName());
            medalData.put("cellTime", StringUtils.isBlank(medal.getSellCaption()) ? "" : "/"+medal.getSellCaption()+"/");
            medalData.put("circulation", medal.getCirculation());
            medals.add(medalData);
            medalDataMap.put(medal.getMedalType().toString(),medals);
        }
        //查询配置信息列表
        List<UnifiedConfiguration> configurations = unifiedConfigurationMapper.selectByTypeOrderByValueAsc("medal_type");
        List<Map<String, Object>> medalDatas = new ArrayList<Map<String, Object>>();
        for (UnifiedConfiguration configuration : configurations) {
            Map<String, Object> medalData = new HashMap<String, Object>();
            medalData.put("typeId", configuration.getValue());
            medalData.put("typeName", configuration.getLabel());
            List<Map<String, Object>> medalListMap = (List<Map<String, Object>>) medalDataMap.get(configuration.getValue());
            medalData.put("medalList", medalListMap == null ? new ArrayList<>():medalListMap);
            medalData.put("isShowDefault", medalListMap == null);
            medalDatas.add(medalData);
        }
        Map<String, Object> resultMap = new HashMap<>();

        UnifiedConfiguration unifiedConfiguration = unifiedConfigurationMapper.selectByTypeAndLabel("medal_index_layout","layout");
        if (unifiedConfiguration != null){
            resultMap.put("layoutType", unifiedConfiguration.getValue());
        }else {
            resultMap.put("layoutType", unifiedConfiguration.getValue());
        }
        resultMap.put("list", medalDatas);
        log.info("[queryMedalIndexInfo][response] {}",resultMap);
        return ResponseMessage.success(resultMap);
    }




    /**
     * 录入勋章cdkey
     * @param medalId
     * @param cdKey
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> inputMedalCdKey(long medalId, String cdKey) {
        //校验勋章是否存在，是否可以根据cdkey获取
        Medal medal = medalMapper.selectByPrimaryKey(medalId);
        if (medal == null){
            return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
        }
        if (!medal.getIsObtain()){
            return ResponseMessage.error(ResponseCode.MEDAL_NOT_SUPPORT_CDKEY);
        }
        //校验剩余数量是否为空
        MedalFragmentsStorage storage = medalFragmentsStorageMapper.selectByMedalId(medalId);
        if (storage.getAvailableMedalNum()<0){
            return ResponseMessage.error(ResponseCode.MEDAL_AVAILABLE_NUM_EMPLY);
        }
        //保存cdkey信息
        //保存cdkey信息
        MedalCdKey medalCdKey = medalCdKeyMapper.selectByCdKeyAndMedalId(cdKey,medalId);
        if (medalCdKey != null){
            return ResponseMessage.error(ResponseCode.CDKEY_ALREADY);
        }
        medalCdKey = new MedalCdKey();
        medalCdKey.setMedalId(medalId);
        medalCdKey.setCkKey(cdKey);
        medalCdKey.setCreateTime(System.currentTimeMillis()/1000);
        medalCdKey.setUpdateTime(medalCdKey.getCreateTime());
        medalCdKeyMapper.insert(medalCdKey);
        //更新勋章库信息
        storage.setAvailableMedalNum(storage.getAvailableMedalNum() - 1 );
        storage.setUsedMedalNum(storage.getUsedMedalNum()+1);
        medalFragmentsStorageMapper.updateByPrimaryKey(storage);
        return ResponseMessage.success();
    }

    @Override
    public Map<String, Object> queryResourcesPackageUrl(String appVersion, String userAgent) {
        log.info("[queryResourcesPackageUrl][requestParam] appVersion = {} , userAgent = {}", appVersion, userAgent);
        UnifiedConfiguration unifiedConfiguration = unifiedConfigurationMapper.selectByTypeAndLabel("resources-" + appVersion,userAgent);
        if (unifiedConfiguration == null) {
            unifiedConfiguration = unifiedConfigurationMapper.selectByTypeAndLabel("resources-default",userAgent);
            if (unifiedConfiguration == null) {
                return ResponseMessage.error(ResponseCode.NO_DATA_FOUND);
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resourcesUrl", unifiedConfiguration.getValue());
        log.info("[queryMedalIndexInfo][response] {}",resultMap);
        return ResponseMessage.success(resultMap);

    }


}