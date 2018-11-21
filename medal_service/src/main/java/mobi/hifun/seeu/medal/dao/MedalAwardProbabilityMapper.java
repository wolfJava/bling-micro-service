package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.MedalAwardProbability;

import java.util.List;

public interface MedalAwardProbabilityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MedalAwardProbability record);

    int insertSelective(MedalAwardProbability record);

    MedalAwardProbability selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MedalAwardProbability record);

    int updateByPrimaryKey(MedalAwardProbability record);

    void insertBatch(List<MedalAwardProbability> records);

    List<MedalAwardProbability> selectLessThanNum(int number);

}