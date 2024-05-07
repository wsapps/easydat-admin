package cn.easydat.framework.config;

import org.noear.solon.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mybatisflex.core.audit.AuditManager;

/**
 * Mybatis-Flex配置
 */
@Configuration
public class MybatisFlexConfig {

    private static final Logger log = LoggerFactory.getLogger("mybatis-flex-sql");

    public MybatisFlexConfig() {
        globalConfig();
        sqlLog();
    }

    /**
     * 全局配置
     */
    private void globalConfig() {
        //FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();
        //globalConfig.registerInsertListener(new DomainInsertListener<>(), BaseEntity.class);
        //globalConfig.registerUpdateListener(new DomainUpdateListener<>(), BaseEntity.class);
    }

    /**
     * SQL打印
     */
    private void sqlLog() {
        // 开启审计功能（SQL打印分析的功能是使用 SQL审计模块完成的）
        AuditManager.setAuditEnable(true);

        // 设置 SQL审计收集器
        AuditManager.setMessageCollector(auditMessage ->
                log.info("SQL:【{}】, 耗时:【{}ms】", auditMessage.getFullSql(), auditMessage.getElapsedTime())
        );
    }

}
