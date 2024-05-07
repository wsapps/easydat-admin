package cn.easydat.framework.config;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.auth.AuthAdapter;
import org.noear.solon.auth.AuthRule;
import org.noear.solon.auth.impl.AuthRuleImpl;
import org.noear.solon.core.handle.Context;

import cn.easydat.framework.security.AuthProcessorImpl;

/**
 * 安全配置
 */
@Configuration
public class SecurityConfig {

    /**
     * 注入配置
     * @param securityProperties
     * @return
     */
    @Bean
    public SecurityProperties securityProperties(@Inject("${easydat.security}") SecurityProperties securityProperties) {
        return securityProperties;
    }

    /**
     * 鉴权处理
     * @return
     */
    @Bean
    @Condition(onClass = SecurityProperties.class)
    public AuthAdapter authAdapter(SecurityProperties securityProperties) {
        AuthProcessorImpl authProcessor = new AuthProcessorImpl();
        authProcessor.setIpBlackList(securityProperties.getIps());
        return new AuthAdapter()
                // 添加规则
                .addRules(getRules(securityProperties))
                // 设定鉴权处理器
                .processor(authProcessor)
                // 设定默认的验证失败处理
                .failure(Context::render);
    }

    /**
     * 定义规则
     * @return
     */
    private List<AuthRule> getRules(SecurityProperties securityProperties) {
        List<AuthRule> list = new ArrayList<>();

        // 所有请求，校验 IP
        AuthRuleImpl ipRule = new AuthRuleImpl();
        AuthRule verifyIp = ipRule.include("/**").verifyIp();
        list.add(verifyIp);

        // 所有请求，排除白名单，校验权限字符（校验登录）
        AuthRuleImpl loginRule = new AuthRuleImpl();
        loginRule.include("/**");
        for (String path : securityProperties.getWhites()) {
            loginRule.exclude(path);
        }
        AuthRule verifyPermissions = loginRule.verifyPermissions();
        list.add(verifyPermissions);

        return list;
    }

}
