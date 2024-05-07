package cn.easydat.common.constant;

public class UserConstant {

	/**
	 * 超级管理员
	 */
	// 用户 ID
	public static final long ADMIN_USER_ID = 1L;
	// 角色 ID
	public static final long ADMIN_ROLE_ID = 1L;
	
	/** 校验是否唯一的返回标识 */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;
    
    /** 是否为系统默认（是） */
    public static final String YES = "Y";
    
    /** 部门正常状态 */
    public static final String DEPT_NORMAL = "0";

    /** 部门停用状态 */
    public static final String DEPT_DISABLE = "1";
	
}
