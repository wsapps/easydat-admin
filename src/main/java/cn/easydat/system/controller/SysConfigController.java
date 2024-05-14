package cn.easydat.system.controller;

import java.util.List;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.annotation.Post;
import org.noear.solon.annotation.Put;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.validation.annotation.Validated;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.system.domain.SysConfig;
import cn.easydat.system.service.SysConfigService;

/**
 * 参数配置 信息操作处理
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/system/config")
public class SysConfigController extends AjaxResultBaseController {
	@Inject
	private SysConfigService configService;

	/**
	 * 获取参数配置列表
	 */
	@AuthPermissions("system:config:list")
	@Get
	@Mapping("/list")
	public TableDataInfo<SysConfig> list(SysConfig config) {
		startPage();
		List<SysConfig> list = configService.selectConfigList(config);
		return getDataTable(list);
	}

	@Log(title = "参数管理", businessType = BusinessType.EXPORT)
	@AuthPermissions("system:config:export")
	@Post
	@Mapping("/export")
	public void export(SysConfig config) {
		List<SysConfig> list = configService.selectConfigList(config);
		export(list);
	}

	/**
	 * 根据参数编号获取详细信息
	 */
	@AuthPermissions("system:config:query")
	@Get
	@Mapping("/{configId}")
	public AjaxResult getInfo(@Path Long configId) {
		return success(configService.selectConfigById(configId));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@Get
	@Mapping("/configKey/{configKey}")
	public AjaxResult getConfigKey(@Path String configKey) {
		return success(configService.selectConfigByKey(configKey));
	}

	/**
	 * 新增参数配置
	 */
	@AuthPermissions("system:config:add")
	@Log(title = "参数管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Body @Validated SysConfig config) {
		if (!configService.checkConfigKeyUnique(config)) {
			return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
		}
		config.setCreateBy(getUsername());
		return toAjax(configService.insertConfig(config));
	}

	/**
	 * 修改参数配置
	 */
	@AuthPermissions("system:config:edit")
	@Log(title = "参数管理", businessType = BusinessType.UPDATE)
	@Put
    @Mapping
	public AjaxResult edit(@Body @Validated SysConfig config) {
		if (!configService.checkConfigKeyUnique(config)) {
			return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
		}
		config.setUpdateBy(getUsername());
		return toAjax(configService.updateConfig(config));
	}

	/**
	 * 删除参数配置
	 */
	@AuthPermissions("system:config:remove")
	@Log(title = "参数管理", businessType = BusinessType.DELETE)
	@Delete
    @Mapping("/{configIds}")
	public AjaxResult remove(@Path Long[] configIds) {
		configService.deleteConfigByIds(configIds);
		return success();
	}

	/**
	 * 刷新参数缓存
	 */
	@AuthPermissions("system:config:remove")
	@Log(title = "参数管理", businessType = BusinessType.CLEAN)
	@Delete
    @Mapping("/refreshCache")
	public AjaxResult refreshCache() {
		configService.resetConfigCache();
		return success();
	}
}
