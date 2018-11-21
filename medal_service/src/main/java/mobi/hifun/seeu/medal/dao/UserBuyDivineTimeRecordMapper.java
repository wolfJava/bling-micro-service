package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserBuyDivineTimeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserBuyDivineTimeRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBuyDivineTimeRecord record);

    int insertSelective(UserBuyDivineTimeRecord record);

    UserBuyDivineTimeRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBuyDivineTimeRecord record);

    int updateByPrimaryKey(UserBuyDivineTimeRecord record);

    List<UserBuyDivineTimeRecord> selectRecordByUidAndNumAndDate(@Param("uid") Long uid,@Param("num") int num,  @Param("startDate")long startDate, @Param("endDate")long endDate);



}