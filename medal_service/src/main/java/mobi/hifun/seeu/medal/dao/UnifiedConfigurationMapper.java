package mobi.hifun.seeu.medal.dao;

import mobi.hifun.seeu.medal.entity.UnifiedConfiguration;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnifiedConfigurationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UnifiedConfiguration record);

    int insertSelective(UnifiedConfiguration record);

    UnifiedConfiguration selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UnifiedConfiguration record);

    int updateByPrimaryKey(UnifiedConfiguration record);

    List<UnifiedConfiguration> selectByTypeOrderByLableAsc(String type);

    List<UnifiedConfiguration> selectByTypeOrderByValueAsc(String type);

    UnifiedConfiguration selectByTypeAndLabel(@Param("type") String type, @Param("label") String label);

}