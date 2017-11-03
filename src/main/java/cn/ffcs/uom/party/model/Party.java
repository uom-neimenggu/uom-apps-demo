package cn.ffcs.uom.party.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ChineseSpellUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.systemconfig.manager.FilterConfigManager;
import cn.ffcs.uom.systemconfig.manager.impl.FilterConfigManagerImpl;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 参与人封装 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
public class Party extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 参与人标识.
	 **/
	public Long getPartyId() {
		return super.getId();
	}

	public void setPartyId(Long partyId) {
		super.setId(partyId);
	}

	/**
	 * 参与人名称.
	 **/
	@Getter
	private String partyName;

	public void setPartyName(String partyName) {
		FilterConfigManager fcm = new FilterConfigManagerImpl();
		this.partyName = fcm.filterAllByActive(partyName);
	}

	/**
	 * 参与人曾用名.
	 **/
	@Getter
	@Setter
	private String partyNameFirst;

	/**
	 * 参与人简拼.
	 **/
	@Getter
	@Setter
	private String partyAbbrname;

	/**
	 * 参与人英文名.
	 **/
	@Getter
	@Setter
	private String englishName;

	/**
	 * 参与人类型.
	 **/
	@Getter
	@Setter
	private String partyType;

	/**
	 * 参与人证件
	 */
	@Setter
	@Getter
	private List<PartyCertification> partyCertificationList;

	/**
	 * 参与人角色类型.
	 **/
	@Getter
	@Setter
	private String roleType;

	/**
	 * 参与人角色类型.
	 **/
	@Getter
	@Setter
	private String partyRoleId;

	/**
	 * 证件类型
	 */
	@Getter
	@Setter
	private String certType;

	/**
	 * 证件号码
	 */
	@Getter
	@Setter
	private String certNumber;

	/**
	 * 员工账号
	 */
	@Getter
	@Setter
	private String staffAccount;

	/**
	 * 是否游离
	 */
	@Getter
	@Setter
	private String freeParty;

	/**
	 * 员工
	 */
	@Getter
	@Setter
	private Staff staff;

	/**
	 * 首选联系人号码
	 */
	@Getter
	@Setter
	private String mobilePhone;

	@Getter
	@Setter
	private PartyRole partyRole;

	/**
	 * 个人信息
	 */
	@Setter
	@Getter
	private Individual individual;

	/**
	 * 参与人联系人信息
	 */
	@Setter
	@Getter
	private PartyContactInfo partyContactInfo;
	/**
	 * 参与人证件类型
	 */
	@Setter
	@Getter
	private PartyCertification partyCertification;

	/**
	 * 参与人组织
	 */
	@Setter
	@Getter
	private PartyOrganization partyOrganization;

	/**
	 * 员工组织
	 */
	@Setter
	@Getter
	private StaffOrganization staffOrganization;
	/**
	 * 集团员工数据
	 */
	@Setter
	@Getter
	private Group group;

	/**
     * 
     */
	@Setter
	@Getter
	private List<PartyContactInfo> partyContactInfoList;
	/**
	 * 修改员工接口是否需要更新
	 */
	@Setter
	@Getter
	private Boolean isNeedUpdate = false;

	public Party() {
		super();
	}

	public static Party newInstance() {
		return new Party();
	}

	public String getPartyTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getPartyType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"Party", "partyType", this.getPartyType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getRoleTypeName() {
		if (!StrUtil.isNullOrEmpty(this.getRoleType())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"PartyRole", "roleType", this.getRoleType(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	public String getStatusCdName() {
		if (!StrUtil.isNullOrEmpty(this.getStatusCd())) {
			if (BaseUnitConstants.ENTT_STATE_ACTIVE.equals(this.getStatusCd())) {
				return "生效";
			} else {
				return "失效";
			}
		}
		return "";
	}

	public String getStaffAccountStr() {
		if (!StrUtil.isNullOrEmpty(this.getPartyRoleId())) {
			String sql = "SELECT SA.* FROM STAFF_ACCOUNT SA, STAFF S WHERE S.PARTY_ROLE_ID = ? AND SA.STAFF_ID= S.STAFF_ID AND SA.STATUS_CD= ?";
			List<Object> params = new ArrayList<Object>();
			params.add(this.getPartyRoleId());
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			List<StaffAccount> sas = DefaultDaoFactory.getDefaultDao()
					.jdbcFindList(sql, params, StaffAccount.class);
			if (null != sas && sas.size() > 0) {
				for (StaffAccount sa : sas) {
					if (sa.getStaffAccount().equals(this.getStaffAccount())) {
						return sa.getStaffAccount();
					}
				}
				return sas.get(0).getStaffAccount();
			}
		}
		return "";
	}

}
