package mobi.hifun.seeu.newweb.api.service;

import com.alibaba.dubbo.config.annotation.Reference;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.service.UserMedalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserMedalApiService {


    private static final Logger log = LoggerFactory.getLogger(UserMedalApiService.class);

    @Reference
    private UserMedalService userMedalService;


    public Map<String, Object> queryUserAllMedal(long uid){
        log.info("[queryUserAllMedal][requestParam] uid : {}",uid);
        try{
            return userMedalService.queryUserAllMedal(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> queryUserMedalObtainRecord(long uid){
        log.info("[queryUserMedalObtainRecord][requestParam] uid : {}",uid);
        try{
            return userMedalService.queryUserMedalObtainRecord(uid);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> queryMineMedalDetail(long uid, long medalId, Long userMedalId){
        log.info("[queryMineMedalDetail][requestParam] medalId={}, uid={}",medalId, uid);
        try{
            return userMedalService.queryMineMedalDetail(uid,medalId,userMedalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> queryIndexMedalDetail(long uid, long medalId, boolean isCanReceiveByTask){
        log.info("[queryMineMedalDetail][requestParam] medalId={}, uid={}",medalId, uid);
        try{
            return userMedalService.queryIndexMedalDetail(uid,medalId,isCanReceiveByTask);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> composeMedal(long uid,long medalId){
        log.info("[composeMedal][requestParam] medalId : {}",medalId);
        try{
            return userMedalService.composeMedal(uid,medalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> obtainMedal(long uid,long medalId, String cdKey){
        log.info("[obtainMedal][requestParam] medalId : {}",medalId);
        try{
            return userMedalService.obtainMedal(uid, medalId, cdKey);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> queryUserMedalObtainRecordDetail(long userMedalId){
        log.info("[queryUserMedalObtainRecordDetail][requestParam] userMedalId={}", userMedalId);
        try{
            return userMedalService.queryUserMedalObtainRecordDetail(userMedalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }



    public Map<String, Object> completeTaskMedal(long uid, long medalId){
        log.info("[completeTaskMedal][requestParam] uid={} , medalId={}", uid, medalId);
        try{
            return userMedalService.completeTaskMedal(uid, medalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> checkBuyMedalBefore(long uid,long medalId){
        log.info("[checkBuyMedalBefore][requestParam] medalId : {}",medalId);
        try{
            return userMedalService.checkBuyMedalBefore(uid, medalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> buyMedal(long uid, long medalId){
        log.info("[buyMedal][requestParam] uid={} , medalId={}", uid, medalId);
        try{
            return userMedalService.buyMedal(uid, medalId);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


}
