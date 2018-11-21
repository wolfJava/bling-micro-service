package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserFragmentsStorage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserFragmentsStorageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFragmentsStorage record);

    int insertSelective(UserFragmentsStorage record);

    UserFragmentsStorage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFragmentsStorage record);

    int updateByPrimaryKey(UserFragmentsStorage record);

    UserFragmentsStorage selectByUidAndMedalId(@Param("uid") long uid, @Param("medalId")long medalId);

    List<Map<String, Object>> selectUserFragmentsByUid(long uid);

}