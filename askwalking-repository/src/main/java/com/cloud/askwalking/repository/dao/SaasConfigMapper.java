package com.cloud.askwalking.repository.dao;

import com.cloud.askwalking.repository.model.SaasConfigDO;
import com.cloud.askwalking.repository.model.SaasConfigExample;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author niuzhiwei
 */
@Repository
public interface SaasConfigMapper {
    /**
     *  根据指定的条件获取数据库记录数,gw_saas_config
     *
     * @param example
     */
    int countByExample(SaasConfigExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录,gw_saas_config
     *
     * @param example
     */
    int deleteByExample(SaasConfigExample example);

    /**
     *  根据主键删除数据库的记录,gw_saas_config
     *
     * @param id
     */
    int deleteByPrimaryKey(String id);

    /**
     *  新写入数据库记录,gw_saas_config
     *
     * @param record
     */
    int insert(SaasConfigDO record);

    /**
     *  动态字段,写入数据库记录,gw_saas_config
     *
     * @param record
     */
    int insertSelective(SaasConfigDO record);

    /**
     *  根据指定的条件查询符合条件的数据库记录,gw_saas_config
     *
     * @param example
     */
    List<SaasConfigDO> selectByExample(SaasConfigExample example);

    /**
     *  根据指定主键获取一条数据库记录,gw_saas_config
     *
     * @param id
     */
    SaasConfigDO selectByPrimaryKey(String id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录,gw_saas_config
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") SaasConfigDO record, @Param("example") SaasConfigExample example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录,gw_saas_config
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") SaasConfigDO record, @Param("example") SaasConfigExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,gw_saas_config
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SaasConfigDO record);

    /**
     *  根据主键来更新符合条件的数据库记录,gw_saas_config
     *
     * @param record
     */
    int updateByPrimaryKey(SaasConfigDO record);

    int insertBatchSelective(List<SaasConfigDO> records);

    int updateBatchByPrimaryKeySelective(List<SaasConfigDO> records);
}