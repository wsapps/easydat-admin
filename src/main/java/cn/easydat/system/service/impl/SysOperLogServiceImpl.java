package cn.easydat.system.service.impl;

import java.util.List;

import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import cn.easydat.system.domain.SysOperLog;
import cn.easydat.system.mapper.SysOperLogMapper;
import cn.easydat.system.service.SysOperLogService;

/**
 * 操作日志 服务层处理
 */
@Component
public class SysOperLogServiceImpl implements SysOperLogService {
	@Db
	private SysOperLogMapper operLogMapper;

	/**
	 * 新增操作日志
	 * 
	 * @param operLog 操作日志对象
	 */
	@Override
	public void insertOperlog(SysOperLog operLog) {
		operLogMapper.insertOperlog(operLog);
	}

	/**
	 * 查询系统操作日志集合
	 * 
	 * @param operLog 操作日志对象
	 * @return 操作日志集合
	 */
	@Override
	public List<SysOperLog> selectOperLogList(SysOperLog operLog) {
		return operLogMapper.selectOperLogList(operLog);
	}

	/**
	 * 批量删除系统操作日志
	 * 
	 * @param operIds 需要删除的操作日志ID
	 * @return 结果
	 */
	@Override
	public int deleteOperLogByIds(Long[] operIds) {
		return operLogMapper.deleteOperLogByIds(operIds);
	}

	/**
	 * 查询操作日志详细
	 * 
	 * @param operId 操作ID
	 * @return 操作日志对象
	 */
	@Override
	public SysOperLog selectOperLogById(Long operId) {
		return operLogMapper.selectOperLogById(operId);
	}

	/**
	 * 清空操作日志
	 */
	@Override
	public void cleanOperLog() {
		operLogMapper.cleanOperLog();
	}
}
