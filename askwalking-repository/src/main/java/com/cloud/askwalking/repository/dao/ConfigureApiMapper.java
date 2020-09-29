package com.cloud.askwalking.repository.dao;

import com.cloud.askwalking.repository.model.ConfigureApiDO;
import com.cloud.askwalking.repository.model.ConfigureApiExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigureApiMapper {
    /**
     *  根据指定的条件获取数据库记录数,gw_configure_api
     *
     * @param example
     */
    int countByExample(ConfigureApiExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录,gw_configure_api
     *
     * @param example
     */
    int deleteByExample(ConfigureApiExample example);

    /**
     *  根据主键删除数据库的记录,gw_configure_api
     *
     * @param id
     */
    int deleteByPrimaryKey(String id);

    /**
     *  新写入数据库记录,gw_configure_api
     *
     * @param record
     */
    int insert(ConfigureApiDO record);

    /**
     *  动态字段,写入数据库记录,gw_configure_api
     *
     * @param record
     */
    int insertSelective(ConfigureApiDO record);

    /**
     *  根据指定的条件查询符合条件的数据库记录,gw_configure_api
     *
     * @param example
     */
    List<ConfigureApiDO> selectByExample(ConfigureApiExample example);

    /**
     *  根据指定主键获取一条数据库记录,gw_configure_api
     *
     * @param id
     */
    ConfigureApiDO selectByPrimaryKey(String id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录,gw_configure_api
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") ConfigureApiDO record, @Param("example") ConfigureApiExample example);

    /**
     *  根据指定的条件来更新符合条件的数据库记录,gw_configure_api
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") ConfigureApiDO record, @Param("example") ConfigureApiExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,gw_configure_api
     *
     * @param record
     */
    int updateByPrimaryKeySelective(ConfigureApiDO record);

    /**
     *  根据主键来更新符合条件的数据库记录,gw_configure_api
     *
     * @param record
     */
    int updateByPrimaryKey(ConfigureApiDO record);

    int insertBatchSelective(List<ConfigureApiDO> records);

    int updateBatchByPrimaryKeySelective(List<ConfigureApiDO> records);
}