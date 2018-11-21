package mobi.hifun.seeu.newweb.api.api;

import com.alibaba.dubbo.common.URL;
import mobi.hifun.seeu.newweb.api.service.MedalApiService;
import mobi.hifun.seeu.newweb.api.service.UserDivineApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/medal/divine")
public class UserDivineApi {

    private static final Logger log = LoggerFactory.getLogger(UserDivineApi.class);

    @Autowired
    private UserDivineApiService userDivineApiService;

    /**
     * 查询用户占卜次数
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @GetMapping("/queryUserDivineTime")
    public Map<String,Object> queryUserDivineTime(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[queryIndexMedalById] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.queryUserDivineTime(uid);
        log.info("[queryUserDivineTime] response : {}", result);
        return result;
    }

    /**
     * 用户占卜
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @PostMapping("/userDivine")
    public Map<String,Object> userDivine(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[userDivine] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.userDivine(uid);
        log.info("[userDivine] response : {}", result);
        return result;
    }

    /**
     * 添加分享记录
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @PostMapping("/addShareRecord")
    public Map<String,Object> addShareRecord(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[addShareRecord] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.addShareRecord(uid);
        log.info("[addShareRecord] response : {}", result);
        return result;
    }

    /**
     * 用户购买占卜按钮列表
     */
    @GetMapping("/queryDivineTimeType")
    public Map<String,Object> queryDivineTimeType(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[queryDivineTimeType] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.queryDivineTimeType(uid);
        log.info("[queryDivineTimeType] response : {}", result);
        return result;
    }

    /**
     * 查询购买按钮的价格
     * @param request
     * @param response
     * @param uid
     * @param configId
     * @return
     */
    @GetMapping("/queryDivineTimeTypePrice")
    public Map<String,Object> queryDivineTimeTypePrice(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestParam(name = "uid") long uid,
                                                       @RequestParam(name = "configId") long configId){
        String url = URL.decode(request.getQueryString());
        log.info("[queryDivineTimeType] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.queryDivineTimeTypePrice(uid, configId);
        log.info("[queryDivineTimeType] response : {}", result);
        return result;
    }

    /**
     * 购买占卜次数
     * @param request
     * @param response
     * @param uid
     * @param configId
     * @return
     */
    @GetMapping("/buyDivineTime")
    public Map<String,Object> buyDivineTime(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(name = "uid") long uid,
                                            @RequestParam(name = "configId") long configId){
        String url = URL.decode(request.getQueryString());
        log.info("[buyDivineTime] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.buyDivineTime(uid, configId);
        log.info("[buyDivineTime] response : {}", result);
        return result;
    }


    /**
     * 查询用户占卜次数信息
     * @param request
     * @param response
     * @param uid
     * @return
     */
    @GetMapping("/queryUserDivineNum")
    public Map<String,Object> queryUserDivineNum(HttpServletRequest request, HttpServletResponse response,
                                                 @RequestParam(name = "uid") long uid){
        String url = URL.decode(request.getQueryString());
        log.info("[queryUserDivineNum] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.queryUserDivineNum(uid);
        log.info("[queryUserDivineNum] response : {}", result);
        return result;
    }


    /**
     * 任务获得碎片
     * @param request
     * @param response
     * @param uid
     * @param num
     * @return
     */
    @PostMapping("/getFragmentsStoragesByTask")
    public Map<String,Object> getFragmentsStoragesByTask(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(name = "uid") long uid,
                                            @RequestParam(name = "num") int num){
        String url = URL.decode(request.getQueryString());
        log.info("[getFragmentsStoragesByTask] queryString：{}",url);
        Map<String, Object> result = userDivineApiService.getFragmentsStoragesByTask(uid, num);
        log.info("[getFragmentsStoragesByTask] response : {}", result);
        return result;
    }



}
