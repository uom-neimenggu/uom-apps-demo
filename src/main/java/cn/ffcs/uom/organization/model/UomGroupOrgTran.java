package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
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
public class UomGroupOrgTran extends UomEntity implements Serializable {

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
	private String tranOrgId;

	/**
	 * 业务组织编码.
	 **/
	@Getter
	@Setter
	private String tranOrgCode;

	/**
	 * 供应商组织名称.
	 **/
	@Getter
	@Setter
	private String supplierName;

	/**
	 * 供应商组织编码.
	 **/
	@Getter
	@Setter
	private String supplierCode;

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
	 * 以店包区标识
	 */
	@Getter
	@Setter
	private String storeAreaFlag;

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
	 * 查询使用的业务组织来源
	 */
	@Setter
	@Getter
	private List<String> queryTranOrgTypeList;

	/**
	 * 数据权限：组织
	 */
	@Setter
	@Getter
	private List<Organization> permissionOrganizationList;

	/**
	 * 状态名称
	 */
	// public String getStatusCdName() {
	// List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
	// "UomGroupOrgTran", "statusCd", this.getStatusCd(),
	// BaseUnitConstants.ENTT_STATE_ACTIVE);
	// if (list != null && list.size() > 0
	// && !StrUtil.isEmpty(this.getStatusCd())) {
	// return list.get(0).getAttrValueName();
	// }
	// return "";
	// }

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
	
	
	/**
	 * 获取当前关系的组织对应的组织类型
	 * .
	 * 
	 * @return
	 * @author xiaof
	 * 2017年3月17日 xiaof
	 */
	public List<OrgType> getOrganizationOrgTypeList()
	{
	    //只要组织id不为空，那么就可以查询对应的组织类型
	    List<OrgType> result = null;
	    if(this.orgId != null)
	    {
	        String sql = "SELECT * FROM ORG_TYPE A WHERE A.STATUS_CD=? AND A.ORG_ID=? ";
	        List params = new ArrayList();
	        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
	        params.add(this.orgId);
	        result = Organization.repository().jdbcFindList(sql, params, OrgType.class);
	    }
	    
	    return result;
	}

}
