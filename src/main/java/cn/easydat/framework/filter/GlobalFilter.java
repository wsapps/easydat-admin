package cn.easydat.framework.filter;

import org.noear.solon.annotation.Component;
import org.noear.solon.auth.AuthException;
import org.noear.solon.auth.AuthStatus;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.solon.core.handle.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.easydat.common.exception.EasydatException;

/**
 * 全局过滤器（包含限流、全局异常）
 */
@Component
public class GlobalFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GlobalFilter.class);

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            chain.doFilter(ctx);
        } catch (AuthException e) {
            // 鉴权异常
            AuthStatus status = e.getStatus();
            ctx.render(Result.failure(status.code, status.message));
        } catch (Exception ex) {
            // 其它异常
        	EasydatException apiCode = new EasydatException(400, "未知错误");
            ctx.render(Result.failure(apiCode.getCode(), apiCode.getMessage()));
			log.error(null, ex);
        }
        long times = System.currentTimeMillis() - start;
        log.info("请求【{}】完成，耗时:【{}ms】", ctx.path(), times);
    }

}
