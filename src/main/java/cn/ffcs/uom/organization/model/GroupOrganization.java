package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrganizationTranDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/***
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-13
 * @功能说明：
 * 
 */
public class GroupOrganization implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

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
	 * 分组.
	 **/
	@Getter
	@Setter
	private String orgGroup;

	/**
	 * 组织全称.
	 **/
	@Getter
	@Setter
	private String orgFullName;

	/**
	 * 供应商名称.
	 **/
	@Getter
	@Setter
	private String supplierName;

	/**
	 * 供应商编码.
	 **/
	@Getter
	@Setter
	private String supplierCode;

	/**
	 * 状态.
	 **/
	@Getter
	@Setter
	private String statusCd;

	/**
	 * 组织来源.
	 **/
	@Getter
	@Setter
	private String orgType;

	/**
	 * 组织说明.
	 **/
	@Getter
	@Setter
	private String orgDesc;

	/**
	 * 备注.
	 **/
	@Getter
	@Setter
	private String tDesc;

	/**
	 * 查询使用的组织来源
	 */
	@Setter
	@Getter
	private List<String> queryOrgTypeList;

	/**
	 * 组织来源
	 */
	public String getStatusCdName() {
		if (!StrUtil.isNullOrEmpty(this.getStatusCd())) {
			if (this.getStatusCd().equals(BaseUnitConstants.ENTT_STATE_ACTIVE)) {
				return "生效";
			} else {
				return "失效";
			}
		}
		return "";
	}

	/**
	 * 组织来源
	 */
	public String getOrgTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getOrgType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"GroupOrganization", "orgType", this.getOrgType(),
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
