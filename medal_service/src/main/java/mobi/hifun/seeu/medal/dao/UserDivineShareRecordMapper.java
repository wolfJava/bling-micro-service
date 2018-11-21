package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserDivineShareRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDivineShareRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserDivineShareRecord record);

    int insertSelective(UserDivineShareRecord record);

    UserDivineShareRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDivineShareRecord record);

    int updateByPrimaryKey(UserDivineShareRecord record);


    /**
     * 根据日期查询分享记录
     * @param uid
     * @param startDate
     * @param endDate
     * @return
     */
    List<UserDivineShareRecord> queryRecordByUidAndDate(@Param("uid") Long uid, @Param("startDate")long startDate, @Param("endDate")long endDate);



}