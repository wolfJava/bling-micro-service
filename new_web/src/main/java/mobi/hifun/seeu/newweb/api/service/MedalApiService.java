package mobi.hifun.seeu.newweb.api.service;

import com.alibaba.dubbo.config.annotation.Reference;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.service.MedalService;
import mobi.hifun.seeu.newweb.admin.service.MedalWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MedalApiService {

    private static final Logger log = LoggerFactory.getLogger(MedalWebService.class);


    @Reference
    private MedalService medalService;

    public Map<String, Object> queryMedalIndex(){
        try{
            return medalService.queryMedalIndexInfo();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


    public Map<String, Object> queryResourcesPackageUrl(String appVersion, String userAgent){
        try{
            return medalService.queryResourcesPackageUrl(appVersion, userAgent);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }




}
