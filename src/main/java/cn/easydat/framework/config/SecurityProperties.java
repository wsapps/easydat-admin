package cn.easydat.framework.config;

/**
 * 安全Properties
 */
public class SecurityProperties {

	/**
	 * IP黑名单
	 */
	private String[] ips = new String[] {};

	/**
	 * 放行白名单
	 */
	private String[] whites = new String[] {};

	public String[] getIps() {
		return ips;
	}

	public void setIps(String[] ips) {
		this.ips = ips;
	}

	public String[] getWhites() {
		return whites;
	}

	public void setWhites(String[] whites) {
		this.whites = whites;
	}

}
