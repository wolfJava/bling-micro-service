package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.Medal;

import java.util.List;

public interface MedalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Medal record);

    int insertSelective(Medal record);

    Medal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Medal record);

    int updateByPrimaryKey(Medal record);

    Medal selectByName(String name);

    List<Medal> selectByDel(boolean isDel);

    List<Medal> selectByMedalType(byte medalType);

    List<Medal> selectByIsCompose(boolean isCompose);


}