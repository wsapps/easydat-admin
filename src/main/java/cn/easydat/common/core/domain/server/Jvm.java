package cn.easydat.common.core.domain.server;

import java.lang.management.ManagementFactory;
import java.util.Date;

import cn.easydat.common.utils.Arith;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * JVM相关信息
 */
public class Jvm {
	/**
	 * 当前JVM占用的内存总数(M)
	 */
	private double total;

	/**
	 * JVM最大可用内存总数(M)
	 */
	private double max;

	/**
	 * JVM空闲内存(M)
	 */
	private double free;

	/**
	 * JDK版本
	 */
	private String version;

	/**
	 * JDK路径
	 */
	private String home;

	public double getTotal() {
		return Arith.div(total, (1024 * 1024), 2);
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getMax() {
		return Arith.div(max, (1024 * 1024), 2);
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getFree() {
		return Arith.div(free, (1024 * 1024), 2);
	}

	public void setFree(double free) {
		this.free = free;
	}

	public double getUsed() {
		return Arith.div(total - free, (1024 * 1024), 2);
	}

	public double getUsage() {
		return Arith.mul(Arith.div(total - free, total, 4), 100);
	}

	/**
	 * 获取JDK名称
	 */
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getVmName();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	/**
	 * JDK启动时间
	 */
	public String getStartTime() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return DateUtil.format(new Date(time), DatePattern.NORM_DATETIME_FORMAT);
	}

	/**
	 * JDK运行时间
	 */
	public String getRunTime() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return timeDistance(new Date(), new Date(time));
	}

	/**
	 * 运行参数
	 */
	public String getInputArgs() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
	}

	/**
	 * 计算时间差
	 *
	 * @param endDate   最后时间
	 * @param startTime 开始时间
	 * @return 时间差（天/小时/分钟）
	 */
	private String timeDistance(Date endDate, Date startTime) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startTime.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}
}
