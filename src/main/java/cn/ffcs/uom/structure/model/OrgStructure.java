package cn.ffcs.uom.structure.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.structure.dao.OrgStructureDao;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class OrgStructure extends UomEntity implements Serializable {
	@Getter
	@Setter
	private Long rowNumId;
	@Getter
	@Setter
	private String orgTreeName;
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
	private String orgFullName;
	@Getter
	@Setter
	private Long telcomRegionId;
	@Getter
	@Setter
	private String orgUuId;
	@Getter
	@Setter
	private Long orgFixId;
	@Getter
	@Setter
	private Long relaOrgId;
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
	 * 查询条件：组织
	 */
	@Getter
	@Setter
	private Organization queryOrganization;
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

	public String getRegionName() {
		if (this.getTelcomRegionId() != null) {
			TelcomRegion telcomRegion = (TelcomRegion) OrgStructure
					.repository().getObject(TelcomRegion.class,
							this.getTelcomRegionId());
			if (telcomRegion != null
					&& !StrUtil.isEmpty(telcomRegion.getRegionName())) {
				return telcomRegion.getRegionName();
			}
		}
		return "";
	}
}
