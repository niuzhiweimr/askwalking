package com.cloud.askwalking.gateway.manager.dao;

import com.cloud.askwalking.gateway.manager.model.UserDO;
import com.cloud.askwalking.gateway.manager.model.UserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 根据指定的条件获取数据库记录数,gw_user
     *
     * @param example
     */
    int countByExample(UserExample example);

    /**
     * 根据指定的条件删除数据库符合条件的记录,gw_user
     *
     * @param example
     */
    int deleteByExample(UserExample example);

    /**
     * 根据主键删除数据库的记录,gw_user
     *
     * @param id
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新写入数据库记录,gw_user
     *
     * @param record
     */
    int insert(UserDO record);

    /**
     * 动态字段,写入数据库记录,gw_user
     *
     * @param record
     */
    int insertSelective(UserDO record);

    /**
     * 根据指定的条件查询符合条件的数据库记录,gw_user
     *
     * @param example
     */
    List<UserDO> selectByExample(UserExample example);

    /**
     * 根据指定主键获取一条数据库记录,gw_user
     *
     * @param id
     */
    UserDO selectByPrimaryKey(Integer id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录,gw_user
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") UserDO record, @Param("example") UserExample example);

    /**
     * 根据指定的条件来更新符合条件的数据库记录,gw_user
     *
     * @param record
     * @param example
     */
    int updateByExample(@Param("record") UserDO record, @Param("example") UserExample example);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,gw_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * 根据主键来更新符合条件的数据库记录,gw_user
     *
     * @param record
     */
    int updateByPrimaryKey(UserDO record);

    int insertBatchSelective(List<UserDO> records);

    int updateBatchByPrimaryKeySelective(List<UserDO> records);
}