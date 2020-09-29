package com.cloud.askwalking.admin.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author niuzhiwei
 */
@Configuration
@MapperScan(basePackages = {"com.cloud.askwalking.repository.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {


    @Order
    @Bean(name = "dateSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDateSourceFirst() {
        DruidDataSource druidDataSource = DataSourceBuilder.create().type(DruidDataSource.class).build();
        druidDataSource.setRemoveAbandoned(Boolean.TRUE);
        druidDataSource.setRemoveAbandonedTimeout(180);
        druidDataSource.setLogAbandoned(Boolean.TRUE);
        return druidDataSource;
    }

    @Order
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dateSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
        return bean.getObject();
    }

    @Order
    @Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessiontemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionTemplate) {
        return new SqlSessionTemplate(sqlSessionTemplate);
    }
}
