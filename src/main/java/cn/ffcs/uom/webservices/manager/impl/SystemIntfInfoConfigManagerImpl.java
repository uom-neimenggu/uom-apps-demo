package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.dao.SystemIntfInfoConfigDao;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

@Service("systemIntfInfoConfigManager")
@Scope("prototype")
public class SystemIntfInfoConfigManagerImpl implements
		SystemIntfInfoConfigManager {

	@Resource
	private SystemIntfInfoConfigDao systemIntfInfoConfigDao;

	@Override
	public SystemIntfInfoConfig querySystemIntfInfoConfigBySystemCode(
			String systemCode) {
		if (!StrUtil.isEmpty(systemCode)) {
			SystemIntfInfoConfig querySystemIntfInfoConfig = new SystemIntfInfoConfig();
			querySystemIntfInfoConfig.setSystemCode(systemCode);
			List<SystemIntfInfoConfig> list = this
					.querySystemIntfInfoConfigList(querySystemIntfInfoConfig);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		}
		return null;
	}

	@Override
	public List<SystemIntfInfoConfig> querySystemIntfInfoConfigList(
			SystemIntfInfoConfig querySystemIntfInfoConfig) {
		StringBuffer hql = new StringBuffer(
				"From SystemIntfInfoConfig where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (querySystemIntfInfoConfig != null) {
			if (!StrUtil.isEmpty(querySystemIntfInfoConfig.getSystemCode())) {
				hql.append(" and systemCode = ?");
				params.add(querySystemIntfInfoConfig.getSystemCode());
			}
		}
		return SystemIntfInfoConfig.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

	/**
	 * 分页查询业务系统配置
	 * 
	 * @param systemIntfInfoConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo querySystemIntfInfoConfigPageInfo(
			SystemIntfInfoConfig systemIntfInfoConfig, int currentPage,
			int pageSize) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_INTF_INFO_CONFIG WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemIntfInfoConfig != null) {

			String systemCode = systemIntfInfoConfig.getSystemCode();
			if (!StrUtil.isNullOrEmpty(systemCode)) {
				sb.append(" AND SYSTEM_CODE = ?");
				params.add(systemCode);
			}

		}

		sb.append(" ORDER BY SYSTEM_INTF_INFO_CONFIG_ID");

		return this.systemIntfInfoConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemIntfInfoConfig.class);
	}

	/**
	 * 业务系统信息新增功能
	 * 
	 * @param systemIntfInfoConfig
	 */
	@Override
	public void addSystemIntfInfoConfig(
			SystemIntfInfoConfig systemIntfInfoConfig) {
		if (systemIntfInfoConfig != null) {
			systemIntfInfoConfig.addOnly();
		}
	}

	/**
	 * 业务系统信息修改功能
	 * 
	 * @param systemIntfInfoConfig
	 */
	@Override
	public void updateSystemIntfInfoConfig(
			SystemIntfInfoConfig systemIntfInfoConfig) {
		if (systemIntfInfoConfig != null) {
			systemIntfInfoConfig.updateOnly();
		}
	}

	/**
	 * 业务系统信息删除功能
	 * 
	 * @param systemIntfInfoConfig
	 */
	@Override
	public void removeSystemIntfInfoConfig(
			SystemIntfInfoConfig systemIntfInfoConfig) {
		if (systemIntfInfoConfig != null
				&& systemIntfInfoConfig.getSystemIntfInfoConfigId() != null) {
			systemIntfInfoConfig.removeOnly();
		}
	}
	
	/**
	 * 修改AttrValue 中接口状态值
	*@param objs 
	*@author wongs 
	*@date 2014-8-25 下午3:05:11 
	*@comment
	 */
	public void interfaceStatus(Object[] objs){
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE SYSTEM_INTF_INFO_CONFIG T SET T.INTF_SWITCH_INCREASE=?").
		append(" WHERE T.STATUS_CD=? AND T.SYSTEM_CODE").
		append(" NOT IN(13100,13106,13107)");
		
		systemIntfInfoConfigDao.getJdbcTemplate().update(sb.toString(), new Object[]{objs[0],objs[1]});
		
	}

}
