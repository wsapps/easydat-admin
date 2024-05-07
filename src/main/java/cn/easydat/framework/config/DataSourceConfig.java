package cn.easydat.framework.config;

import javax.sql.DataSource;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.dynamicds.DynamicDataSource;

import cn.easydat.common.constant.DataSourceConstant;

/**
 * 数据源配置
 */
@Configuration
public class DataSourceConfig {

    /**
     * 动态数据源
     * @param ds
     * @return
     */
    @Bean(value = DataSourceConstant.DATA_SOURCE, typed = true)
    public DataSource data_source(@Inject("${ds.data_source}") DynamicDataSource ds) {
        return ds;
    }

}
