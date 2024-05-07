package cn.easydat.system.domain;

import java.util.Date;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 操作日志记录表 oper_log
 */
public class SysOperLog extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 日志主键 */
	@Alias("操作序号")
	@Id
	private Long operId;

	/** 操作模块 */
	@Alias("操作模块")
	private String title;

	/** 业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据） */
	@Alias("业务类型")
	private Integer businessType;

	/** 业务类型数组 */
	@Column(ignore = true)
	private Integer[] businessTypes;

	/** 请求方法 */
	@Alias("请求方法")
	private String method;

	/** 请求方式 */
	@Alias("请求方式")
	private String requestMethod;

	/** 操作类别（0其它 1后台用户 2手机端用户） */
	@Alias("操作类别")
	private Integer operatorType;

	/** 操作人员 */
	@Alias("操作人员")
	private String operName;

	/** 部门名称 */
	@Alias("部门名称")
	private String deptName;

	/** 请求url */
	@Alias("请求地址")
	private String operUrl;

	/** 操作地址 */
	@Alias("操作地址")
	private String operIp;
	
	/** 操作地点 */
	@Alias("操作地点")
    private String operLocation;

	/** 请求参数 */
	@Alias("请求参数")
	private String operParam;

	/** 返回参数 */
	@Alias("返回参数")
	private String jsonResult;

	/** 操作状态（0正常 1异常） */
	@Alias("操作状态（0成功 1失败）")
	private Integer status;

	/** 错误消息 */
	@Alias("错误消息")
	private String errorMsg;

	/** 操作时间 */
	@Alias("操作时间")
	private Date operTime;

	/** 消耗时间 */
	@Alias("消耗时间")
	private Long costTime;

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
		this.operId = operId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Integer[] getBusinessTypes() {
		return businessTypes;
	}

	public void setBusinessTypes(Integer[] businessTypes) {
		this.businessTypes = businessTypes;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Integer getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getOperUrl() {
		return operUrl;
	}

	public void setOperUrl(String operUrl) {
		this.operUrl = operUrl;
	}

	public String getOperIp() {
		return operIp;
	}

	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}

	public String getOperLocation() {
		return operLocation;
	}

	public void setOperLocation(String operLocation) {
		this.operLocation = operLocation;
	}

	public String getOperParam() {
		return operParam;
	}

	public void setOperParam(String operParam) {
		this.operParam = operParam;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
