package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserMedal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMedalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserMedal record);

    int insertSelective(UserMedal record);

    UserMedal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserMedal record);

    int updateByPrimaryKey(UserMedal record);

    List<UserMedal> selectByUidAndMedalId(@Param("uid") Long uid, @Param("medalId")Long medalId);

    List<Map<String, Object>> selectUserAllMedalByUid(long uid);

    List<Map<String, Object>> selectUserMedalTransactionRecordByUid(long uid);
}