package cn.easydat.generator.config;

import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

@Configuration
@Inject(value = "${easydat.gen}")
public class GenConfig {

	/** 作者 */
	public String author;

	/** 生成包路径 */
	public String packageName;

	/** 自动去除表前缀，默认是false */
	public boolean autoRemovePre;

	/** 表前缀(类名不会包含表前缀) */
	public String tablePrefix;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isAutoRemovePre() {
		return autoRemovePre;
	}

	public void setAutoRemovePre(boolean autoRemovePre) {
		this.autoRemovePre = autoRemovePre;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

}
