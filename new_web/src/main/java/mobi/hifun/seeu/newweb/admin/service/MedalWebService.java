package mobi.hifun.seeu.newweb.admin.service;

import com.alibaba.dubbo.config.annotation.Reference;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.request.MedalVo;
import mobi.hifun.seeu.newproxy.medal.service.MedalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MedalWebService {

    private static final Logger log = LoggerFactory.getLogger(MedalWebService.class);

    @Reference
    private MedalService medalService;

    public Map<String, Object> saveMedal(MedalVo medalVo){
        log.info("[saveMedal][requestParam] medalVo : "+medalVo.toString());
        try{
            return medalService.insertMedal(medalVo);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }

    public Map<String, Object> inputMedalCdKey(long medalId, String cdKey){
        log.info("[inputMedalCdKey][requestParam] medalId={} , cdKey={}", medalId, cdKey);
        try{
            return medalService.inputMedalCdKey(medalId, cdKey);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResponseMessage.fail();
    }


}
