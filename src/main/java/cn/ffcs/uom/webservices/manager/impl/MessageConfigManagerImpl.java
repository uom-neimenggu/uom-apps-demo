package cn.ffcs.uom.webservices.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.dao.MessageConfigDao;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemBusiUser;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Service("messageConfigManager")
@Scope("prototype")
public class MessageConfigManagerImpl implements
MessageConfigManager {

	@Resource
	private MessageConfigDao messageConfigDao;

	@Override
	public SystemConfigUser queryMessageConfig(
			SystemConfigUser systemConfigUser) {
		if (!StrUtil.isEmpty(systemConfigUser.getTelephoneNumber())) {
			List<SystemConfigUser> list = this.queryMessageConfigList(systemConfigUser);
			if (list != null && list.size() > 0) {
				for (SystemConfigUser newSystemMessageConfig : list) {
					if (StrUtil.isNullOrEmpty(systemConfigUser.getSystemConfigUserId())) {
						return newSystemMessageConfig;
					} else if (!systemConfigUser.getSystemConfigUserId()
							.equals(newSystemMessageConfig
									.getSystemConfigUserId()))
						return newSystemMessageConfig;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemConfigUser> queryMessageConfigList(
			SystemConfigUser querySystemMessageConfig) {
		StringBuffer hql = new StringBuffer(
				"From SystemConfigUser where statusCd = ?");
		List<String> params = new ArrayList<String>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (querySystemMessageConfig != null) {
			if (!StrUtil.isNullOrEmpty(querySystemMessageConfig
					.getUserName())) {
				hql.append(" and userName like ?");
				params.add("%" + querySystemMessageConfig.getUserName()
						+ "%");
			}

			if (!StrUtil.isNullOrEmpty(querySystemMessageConfig
					.getTelephoneNumber())) {
				hql.append(" and telephoneNumber = ?");
				params.add(querySystemMessageConfig.getTelephoneNumber());
			}

			if (!StrUtil.isEmpty(querySystemMessageConfig
					.getOverallSituation())) {
				hql.append(" and overallSituation = ?");
				params.add(querySystemMessageConfig.getOverallSituation());
			}
		}

		hql.append(" order by systemConfigUserId");

		return SystemMessageConfig.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

	/**
	 * 分页查询用户信息
	 * systemConfigUser
	 * @param systemConfigUser
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryMessageConfigPageInfo(
			BusinessSystem businessSystem, int currentPage,
			int pageSize) {
		
		String sql = "select u.* from SYSTEM_CONFIG_USER u,SYSTEM_BUSI_USER s,BUSINESS_SYSTEM m where u.system_config_user_id = s.system_config_user_id and s.business_system_id = m.business_system_id and u.status_cd = ? and s.status_cd = ? and m.status_cd = ? and m.system_code = ?";
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(businessSystem.getSystemCode());

		return this.messageConfigDao.jdbcFindPageInfo(sql,
				paramsList, currentPage, pageSize, SystemConfigUser.class);
	}
	
	
	@Override
	public PageInfo queryMessageConfigPageInfo(
			SystemConfigUser systemConfigUser, int currentPage,
			int pageSize) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYSTEM_CONFIG_USER WHERE STATUS_CD = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (systemConfigUser != null) {
			if (!StrUtil.isNullOrEmpty(systemConfigUser.getTelephoneNumber())) {
				sb.append(" AND TELEPHONE_NUMBER = ?");
				params.add(systemConfigUser.getTelephoneNumber());
			}

			if (!StrUtil.isNullOrEmpty(systemConfigUser.getUserName())) {
				sb.append(" AND USER_NAME LIKE ?");
				params.add("%" + systemConfigUser.getUserName() + "%");
			}
		}

		sb.append(" ORDER BY SYSTEM_CONFIG_USER_ID");

		return this.messageConfigDao.jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, SystemConfigUser.class);
	}

	/**
	 * 短信用户信息新增功能
	 * 
	 * @param systemConfigUser
	 */
	@Override
	public void addMessageConfig(SystemConfigUser systemConfigUser) {
		if (systemConfigUser != null) {
			systemConfigUser.addOnly();

			if("1".equals(systemConfigUser.getOverallSituation())) {
				List<BusinessSystem> list = (List<BusinessSystem>) this.messageConfigDao.getJdbcTemplate().query("select * from BUSINESS_SYSTEM where status_cd = 1000", new Object[]{}, new BeanPropertyRowMapper<BusinessSystem>(BusinessSystem.class));

				for(BusinessSystem businessSystem : list) {
					SystemBusiUser sysBusiUser = new SystemBusiUser();
					sysBusiUser.setBusinessSystemId(businessSystem.getBusinessSystemId());
					sysBusiUser.setSystemConfigUserId(systemConfigUser.getId());
					sysBusiUser.setSort(1L);
					sysBusiUser.addOnly();
				}
			}
		}
	}

	/**
	 * 短信用户信息修改功能
	 * 
	 * @param systemConfigUser
	 */
	@Override
	public void updateMessageConfig(
			SystemConfigUser systemConfigUser) {
		
		long systemConfigUserId = systemConfigUser.getId();
		String sql = "";
		
		if (systemConfigUser != null) {
			systemConfigUser.updateOnly();
			
			// 修改全局管理标识时，更新系统关系数据
			if("1".equals(systemConfigUser.getOverallSituation())) {
				sql = "select * from SYSTEM_BUSI_USER where status_cd = 1000 and system_config_user_id = " + systemConfigUserId;
				List<SystemBusiUser> sysBusiUserList = (List<SystemBusiUser>) this.messageConfigDao.getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<SystemBusiUser>(SystemBusiUser.class));
				for(SystemBusiUser busiUser : sysBusiUserList) {
					busiUser.removeOnly();
				}

				List<BusinessSystem> list = (List<BusinessSystem>) this.messageConfigDao.getJdbcTemplate().query("select * from BUSINESS_SYSTEM where status_cd = 1000", new Object[]{}, new BeanPropertyRowMapper<BusinessSystem>(BusinessSystem.class));

				for(BusinessSystem businessSystem : list) {
					SystemBusiUser sysBusiUser = new SystemBusiUser();
					sysBusiUser.setBusinessSystemId(businessSystem.getBusinessSystemId());
					sysBusiUser.setSystemConfigUserId(systemConfigUserId);
					sysBusiUser.setSort(1L);
					sysBusiUser.addOnly();
				}
			} else {
				sql = "select * from SYSTEM_BUSI_USER where status_cd = 1000 and system_config_user_id = " + systemConfigUserId;
				List<SystemBusiUser> sysBusiUserList = (List<SystemBusiUser>) this.messageConfigDao.getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<SystemBusiUser>(SystemBusiUser.class));
				for(SystemBusiUser busiUser : sysBusiUserList) {
					busiUser.removeOnly();
				}
			}
		}
	}

	/**
	 * 用户信息配置删除功能
	 * 
	 * @param systemConfigUser
	 */
	@Override
	public void removeMessageConfig(
			SystemConfigUser systemConfigUser) {
		if (systemConfigUser != null
				&& systemConfigUser.getSystemConfigUserId() != null) {
			systemConfigUser.removeOnly();

			long systemConfigUserId = systemConfigUser.getId();
			String sql = "select * from SYSTEM_BUSI_USER where status_cd = 1000 and system_config_user_id = " + systemConfigUserId;
			List<SystemBusiUser> sysBusiUserList = (List<SystemBusiUser>) this.messageConfigDao.getJdbcTemplate().query(sql, new Object[]{}, new BeanPropertyRowMapper<SystemBusiUser>(SystemBusiUser.class));
			for(SystemBusiUser busiUser : sysBusiUserList) {
				busiUser.removeOnly();
			}
		}
	}

	/**
	 * 获取电话号码
	 */
	@Override
	public String getTelephoneNumberInfo(
			List<SystemConfigUser> systemMessageConfigList) {
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

	@Override
	public List<SystemConfigUser> querySystemConfigUserListByBusiSys(
			BusinessSystem businessSystem) {
		String sql = "select u.* from SYSTEM_CONFIG_USER u,SYSTEM_BUSI_USER s,BUSINESS_SYSTEM m where u.system_config_user_id = s.system_config_user_id and s.business_system_id = m.business_system_id and u.status_cd = ? and s.status_cd = ? and m.status_cd = ? and m.system_code = ?";
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		paramsList.add(businessSystem.getSystemCode());
		List<SystemConfigUser> systemConfigUserList = (List<SystemConfigUser>) this.messageConfigDao.getJdbcTemplate().query(sql, paramsList.toArray(), new BeanPropertyRowMapper<SystemConfigUser>(SystemConfigUser.class));
		
		return systemConfigUserList;
	}

}
