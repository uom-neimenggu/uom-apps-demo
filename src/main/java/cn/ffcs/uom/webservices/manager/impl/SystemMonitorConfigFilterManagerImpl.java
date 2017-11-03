package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.dao.SystemMonitorConfigFilterDao;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigFilterManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfigFilter;

@Service("systemMonitorConfigFilterManager")
@Scope("prototype")
public class SystemMonitorConfigFilterManagerImpl implements SystemMonitorConfigFilterManager {

	@Resource
	private SystemMonitorConfigFilterDao systemMonitorConfigFilterDao;

	@Override
	public SystemMonitorConfigFilter querySystemMonitorConfigFilter(SystemMonitorConfigFilter systemMonitorConfigFilter) {
		if (systemMonitorConfigFilter.getSystemMonitorFilterId()!=null) {
			List<SystemMonitorConfigFilter> list = this.querySystemMonitorConfigFilterList(systemMonitorConfigFilter);
			if (list != null && list.size() > 0) {
				for (SystemMonitorConfigFilter newSystemMonitorConfigFilter : list) {
					if (StrUtil.isNullOrEmpty(systemMonitorConfigFilter.getSystemMonitorFilterId())) {
						return newSystemMonitorConfigFilter;
					} 
					else if (!systemMonitorConfigFilter.getSystemMonitorFilterId().equals(newSystemMonitorConfigFilter.getSystemMonitorFilterId()))
						return newSystemMonitorConfigFilter;
				}
			}
		}
		return null;
	}

	@Override
	public List<SystemMonitorConfigFilter> querySystemMonitorConfigFilterList(SystemMonitorConfigFilter querySystemMonitorConfigFilter) {
		StringBuffer hql = new StringBuffer("From SystemMonitorConfigFilter where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (querySystemMonitorConfigFilter != null) {
			if (querySystemMonitorConfigFilter.getSystemMonitorFilterId()!=null) {
				hql.append(" and systemMonitorConfigFilterId  = ?");
				params.add(querySystemMonitorConfigFilter.getSystemMonitorFilterId());
			}

			if (!StrUtil.isNullOrEmpty(querySystemMonitorConfigFilter.getFilterName())) {
				hql.append(" and filterName like ?");
				params.add("%" + querySystemMonitorConfigFilter.getFilterName() + "%");
			}

			if (!StrUtil.isEmpty(querySystemMonitorConfigFilter.getSystemMonitorFilterSwitch())) {
				hql.append(" and systemMonitorFilterSwitch = ?");
				params.add(querySystemMonitorConfigFilter.getSystemMonitorFilterSwitch());
			}
		}

		hql.append(" order by systemMonitorConfigFilterId desc");

		return SystemMonitorConfigFilter.repository().findListByHQLAndParams(hql.toString(), params);
	}

	/**
	 * 分页查询短信信息配置
	 * 
	 * @param systemMonitorConfigFilter
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo querySystemMonitorConfigFilterPageInfo(SystemMonitorConfigFilter systemMonitorConfigFilter, int currentPage,int pageSize) {

		StringBuffer sb = new StringBuffer("SELECT * FROM SYSTEM_MONITOR_CONFIG_FILTER WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemMonitorConfigFilter != null) {

			String systemMonitorConfigId = systemMonitorConfigFilter.getSystemMonitorConfigId().toString();

			if (!StrUtil.isNullOrEmpty(systemMonitorConfigId)) {
				sb.append(" AND SYSTEM_MONITOR_CONFIG_ID = ?");
				params.add(systemMonitorConfigId);
			}

			if (!StrUtil.isNullOrEmpty(systemMonitorConfigFilter.getFilterName())) {
				sb.append(" AND FILTER_NAME LIKE ?");
				params.add("%" + systemMonitorConfigFilter.getFilterName() + "%");
			}


			if (!StrUtil.isNullOrEmpty(systemMonitorConfigFilter
					.getSystemMonitorFilterSwitch())) {
				sb.append(" AND SYSTEM_MONITOR_FILTER_SWITCH = ?");
				params.add(systemMonitorConfigFilter.getSystemMonitorFilterSwitch());
			}

		}

		sb.append(" ORDER BY SYSTEM_MONITOR_FILTER_ID DESC");

		return this.systemMonitorConfigFilterDao.jdbcFindPageInfo(sb.toString(),params, currentPage, pageSize, SystemMonitorConfigFilter.class);
	}

	/**
	 * 短信信息配置新增功能
	 * 
	 * @param systemMonitorConfigFilter
	 */
	@Override
	public void addSystemMonitorConfigFilter(SystemMonitorConfigFilter systemMonitorConfigFilter) {
		if (systemMonitorConfigFilter != null) {
			systemMonitorConfigFilter.addOnly();
		}
	}

	/**
	 * 短信信息配置修改功能
	 * 
	 * @param systemMonitorConfigFilter
	 */
	@Override
	public void updateSystemMonitorConfigFilter(
			SystemMonitorConfigFilter systemMonitorConfigFilter) {
		if (systemMonitorConfigFilter != null) {
			systemMonitorConfigFilter.updateOnly();
		}
	}

	/**
	 * 短信信息配置删除功能
	 * 
	 * @param systemMonitorConfigFilter
	 */
	@Override
	public void removeSystemMonitorConfigFilter(
			SystemMonitorConfigFilter systemMonitorConfigFilter) {
		if (systemMonitorConfigFilter != null
				&& systemMonitorConfigFilter.getSystemMonitorFilterId() != null) {
			systemMonitorConfigFilter.removeOnly();
		}
	}


}
