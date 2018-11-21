package mobi.hifun.seeu.newproxy.medal.service;

import mobi.hifun.seeu.newproxy.medal.request.MedalVo;

import java.util.Map;

/**
 * 勋章接口
 * @author huhao 2018年08月23日12:00:30
 */
public interface MedalService {

    /**
     * 插入勋章信息
     * @param medalVo
     * @throws Exception
     */
    public Map<String, Object> insertMedal(MedalVo medalVo);

    /**
     * 查询勋章首页信息，只展示上架的
     * @return
     */
    public Map<String, Object> queryMedalIndexInfo();


    /**
     * 录入cdKey信息
     * @param cdKey
     * @return
     */
    public Map<String, Object> inputMedalCdKey(long medalId, String cdKey);


    /**
     * 获取勋章资源地址
     * @param appVersion
     * @param userAgent
     * @return
     */
    public Map<String, Object> queryResourcesPackageUrl(String appVersion, String userAgent);

}
