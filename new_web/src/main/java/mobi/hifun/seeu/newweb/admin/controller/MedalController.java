package mobi.hifun.seeu.newweb.admin.controller;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import mobi.hifun.seeu.newproxy.medal.common.ResponseCode;
import mobi.hifun.seeu.newproxy.medal.common.ResponseMessage;
import mobi.hifun.seeu.newproxy.medal.request.MedalVo;
import mobi.hifun.seeu.newweb.admin.service.MedalWebService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/medal")
public class MedalController {

    private static final Logger log = LoggerFactory.getLogger(MedalController.class);

    @Autowired
    private MedalWebService medalWebService;

    /**
     * 添加勋章
     * @param request
     * @param response
     * @param name
     * @param thumbnailUrl
     * @param viewUrl
     * @param circulation
     * @param medalType
     * @param price
     * @param needFragmentsNum
     * @param sort
     * @param introduceCaption
     * @param obtainRouteCaption
     * @param rewardCaption
     * @param destroyCaption
     * @param isDel
     * @param isTransaction
     * @param isDestory
     * @param isCompose
     * @param isObtain
     * @param divineNum
     * @param reserveNum
     * @param taskNum
     * @param fragmentsWaringNum
     * @return
     */
    @PostMapping("/addMedal")
    private Map<String,Object> saveMedal(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(required = true,name = "name") String name,
                                         @RequestParam(required = false,name = "thumbnailUrl") String thumbnailUrl,
                                         @RequestParam(required = false,name = "viewUrl") String viewUrl,
                                         @RequestParam(required = true,name = "circulation") int circulation,
                                         @RequestParam(required = true,name = "medalType") byte medalType,
                                         @RequestParam(required = true,name = "price") int price,
                                         @RequestParam(required = false,name = "needFragmentsNum") Integer needFragmentsNum,
                                         @RequestParam(required = true,name = "sort") int sort,
                                         @RequestParam(required = true,name = "introduceCaption") String introduceCaption,
                                         @RequestParam(required = true,name = "obtainRouteCaption") String obtainRouteCaption,
                                         @RequestParam(required = true,name = "rewardCaption") String rewardCaption,
                                         @RequestParam(required = true,name = "destroyCaption") String destroyCaption,
                                         @RequestParam(required = true,name = "isDel") boolean isDel,
                                         @RequestParam(required = true,name = "isTransaction") boolean isTransaction,
                                         @RequestParam(required = true,name = "isDestory") boolean isDestory,
                                         @RequestParam(required = true,name = "isCompose") boolean isCompose,
                                         @RequestParam(required = true,name = "isObtain") boolean isObtain,
                                         @RequestParam(required = true,name = "isSell") boolean isSell,
                                         @RequestParam(required = true,name = "divineNum") int divineNum,
                                         @RequestParam(required = true,name = "reserveNum") int reserveNum,
                                         @RequestParam(required = true,name = "taskNum") int taskNum,
                                         @RequestParam(required = true,name = "fragmentsWaringNum") int fragmentsWaringNum,
                                         @RequestParam(required = false,name = "sellCaption", defaultValue = "") String sellCaption,
                                         @RequestParam(required = false,name = "sellStartTime", defaultValue = "") String sellStartTime,
                                         @RequestParam(required = false,name = "sellEndTime", defaultValue = "") String sellEndTime) throws ParseException {
        String url = URL.decode(request.getQueryString());
        log.info("[saveMedal] queryString = {}",url);
        //校验勋章名称是否为空
        if (StringUtils.isBlank(name)){
            return ResponseMessage.errorParamEmply("name");
        }

        MedalVo medalVo = new MedalVo();

        if (isSell){

            if (StringUtils.isBlank(sellStartTime)){
                return ResponseMessage.errorParamEmply("sellStartTime");
            }
            if (StringUtils.isBlank(sellEndTime)){
                return ResponseMessage.errorParamEmply("sellEndTime");
            }
            Date sellStartDate = DateUtils.parseDate(sellStartTime,"yyyy-MM-dd");
            medalVo.setSellStartTime(sellStartDate.getTime()/1000);

            Date sellEndDate = DateUtils.parseDate(sellEndTime,"yyyy-MM-dd");
            medalVo.setSellEndTime(sellEndDate.getTime()/1000);

        }

        int totalNum = divineNum + reserveNum + taskNum;
        if (totalNum != circulation){
            return ResponseMessage.error(ResponseCode.MEDAL_NUM_INVALID.getCode(),ResponseCode.MEDAL_NUM_INVALID.getMessage());
        }
        //校验需要合成数量
        if (isCompose){
            if (needFragmentsNum == null || needFragmentsNum < 1){
                return ResponseMessage.error(ResponseCode.COMPOSE_FRAGMENTS_NUM_INVALID.getCode(),ResponseCode.COMPOSE_FRAGMENTS_NUM_INVALID.getMessage());
            }
            medalVo.setNeedFragmentsNum(needFragmentsNum);
        }

        medalVo.setName(name);
        medalVo.setThumbnailUrl(thumbnailUrl);
        medalVo.setViewUrl(viewUrl);
        medalVo.setCirculation(circulation);
        medalVo.setMedalType(medalType);
        medalVo.setPrice(price);
        medalVo.setSort(sort);
        medalVo.setIntroduceCaption(introduceCaption);
        medalVo.setObtainRouteCaption(obtainRouteCaption);
        medalVo.setRewardCaption(rewardCaption);
        medalVo.setDestroyCaption(destroyCaption);
        medalVo.setIsDel(isDel);
        medalVo.setIsTransaction(isTransaction);
        medalVo.setIsDestory(isDestory);
        medalVo.setIsCompose(isCompose);
        medalVo.setIsSell(isSell);
        medalVo.setTaskNum(taskNum);
        medalVo.setReserveNum(reserveNum);
        medalVo.setDivineNum(divineNum);
        medalVo.setIsObtain(isObtain);
        medalVo.setSellCaption(sellCaption);
        medalVo.setFragmentsWaringNum(fragmentsWaringNum);
        Map<String, Object> result = medalWebService.saveMedal(medalVo);
        log.info("[saveMedal] response : {}", result);
        return result;
    }

    /**
     * 录入勋章CDKEY
     * @param request
     * @param response
     * @param medalId
     * @param cdKey
     * @return
     */
    @PostMapping("/inputMedalCdKey")
    private Map<String,Object> inputMedalCdKey(HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam(name = "medalId") long medalId,
                                               @RequestParam(name = "cdKey") String cdKey){
        String url = URL.decode(request.getQueryString());
        log.info("[inputMedalCdKey] queryString：{}",url);
        if (org.apache.commons.lang3.StringUtils.isBlank(cdKey)){
            return ResponseMessage.errorParamEmply("cdKey");
        }
        Map<String, Object> result = medalWebService.inputMedalCdKey( medalId, cdKey);
        log.info("[inputMedalCdKey] response : {}", result);
        return result;
    }




}
