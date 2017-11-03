package cn.ffcs.uom.businesssystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.businesssystem.dao.SystemOrgTreeConfigDao;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.BusinessSystemDao;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

/**
 * 接入系统表实体.
 * 
 * @author
 * 
 **/
public class BusinessSystem extends UomEntity implements Serializable {
	public Long getBusinessSystemId() {
		return super.getId();
	}

	public void setBusinessSystemId(Long businessSystemId) {
		super.setId(businessSystemId);
	}

	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 * 系统名称.
	 **/
	@Getter
	@Setter
	private String systemName;
	/**
	 * 系统描述.
	 **/
	@Getter
	@Setter
	private String systemDesc;
	/**
	 * 服务地址.
	 **/
	@Getter
	@Setter
	private String systemUrl;
	/**
	 * 全局标识码.
	 **/
	@Getter
	@Setter
	private String uuid;
	/**
	 * 系统接口信息配置
	 */
	@Setter
	private List<SystemIntfInfoConfig> systemIntfInfoConfigList;
	/**
	 * 系统接口配置组织树
	 */
	@Setter
	private List<SystemOrgTreeConfig> systemOrgTreeConfigList;

	/**
	 * 获取dao
	 */
	public static BusinessSystemDao repository() {
		return (BusinessSystemDao) ApplicationContextUtil
				.getBean("businessSystemDao");
	}

	/**
	 * 获取系统接口信息配置
	 */
	public List<SystemIntfInfoConfig> getSysIntfInfoConfigList() {
		if (systemIntfInfoConfigList == null
				|| systemIntfInfoConfigList.size() <= 0) {
			if (!StrUtil.isNullOrEmpty(this.getSystemCode())) {
				String sql = "SELECT * FROM SYSTEM_INTF_INFO_CONFIG WHERE STATUS_CD= ? AND SYSTEM_CODE = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getSystemCode());
				systemIntfInfoConfigList = SystemIntfInfoConfig.repository()
						.jdbcFindList(sql, params, SystemIntfInfoConfig.class);
			}
		}
		return systemIntfInfoConfigList;
	}

	/**
	 * 获取系统接口配置组织树
	 */
	public List<SystemOrgTreeConfig> getSysOrgTreeConfigList() {
		if (systemOrgTreeConfigList == null
				|| systemOrgTreeConfigList.size() <= 0) {
			if (!StrUtil.isNullOrEmpty(this.getBusinessSystemId())) {
				String sql = "SELECT * FROM SYSTEM_ORG_TREE_CONFIG WHERE STATUS_CD= ? AND BUSINESS_SYSTEM_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getBusinessSystemId());
				systemOrgTreeConfigList = SystemOrgTreeConfig.repository()
						.jdbcFindList(sql, params, SystemOrgTreeConfig.class);
			}
		}
		return systemOrgTreeConfigList;
	}
}
