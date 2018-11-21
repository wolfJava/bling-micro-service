package mobi.hifun.seeu.newweb.api.api;

import com.alibaba.dubbo.common.URL;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newweb.api.service.UserDivineApiService;
import mobi.hifun.seeu.newweb.api.service.UserMedalApiService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/medal/user")
public class UserMedalApi {

    private static final Logger log = LoggerFactory.getLogger(UserMedalApi.class);

    @Autowired
    private UserMedalApiService userMedalApiService;

    /**
     * 查询用户所有勋章
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @GetMapping("/queryUserAllMedal")
    public Map<String,Object> queryUserAllMedal(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[queryUserAllMedal] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.queryUserAllMedal(uid);
        log.info("[queryUserAllMedal] response : {}", result);
        return result;
    }

    /**
     * 合成勋章
     * @param request
     * @param response
     * @param medalId
     * @return
     */
    @PostMapping("/composeMedal")
    public Map<String,Object> composeMedal(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(name = "uid") long uid,
                                           @RequestParam(name = "medalId") long medalId){
        String url = URL.decode(request.getQueryString());
        log.info("[composeMedal] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.composeMedal(uid, medalId);
        log.info("[composeMedal] response : {}", result);
        return result;
    }


    /**
     * 购买勋章前校验，如果全部通过返回勋章价格，供内部使用
     * @param request
     * @param response
     * @param uid
     * @param medalId
     * @return
     */
    @GetMapping("/checkBuyMedalBefore")
    public Map<String,Object> checkBuyMedalBefore(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam(name = "uid") long uid,
                                                  @RequestParam(name = "medalId") long medalId){
        String url = URL.decode(request.getQueryString());
        log.info("[checkBuyMedalBefore] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.checkBuyMedalBefore(uid, medalId);
        log.info("[checkBuyMedalBefore] response : {}", result);
        return result;
    }

    /**
     * 购买勋章，供内部使用
     * @param request
     * @param response
     * @param uid
     * @param medalId
     * @return
     */
    @PostMapping("/buyMedal")
    public Map<String,Object> buyMedal(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(name = "uid") long uid,
                                       @RequestParam(name = "medalId") long medalId){
        String url = URL.decode(request.getQueryString());
        log.info("[buyMedal] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.buyMedal(uid, medalId);
        log.info("[buyMedal] response : {}", result);
        return result;
    }


    /**
     * 我的勋章详细信息
     * @param request
     * @param response
     * @param uid
     * @param medalId
     * @param userMedalId
     * @return
     */
    @GetMapping("/queryMineMedalDetail")
    public Map<String,Object> queryMineMedalDetail(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam(name = "uid") long uid,
                                                  @RequestParam(name = "medalId") long medalId,
                                                  @RequestParam(required = false,name = "userMedalId") Long userMedalId){
        String url = URL.decode(request.getQueryString());
        log.info("[queryMineMedalDetail] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.queryMineMedalDetail(uid,medalId,userMedalId);
        log.info("[queryMineMedalDetail] response : {}", result);
        return result;
    }


    /**
     * 查询勋章单独信息
     * @param request
     * @param response
     * @param medalId
     * @return
     */
    @GetMapping("/queryIndexMedalDetail")
    public Map<String,Object> queryIndexMedalDetail(HttpServletRequest request, HttpServletResponse response,
                                                        @RequestParam(name = "uid") long uid,
                                                        @RequestParam(name = "medalId") long medalId,
                                                        @RequestParam(name = "isCanReceiveByTask") boolean isCanReceiveByTask){
        String url = URL.decode(request.getQueryString());
        log.info("[queryIndexMedalDetail] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.queryIndexMedalDetail(uid,medalId,isCanReceiveByTask);
        log.info("[queryIndexMedalDetail] response : {}", result);
        return result;
    }




    /**
     * 查询用户勋章交易记录
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @GetMapping("/queryUserMedalObtainRecord")
    public Map<String,Object> queryUserMedalObtainRecord(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[queryUserMedalObtainRecord] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.queryUserMedalObtainRecord(uid);
        log.info("[queryUserMedalObtainRecord] response : {}", result);
        return result;
    }

    /**
     * 查询用户勋章记录详情
     * @param request
     * @param response
     * @param userMedalId
     * @return
     */
    @GetMapping("/queryUserMedalObtainRecordDetail")
    public Map<String,Object> queryUserMedalObtainRecordDetail(HttpServletRequest request, HttpServletResponse response,
                                                         @RequestParam(name = "userMedalId") long userMedalId){
        String url = URL.decode(request.getQueryString());
        log.info("[queryUserMedalObtainRecordDetail] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.queryUserMedalObtainRecordDetail(userMedalId);
        log.info("[queryUserMedalObtainRecordDetail] response : {}", result);
        return result;
    }


    /**
     * 完成任务获得勋章
     * @param request
     * @param response
     * @param uid
     * @param medalId
     * @return
     */
    @PostMapping("/completeTaskMedal")
    public Map<String,Object> completeTaskMedal(HttpServletRequest request, HttpServletResponse response,
                                                @RequestParam(name = "uid") long uid,
                                                @RequestParam(name = "medalId") long medalId){
        String url = URL.decode(request.getQueryString());
        log.info("[completeTaskMedal] queryString：{}",url);
        Map<String, Object> result = userMedalApiService.completeTaskMedal(uid, medalId);
        log.info("[completeTaskMedal] response : {}", result);
        return result;
    }


    /**
     * 根据cdkey获得勋章
     * @param request
     * @param response
     * @param uid
     * @param medalId
     * @param cdKey
     * @return
     */
    @PostMapping("/obtainMedal")
    public Map<String,Object> obtainMedal(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(name = "uid") long uid,
                                           @RequestParam(name = "medalId") long medalId,
                                           @RequestParam(name = "cdKey") String cdKey){
        String url = URL.decode(request.getQueryString());
        log.info("[obtainMedal] queryString：{}",url);
        if (StringUtils.isBlank(cdKey)){
            return ResponseMessage.errorParamEmply("cdKey");
        }
        Map<String, Object> result = userMedalApiService.obtainMedal(uid, medalId,cdKey);
        log.info("[obtainMedal] response : {}", result);
        return result;
    }





}
