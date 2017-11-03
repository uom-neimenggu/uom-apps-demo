package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.dao.SystemMonitorConfigDao;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;

@Service("systemMonitorConfigManager")
@Scope("prototype")
public class SystemMonitorConfigManagerImpl implements SystemMonitorConfigManager {

	@Resource
	private SystemMonitorConfigDao systemMonitorConfigDao;

	@Override
	public SystemMonitorConfig querySystemMonitorConfig(SystemMonitorConfig systemMonitorConfig) {
		if (!StrUtil.isEmpty(systemMonitorConfig.getSystemCode())) {
			List<SystemMonitorConfig> list = this.querySystemMonitorConfigList(systemMonitorConfig);
			if (list != null && list.size() > 0) {
				for (SystemMonitorConfig newSystemMonitorConfig : list) {
					if (StrUtil.isNullOrEmpty(systemMonitorConfig.getSystemMonitorConfigId())) {
						return newSystemMonitorConfig;
					} 
					else if (!systemMonitorConfig.getSystemMonitorConfigId().equals(newSystemMonitorConfig.getSystemMonitorConfigId()))
						return newSystemMonitorConfig;
				}
			}
		}
		return null;
	}

	@Override
	public List<SystemMonitorConfig> querySystemMonitorConfigList(SystemMonitorConfig querySystemMonitorConfig) {
		StringBuffer hql = new StringBuffer("From SystemMonitorConfig where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (querySystemMonitorConfig != null) {
			if (!StrUtil.isEmpty(querySystemMonitorConfig.getSystemCode())) {
				hql.append(" and systemCode = ?");
				params.add(querySystemMonitorConfig.getSystemCode());
			}

			if (!StrUtil.isNullOrEmpty(querySystemMonitorConfig.getEventName())) {
				hql.append(" and eventName like ?");
				params.add("%" + querySystemMonitorConfig.getEventName() + "%");
			}

			if (!StrUtil.isEmpty(querySystemMonitorConfig.getSystemMonitorSwitch())) {
				hql.append(" and systemMonitorSwitch = ?");
				params.add(querySystemMonitorConfig.getSystemMonitorSwitch());
			}
		}

		hql.append(" order by systemMonitorConfigId desc");

		return SystemMonitorConfig.repository().findListByHQLAndParams(hql.toString(), params);
	}

	/**
	 * 分页查询短信信息配置
	 * 
	 * @param systemMonitorConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo querySystemMonitorConfigPageInfo(SystemMonitorConfig systemMonitorConfig, int currentPage,int pageSize) {

		StringBuffer sb = new StringBuffer("SELECT * FROM SYSTEM_MONITOR_CONFIG WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemMonitorConfig != null) {

			String systemCode = systemMonitorConfig.getSystemCode();

			if (!StrUtil.isNullOrEmpty(systemCode)) {
				sb.append(" AND SYSTEM_CODE = ?");
				params.add(systemCode);
			}

			if (!StrUtil.isNullOrEmpty(systemMonitorConfig.getEventName())) {
				sb.append(" AND EVENT_NAME LIKE ?");
				params.add("%" + systemMonitorConfig.getEventName() + "%");
			}


			if (!StrUtil.isNullOrEmpty(systemMonitorConfig
					.getSystemMonitorSwitch())) {
				sb.append(" AND SYSTEM_MONITOR_SWITCH = ?");
				params.add(systemMonitorConfig.getSystemMonitorSwitch());
			}

		}

		sb.append(" ORDER BY SYSTEM_MONITOR_CONFIG_ID");

		return this.systemMonitorConfigDao.jdbcFindPageInfo(sb.toString(),params, currentPage, pageSize, SystemMonitorConfig.class);
	}

	/**
	 * 短信信息配置新增功能
	 * 
	 * @param systemMonitorConfig
	 */
	@Override
	public void addSystemMonitorConfig(SystemMonitorConfig systemMonitorConfig) {
		if (systemMonitorConfig != null) {
			systemMonitorConfig.addOnly();
		}
	}

	/**
	 * 短信信息配置修改功能
	 * 
	 * @param systemMonitorConfig
	 */
	@Override
	public void updateSystemMonitorConfig(
			SystemMonitorConfig systemMonitorConfig) {
		if (systemMonitorConfig != null) {
			systemMonitorConfig.updateOnly();
		}
	}

	/**
	 * 短信信息配置删除功能
	 * 
	 * @param systemMonitorConfig
	 */
	@Override
	public void removeSystemMonitorConfig(
			SystemMonitorConfig systemMonitorConfig) {
		if (systemMonitorConfig != null
				&& systemMonitorConfig.getSystemMonitorConfigId() != null) {
			systemMonitorConfig.removeOnly();
		}
	}
	
	@Override
	public boolean checkDataIsExist(String systemCode,String eventName){
		StringBuffer hql = new StringBuffer("From SystemMonitorConfig where systemCode = ? and eventName = ?");
		List params = new ArrayList();
		params.add(systemCode);
		params.add(eventName);
		
		List<SystemMonitorConfig> list = SystemMonitorConfig.repository().findListByHQLAndParams(hql.toString(), params);
		if(list.size()>0){
			return true;
		}
		return false;
	}
}
