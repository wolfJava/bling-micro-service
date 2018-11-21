package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.MedalFragmentsStorage;

public interface MedalFragmentsStorageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MedalFragmentsStorage record);

    int insertSelective(MedalFragmentsStorage record);

    MedalFragmentsStorage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MedalFragmentsStorage record);

    int updateByPrimaryKey(MedalFragmentsStorage record);

    MedalFragmentsStorage selectByMedalId(Long medalId);

}