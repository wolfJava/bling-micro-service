package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserDivineRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDivineRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDivineRecord record);

    int insertSelective(UserDivineRecord record);

    UserDivineRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDivineRecord record);

    int updateByPrimaryKey(UserDivineRecord record);

    /**
     * 根据日期查询占卜类型信息
     * @param uid
     * @param divineType
     * @return
     */
    List<UserDivineRecord> queryRecordByUidAndDivineTypeAndDate(@Param("uid") Long uid, @Param("divineType")Byte divineType, @Param("startDate")long startDate, @Param("endDate")long endDate);


    /**
     * 查询某天的占卜记录
     * @param uid
     * @param today
     * @return
     */
    List<UserDivineRecord> selectRecordByUidAndDate(@Param("uid") Long uid, @Param("startDate")long startDate, @Param("endDate")long endDate);

    /**
     * 查询查询用户所有的占卜记录
     * @param uid
     * @return
     */
    List<UserDivineRecord> selectRecordByUid(@Param("uid") Long uid);


}