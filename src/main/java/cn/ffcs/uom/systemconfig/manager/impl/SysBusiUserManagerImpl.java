
package cn.ffcs.uom.systemconfig.manager.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.dao.SystemBusiUserDao;
import cn.ffcs.uom.systemconfig.manager.SysBusiUserManager;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.webservices.model.SystemBusiUser;

@Service("sysBusiUserManager")
@Scope("prototype")
public class SysBusiUserManagerImpl implements SysBusiUserManager {
	@Resource(name = "systemBusiUserDao")
	private SystemBusiUserDao systemBusiUserDao;

	@Override
	public void addSysBusiUser(SystemBusiUser sysBusiUser) {
		if (sysBusiUser != null) {
			sysBusiUser.addOnly();
		}
	}

	@Override
	public PageInfo queryBusinessSystemPageInfo(
			SystemBusiUser querySysBusiUser, int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT b.* FROM BUSINESS_SYSTEM b WHERE b.STATUS_CD= ? and exists(SELECT 1 FROM SYSTEM_BUSI_USER s WHERE s.status_cd = ? and s.system_config_user_id = ? and b.business_system_id = s.business_system_id)");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);


		if (querySysBusiUser != null) {

			params.add(querySysBusiUser.getSystemConfigUserId());
		} else {
			params.add(0L);
		}

		sb.append(" ORDER BY BUSINESS_SYSTEM_ID");

		return this.systemBusiUserDao.jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, BusinessSystem.class);
	}

	@Override
	public void removeBusinessSystem(BusinessSystem businessSystem,SystemBusiUser querySysBusiUser) {
		if (businessSystem != null
				&& businessSystem.getBusinessSystemId() != null) {
			long businessSystemId = businessSystem.getId();
			long systemConfigUserId = querySysBusiUser.getSystemConfigUserId();
			Object[] params = new Object[]{businessSystemId,systemConfigUserId};
			String sql = "select * from SYSTEM_BUSI_USER where status_cd = 1000 and business_system_id = ? and system_config_user_id = ? ";
			List<SystemBusiUser> sysBusiUserList = (List<SystemBusiUser>) this.systemBusiUserDao.getJdbcTemplate().query(sql, params, new BeanPropertyRowMapper<SystemBusiUser>(SystemBusiUser.class));
			for(SystemBusiUser busiUser : sysBusiUserList) {
				busiUser.removeOnly();
			}
		}
	}
	
	/**
	 * 查询当前用户没有配置用户系统关系的系统节点列表
	 * @param systemConfigUserId
	 * @return
	 */
	@Override
	public List<NodeVo> queryBusinessSystemListByUserId(long systemConfigUserId) {
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(systemConfigUserId);
		
		String sql = "SELECT b.* FROM BUSINESS_SYSTEM b WHERE b.STATUS_CD= ? and not exists(SELECT 1 FROM SYSTEM_BUSI_USER s WHERE s.status_cd = ? and s.system_config_user_id = ? and b.business_system_id = s.business_system_id)";
		List<BusinessSystem> businessSystemList = (List<BusinessSystem>) this.systemBusiUserDao.getJdbcTemplate()
				.query(sql, params.toArray(), new BeanPropertyRowMapper<BusinessSystem>(BusinessSystem.class));
		
		List<NodeVo> retAttrValues = new ArrayList<NodeVo>();
		if (businessSystemList != null) {
			for (BusinessSystem bs : businessSystemList) {
				if (bs != null) {
					NodeVo vo = new NodeVo();
					vo.setId(bs.getBusinessSystemId().toString());
					vo.setName(bs.getSystemName());
					vo.setDesc(bs.getSystemDesc());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SystemBusiUser querySysBusiUserByUserSys(long businessSystemId,
			long systemConfigUserId) {
		String sqlStr = "select * from SYSTEM_BUSI_USER where status_cd = ? and business_system_id = ? and system_config_user_id = ?";
		
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(businessSystemId);
		params.add(systemConfigUserId);
		
		List<SystemBusiUser> sysBusiUserList = (List<SystemBusiUser>) this.systemBusiUserDao.getJdbcTemplate()
				.query(sqlStr, params.toArray(), new BeanPropertyRowMapper<SystemBusiUser>(SystemBusiUser.class));
		
		if(sysBusiUserList.size() > 0) {
			return sysBusiUserList.get(0);
		}
		
		return null;
	}
}
