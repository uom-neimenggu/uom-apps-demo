package cn.ffcs.uom.businesssystem.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.businesssystem.dao.SystemOrgTreeConfigDao;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 接入系统组织树引用配置实体.
 * 
 * @author
 * 
 **/
public class SystemOrgTreeConfig extends UomEntity implements Serializable {
	public Long getSystemOrgTreeConfigId() {
		return super.getId();
	}

	public void setSystemOrgTreeConfigId(Long systemOrgTreeConfigId) {
		super.setId(systemOrgTreeConfigId);
	}

	/**
	 * 接入系统标识.
	 **/
	@Getter
	@Setter
	private Long businessSystemId;
	/**
	 * 组织树标识.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 * 生成开关.
	 **/
	@Getter
	@Setter
	private String generationSwitch;
	/**
	 * 最后更新时间.
	 **/
	@Getter
	@Setter
	private Date lastTime;

	/**
	 * 获取dao
	 */
	public static SystemOrgTreeConfigDao repository() {
		return (SystemOrgTreeConfigDao) ApplicationContextUtil
				.getBean("systemOrgTreeConfigDao");
	}

	public String getSystemName() {
		if (this.getBusinessSystemId() != null) {
			BusinessSystem businessSystem = (BusinessSystem) BusinessSystem
					.repository().getObject(BusinessSystem.class,
							this.getBusinessSystemId());
			if (businessSystem != null) {
				return businessSystem.getSystemName();
			}
		}
		return "";
	}

	public String getOrgTreeName() {
		if (this.getOrgTreeId() != null) {
			OrgTree orgTree = (OrgTree) Organization.repository().getObject(
					OrgTree.class, this.getOrgTreeId());
			if (orgTree != null) {
				return orgTree.getOrgTreeName();
			}
		}
		return "";
	}

	public String getGenerationSwitchName() {
		if (!StrUtil.isNullOrEmpty(this.getGenerationSwitch())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"SystemOrgTreeConfig", "generationSwitch",
					this.getGenerationSwitch(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}
}
