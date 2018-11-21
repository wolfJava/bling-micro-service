package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserBuyDivineTimeStorage;

public interface UserBuyDivineTimeStorageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserBuyDivineTimeStorage record);

    int insertSelective(UserBuyDivineTimeStorage record);

    UserBuyDivineTimeStorage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserBuyDivineTimeStorage record);

    int updateByPrimaryKey(UserBuyDivineTimeStorage record);

    UserBuyDivineTimeStorage selectByUid(long uid);

}