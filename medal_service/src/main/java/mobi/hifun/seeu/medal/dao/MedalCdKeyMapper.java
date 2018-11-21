package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.MedalCdKey;
import org.apache.ibatis.annotations.Param;

public interface MedalCdKeyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MedalCdKey record);

    int insertSelective(MedalCdKey record);

    MedalCdKey selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MedalCdKey record);

    int updateByPrimaryKey(MedalCdKey record);

    MedalCdKey selectByCdKeyAndMedalId(@Param("ckKey") String ckKey, @Param("medalId") long medalId);

}