package cn.easydat.system.domain;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;

import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.json.JSONUtil;

/**
 * 通知公告表 sys_notice
 */
public class SysNotice extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 公告ID */
	@Id
	private Long noticeId;

	/** 公告标题 */
	@NotBlank(message = "公告标题不能为空")
	@Length(max = 50, message = "公告标题不能超过50个字符")
	private String noticeTitle;

	/** 公告类型（1通知 2公告） */
	private String noticeType;

	/** 公告内容 */
	private String noticeContent;

	/** 公告状态（0正常 1关闭） */
	private String status;

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
