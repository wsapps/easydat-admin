package cn.easydat.common.exception;

import org.noear.solon.core.handle.Result;
import org.noear.solon.core.util.DataThrowable;

import cn.hutool.json.JSONUtil;

/**
 * 接口状态码
 */
public class EasydatException extends DataThrowable {

	private static final long serialVersionUID = 1L;

	private int code = 0;

	private String description = "";

	public EasydatException(int code) {
		super(code + "");
		this.code = code;
	}

	public EasydatException(int code, String description) {
		super(description);
		this.code = code;
		this.description = description;
	}

	public EasydatException(Throwable throwable) {
		super(throwable.getMessage(), throwable);
		this.code = Result.FAILURE_CODE;
		this.description = throwable.getMessage();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
