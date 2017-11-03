package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrganizationTranDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/***
 * 组织业务关系 .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-13
 * @功能说明：
 * 
 */
public class OrganizationTran extends UomEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 组织业务关系标识
	 */
	public Long getOrgTranId() {
		return super.getId();
	}

	public void setOrgTranId(Long orgTranId) {
		super.setId(orgTranId);
	}

	/**
	 * 组织标识
	 */
	@Getter
	@Setter
	private Long orgId;

	/**
	 * 组织编码.
	 **/
	@Getter
	@Setter
	private String orgCode;

	/**
	 * 组织名称.
	 **/
	@Getter
	@Setter
	private String orgName;

	/**
	 * 业务组织标识
	 */
	@Getter
	@Setter
	private Long tranOrgId;

	/**
	 * 业务组织编码.
	 **/
	@Getter
	@Setter
	private String tranOrgCode;

	/**
	 * 业务组织名称.
	 **/
	@Getter
	@Setter
	private String tranOrgName;

	/**
	 * 业务关系类型
	 */
	@Getter
	@Setter
	private String tranRelaType;

	/**
	 * 电信管理区域标识
	 */
	@Getter
	@Setter
	private Long telcomRegionId;

	/**
	 * 查询使用的组织类型
	 */
	@Setter
	@Getter
	private List<OrgType> queryOrgTypeList;
	/**
	 * 查询使用的业务组织类型
	 */
	@Setter
	@Getter
	private List<OrgType> queryTranOrgTypeList;

	/**
	 * 数据权限：组织
	 */
	@Setter
	@Getter
	private List<Organization> permissionOrganizationList;

	/**
	 * listbox是否只有新营销单元组织
	 */
	@Getter
	@Setter
	private Boolean isMarketingListbox = false;
	/**
	 * listbox是否只有财务树组织
	 */
	@Getter
	@Setter
	private Boolean isFinancialListbox = false;

	/**
	 * 状态名称
	 */
//	public String getStatusCdName() {
//		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
//				"OrganizationTran", "statusCd", this.getStatusCd(),
//				BaseUnitConstants.ENTT_STATE_ACTIVE);
//		if (list != null && list.size() > 0
//				&& !StrUtil.isEmpty(this.getStatusCd())) {
//			return list.get(0).getAttrValueName();
//		}
//		return "";
//	}

	/**
	 * 业务关系类型名称
	 */
	public String getTranRelaTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getTranRelaType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"OrganizationTran", "tranRelaType", this.getTranRelaType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrganizationTranDao repository() {
		return (OrganizationTranDao) ApplicationContextUtil
				.getBean("organizationTranDao");
	}

}
