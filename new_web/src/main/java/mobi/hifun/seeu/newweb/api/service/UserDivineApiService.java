package mobi.hifun.seeu.newweb.api.service;

import com.alibaba.dubbo.config.annotation.Reference;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.service.UserDivineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDivineApiService {

    private static final Logger log = LoggerFactory.getLogger(UserDivineApiService.class);

    @Reference
    private UserDivineService userDivineService;

    public Map<String, Object> queryUserDivineTime(long uid){
        log.info("[queryUserDivineTime][requestParam] uid : {}",uid);
        try{
            return userDivineService.queryUserDivineTime(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> userDivine(long uid){
        log.info("[userDivine][requestParam] uid : {}",uid);
        try{
            return userDivineService.userDivine(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> addShareRecord(long uid){
        log.info("[addShareRecord][requestParam] uid : {}",uid);
        try{
            return userDivineService.addShareRecord(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> queryUserDivineNum(long uid){
        log.info("[queryUserDivineNum][requestParam] uid : {}",uid);
        try{
            return userDivineService.queryUserDivineNum(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> queryDivineTimeType(long uid){
        log.info("[queryDivineTimeType]");
        try{
            return userDivineService.queryDivineTimeType(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> queryDivineTimeTypePrice(long uid, long configId){
        log.info("[queryDivineTimeTypePrice][requestParam] uid={} , configId={}", uid, configId);
        try{
            return userDivineService.queryDivineTimeTypePrice(uid, configId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> buyDivineTime(long uid, long configId){
        log.info("[buyDivineTime][requestParam] uid={} , configId={}", uid, configId);
        try{
            return userDivineService.buyDivineTime(uid,configId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> getFragmentsStoragesByTask(long uid, int num){
        log.info("[getFragmentsStoragesByTask][requestParam] uid={} , num={}", uid, num);
        try{
            return userDivineService.getFragmentsStoragesByTask(uid, num);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

}
