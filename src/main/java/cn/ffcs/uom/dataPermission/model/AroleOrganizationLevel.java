package cn.ffcs.uom.dataPermission.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationLevelDao;

public class AroleOrganizationLevel extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 权限角色组织层级标识
	 */
	public Long getAroleOrgLevelId() {
		return super.getId();
	}

	public void setAroleOrgLevelId(Long aroleOrgLevelId) {
		super.setId(aroleOrgLevelId);
	}

	/**
	 * 权限角色标识
	 */
	@Getter
	@Setter
	private Long aroleId;
	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;

	/**
	 * 最小层级
	 */
	@Getter
	@Setter
	private Long lowerLevel;

	/**
	 * 最大层级
	 */
	@Getter
	@Setter
	private Long higherLevel;

	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private String relaCd;

	public static AroleOrganizationLevelDao repository() {
		return (AroleOrganizationLevelDao) ApplicationContextUtil
				.getBean("aroleOrganizationLevelDao");
	}

}
