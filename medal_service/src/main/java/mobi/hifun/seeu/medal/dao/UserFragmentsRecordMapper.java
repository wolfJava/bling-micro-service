package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UserFragmentsRecord;

public interface UserFragmentsRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFragmentsRecord record);

    int insertSelective(UserFragmentsRecord record);

    UserFragmentsRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFragmentsRecord record);

    int updateByPrimaryKey(UserFragmentsRecord record);
}