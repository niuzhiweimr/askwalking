package com.cloud.askwalking.repository.dao;

import com.cloud.askwalking.repository.model.SaasResourceDO;
import com.cloud.askwalking.repository.model.SaasResourceExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author niuzhiwei
 */
@Repository
public interface SaasResourceMapper {
    /**
     * 根据指定的条件获取数据库记录数,gw_saas_resource
     *
     * @param example
     */
    int countByExample(SaasResourceExample example);

    /**
     * 根据指定的条件删除数据库符合条件的记录,gw_saas_resource
     *
     * @param example
     */
    int deleteByExample(SaasResourceExample example);

    /**
     * 根据主键删除数据库的记录,gw_saas_resource
     *
     * @param id
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新写入数据库记录,gw_saas_resource
     *
     * @param record
     */
    int insert(SaasResourceDO record);

    /**
     * 动态字段,写入数据库记录,gw_saas_resource
     *
     * @param record
     */
    int insertSelective(SaasResourceDO record);

    /**
     * 根据指定的条件查询符合条件的数据库记录,gw_saas_resource
     *
     * @param example
     */
    List<SaasResourceDO> selectByExample(SaasResourceExample example);

    /**
     * 根据指定主键获取一条数据库记录,gw_saas_resource
     *
     * @param id
     */
    SaasResourceDO selectByPrimaryKey(String id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录,gw_saas_resource
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") SaasResourceDO record, @Param("example") SaasResourceExample example);

    /**
     * 根据指定的条件来更新符合条件的数据库记录,gw_saas_resource
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") SaasResourceDO record, @Param("example") SaasResourceExample example);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,gw_saas_resource
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SaasResourceDO record);

    /**
     * 根据主键来更新符合条件的数据库记录,gw_saas_resource
     *
     * @param record
     */
    int updateByPrimaryKey(SaasResourceDO record);

    int insertBatchSelective(List<SaasResourceDO> records);

    int updateBatchByPrimaryKeySelective(List<SaasResourceDO> records);
}