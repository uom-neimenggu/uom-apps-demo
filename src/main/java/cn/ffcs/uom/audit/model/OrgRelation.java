package cn.ffcs.uom.audit.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.structure.dao.OrgStructureDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class OrgRelation extends UomEntity implements Serializable {
	@Getter
	@Setter
	private Long orgId;
	@Getter
	@Setter
	private String orgCode;
	@Getter
	@Setter
	private String orgName;
	@Getter
	@Setter
	private Long telcomRegionId;
	@Getter
	@Setter
	private String regionName;
	@Getter
	@Setter
	private String checkResult;

	/**
	 * 查询条件：电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegion queryTelcomRegion;
	/**
	 * 查询条件：是否包含电信管理区域下级
	 */
	@Getter
	@Setter
	private boolean queryIncludeChildren;

	/**
	 * 查询条件：是否包含组织下级
	 */
	@Getter
	@Setter
	private boolean queryIncludeOrgChildren;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgStructureDao repository() {
		return (OrgStructureDao) ApplicationContextUtil
				.getBean("orgStructureDao");
	}

	/*
	 * public String getRegionName() { if (this.getTelcomRegionId() != null) {
	 * TelcomRegion telcomRegion = (TelcomRegion) OrgRelation.repository()
	 * .getObject(TelcomRegion.class, this.getTelcomRegionId()); if
	 * (telcomRegion != null && !StrUtil.isEmpty(telcomRegion.getRegionName()))
	 * { return telcomRegion.getRegionName(); } } return ""; }
	 */
}
