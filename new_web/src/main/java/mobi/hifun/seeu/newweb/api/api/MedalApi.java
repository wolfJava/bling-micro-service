package mobi.hifun.seeu.newweb.api.api;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newweb.api.service.MedalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/medal")
public class MedalApi {

    private static final Logger log = LoggerFactory.getLogger(MedalApi.class);

    @Autowired
    private MedalApiService medalApiService;


    /**
     * 查询博物馆首页信息
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/queryMedalIndex")
    public Map<String,Object> queryMedalIndex(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> result = medalApiService.queryMedalIndex();
        log.info("[queryMedalIndex] response : {}", result);
        return result;
    }


    /**
     * 获取勋章资源路径
     * @param request
     * @param response
     * @param appVersion
     * @param userAgent
     * @return
     */
    @GetMapping("/queryResourcesPackageUrl")
    public Map<String,Object> queryResourcesPackageUrl(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestParam(name = "appVersion") String appVersion,
                                                       @RequestParam(name = "userAgent") String userAgent){
        String url = URL.decode(request.getQueryString());
        log.info("[queryResourcesPackageUrl] queryString：{}",url);
        if (StringUtils.isBlank(appVersion)){
            return ResponseMessage.errorParamEmply("appVersion");
        }
        if (StringUtils.isBlank(userAgent)){
            return ResponseMessage.errorParamEmply("userAgent");
        }
        Map<String, Object> result = medalApiService.queryResourcesPackageUrl(appVersion, userAgent);
        log.info("[queryResourcesPackageUrl] response : {}", result);
        return result;
    }



}
