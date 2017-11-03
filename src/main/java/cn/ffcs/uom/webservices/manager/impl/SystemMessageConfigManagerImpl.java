package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.dao.SystemMessageConfigDao;
import cn.ffcs.uom.webservices.manager.SystemMessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Service("systemMessageConfigManager")
@Scope("prototype")
public class SystemMessageConfigManagerImpl implements
		SystemMessageConfigManager {

	@Resource
	private SystemMessageConfigDao systemMessageConfigDao;

	@Override
	public SystemMessageConfig querySystemMessageConfig(
			SystemMessageConfig systemMessageConfig) {
		if (!StrUtil.isEmpty(systemMessageConfig.getSystemCode())
				&& !StrUtil.isEmpty(systemMessageConfig.getTelephoneNumber())) {
			List<SystemMessageConfig> list = this
					.querySystemMessageConfigList(systemMessageConfig);
			if (list != null && list.size() > 0) {
				for (SystemMessageConfig newSystemMessageConfig : list) {
					if (StrUtil.isNullOrEmpty(systemMessageConfig
							.getSystemMessageConfigId())) {
						return newSystemMessageConfig;
					} else if (!systemMessageConfig.getSystemMessageConfigId()
							.equals(newSystemMessageConfig
									.getSystemMessageConfigId()))
						return newSystemMessageConfig;
				}
			}
		}
		return null;
	}

	@Override
	public List<SystemMessageConfig> querySystemMessageConfigList(
			SystemMessageConfig querySystemMessageConfig) {
		StringBuffer hql = new StringBuffer(
				"From SystemMessageConfig where statusCd = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (querySystemMessageConfig != null) {
			if (!StrUtil.isEmpty(querySystemMessageConfig.getSystemCode())) {
				hql.append(" and systemCode = ?");
				params.add(querySystemMessageConfig.getSystemCode());
			}

			if (!StrUtil.isNullOrEmpty(querySystemMessageConfig
					.getContactName())) {
				hql.append(" and contactName like ?");
				params.add("%" + querySystemMessageConfig.getContactName()
						+ "%");
			}

			if (!StrUtil.isNullOrEmpty(querySystemMessageConfig
					.getTelephoneNumber())) {
				hql.append(" and telephoneNumber = ?");
				params.add(querySystemMessageConfig.getTelephoneNumber());
			}

			if (!StrUtil.isEmpty(querySystemMessageConfig
					.getSystemMessageSwitch())) {
				hql.append(" and systemMessageSwitch = ?");
				params.add(querySystemMessageConfig.getSystemMessageSwitch());
			}
		}

		hql.append(" order by noticeOrder,systemMessageConfigId");

		return SystemMessageConfig.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

	/**
	 * 分页查询短信信息配置
	 * 
	 * @param systemMessageConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo querySystemMessageConfigPageInfo(
			SystemMessageConfig systemMessageConfig, int currentPage,
			int pageSize) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_MESSAGE_CONFIG WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemMessageConfig != null) {

			String systemCode = systemMessageConfig.getSystemCode();

			if (!StrUtil.isNullOrEmpty(systemCode)) {
				sb.append(" AND SYSTEM_CODE = ?");
				params.add(systemCode);
			}

			if (!StrUtil.isNullOrEmpty(systemMessageConfig.getContactName())) {
				sb.append(" AND CONTACT_NAME LIKE ?");
				params.add("%" + systemMessageConfig.getContactName() + "%");
			}

			if (!StrUtil
					.isNullOrEmpty(systemMessageConfig.getTelephoneNumber())) {
				sb.append(" AND TELEPHONE_NUMBER LIKE ?");
				params.add("%" + systemMessageConfig.getTelephoneNumber() + "%");
			}

			if (!StrUtil.isNullOrEmpty(systemMessageConfig
					.getSystemMessageSwitch())) {
				sb.append(" AND SYSTEM_MESSAGE_SWITCH = ?");
				params.add(systemMessageConfig.getSystemMessageSwitch());
			}

		}

		sb.append(" ORDER BY SYSTEM_MESSAGE_CONFIG_ID");

		return this.systemMessageConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemMessageConfig.class);
	}

	/**
	 * 短信信息配置新增功能
	 * 
	 * @param systemMessageConfig
	 */
	@Override
	public void addSystemMessageConfig(SystemMessageConfig systemMessageConfig) {
		if (systemMessageConfig != null) {
			systemMessageConfig.addOnly();
		}
	}

	/**
	 * 短信信息配置修改功能
	 * 
	 * @param systemMessageConfig
	 */
	@Override
	public void updateSystemMessageConfig(
			SystemMessageConfig systemMessageConfig) {
		if (systemMessageConfig != null) {
			systemMessageConfig.updateOnly();
		}
	}

	/**
	 * 短信信息配置删除功能
	 * 
	 * @param systemMessageConfig
	 */
	@Override
	public void removeSystemMessageConfig(
			SystemMessageConfig systemMessageConfig) {
		if (systemMessageConfig != null
				&& systemMessageConfig.getSystemMessageConfigId() != null) {
			systemMessageConfig.removeOnly();
		}
	}

	/**
	 * 获取电话号码
	 */
	@Override
	public String getTelephoneNumberInfo(
			List<SystemMessageConfig> systemMessageConfigList) {
		String telephoneNumber = "";
		if (systemMessageConfigList != null
				&& systemMessageConfigList.size() > 0) {
			for (int i = 0; i < systemMessageConfigList.size(); i++) {
				if (i == 0) {
					telephoneNumber += systemMessageConfigList.get(i)
							.getTelephoneNumber();
				} else {
					telephoneNumber += ","
							+ systemMessageConfigList.get(i)
									.getTelephoneNumber();
				}
			}
		}

		return telephoneNumber;
	}

}
