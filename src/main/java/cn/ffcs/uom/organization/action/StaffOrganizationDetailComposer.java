package cn.ffcs.uom.organization.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationDetailBean;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

/**
 * 组织员工关系查看Composer.
 * 
 * @author faq
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class StaffOrganizationDetailComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffOrganizationDetailBean bean = new StaffOrganizationDetailBean();

	@Autowired
	@Qualifier("staffManager")
	private StaffManager staffManager;

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("partyManager")
	private PartyManager partyManager;

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("staticParameter")
	private StaticParameter staticParameter;

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;

	/**
	 * 组织关系.
	 */
	private StaffOrganization staffOrganization;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffBandboxExt().addForward(
				SffOrPtyCtants.ON_STAFF_ORG_SELECT, comp,
				SffOrPtyCtants.ON_STAFF_ORG_RESPONSE);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffOrganizationDetailWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> ralaCdList = UomClassProvider.getValuesList(
				"StaffOrganization", "ralaCd");
		ListboxUtils.rendererForEdit(this.bean.getRalaCd(), ralaCdList);

		List<NodeVo> liTp = UomClassProvider.getValuesList("Individual",
				"gender");
		ListboxUtils.rendererForEdit(bean.getGender(), liTp);

		liTp = UomClassProvider.getValuesList("Staff", "staffPosition");
		ListboxUtils.rendererForEdit(this.bean.getStaffPosition(), liTp);

		liTp = UomClassProvider.getValuesList("Party", "partyType");
		ListboxUtils.rendererForEdit(this.bean.getPartyType(), liTp);

		liTp = UomClassProvider.getValuesList("PartyOrganization", "orgType");
		ListboxUtils.rendererForEdit(bean.getOrgType(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "politicsStatus");
		ListboxUtils.rendererForEdit(bean.getPoliticsStatus(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "educationLevel");
		ListboxUtils.rendererForEdit(bean.getEducationLevel(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "religion");
		ListboxUtils.rendererForEdit(bean.getReligion(), liTp);

		liTp = UomClassProvider.getValuesList("Individual", "nation");
		ListboxUtils.rendererForEdit(bean.getNation(), liTp);

		liTp = UomClassProvider.getValuesList("Organization", "orgScale");
		ListboxUtils.rendererForEdit(bean.getOrgScale(), liTp);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		String mg = null;
		opType = StrUtil.strnull(arg.get("opType"));
		staffOrganization = (StaffOrganization) arg.get("staffOrganization");
		if ("view".equals(opType)) {
			if (staffOrganization != null) {
				PubUtil.fillBeanFromPo(staffOrganization, this.bean);
				Staff staff = staffOrganization.getStaff();
				if (null != staff) {
					// List<StaffAccount> sas =
					// staffManager.getStaffAccountList(staff);
					// if(null != sas && sas.size() > 0 && null != sas.get(0)){
					// StaffAccount sa = sas.get(0);
					// staff.setObjStaffAccount(sa);
					// staff.setStaffAccount(sa.getStaffAccount());
					// staff.setStaffNbr(sa.getStaffAccount());
					// staff.setStaffPassword(sa.getStaffPassword());
					// }
					StaffAccount sa = staffManager.getStaffAccount(null,
							staff.getStaffId());
					if (null != sa) {
						staff.setObjStaffAccount(sa);
						staff.setStaffAccount(sa.getStaffAccount());
						staff.setStaffPassword(sa.getStaffPassword());
					}
					PoliticalLocation pl = PoliticalLocation
							.getPoliticalLocation(staff.getLocationId());
					if (null != pl) {
						staff.setLocationName(pl.getLocationName());
						bean.getLocationId().setPoliticalLocation(pl);
						bean.getLocationId().getPoliticalLocation()
								.setLocationName(pl.getLocationName());
					}
					mg = staffManager.getPartyNameByStaffId(staff.getStaffId());
					if (null != mg) {
						this.bean.getPartyBandboxExt().setValue(mg);
					}
					mg = null;
					mg = staticParameter.handling("Staff", "workProp",
							staff.getWorkProp());
					if (null != mg) {
						this.bean.getWorkProp().setValue(mg);
					}
					mg = null;
					mg = staticParameter.handling("Staff", "staffProperty",
							staff.getStaffProperty());
					if (null != mg) {
						this.bean.getStaffProperty().setValue(mg);
					}
					mg = null;
					PubUtil.fillBeanFromPo(staff, this.bean);
					this.bean.getStaffNbr().setValue(sa.getStaffAccount());
					this.bean.getStaffBandboxExt().setStaff(staff);
					if (staffOrganization.getStaffSeq() != null) {
						this.bean.getStaffSeq().setValue(
								new Integer(staffOrganization.getStaffSeq()
										+ ""));
					}
					if (staffOrganization.getOrgId() != null) {
						Organization org = organizationManager
								.getById(staffOrganization.getOrgId());
						this.bean.getOrg().setOrganization(org);
					}
					Party party = partyManager.getPartyByStaff(staff);
					if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(party
							.getPartyType())) {
						Individual indivi = partyManager.getIndividual(party
								.getPartyId());
						if (null != indivi) {
							bean.getPel().setVisible(true);
							bean.getOrgs().setVisible(false);
							mg = staticParameter.handling(new String[] {
									"Individual", "maritalStatus",
									indivi.getMarriageStatus() });
							if (null != mg) {
								bean.getMarriageStatus().setValue(mg);
							}
							mg = null;
							mg = staticParameter.handling(new String[] {
									"Individual", "nationality",
									indivi.getNationality() });
							if (null != mg) {
								bean.getNationality().setValue(mg);
							}
							mg = null;
							PubUtil.fillBeanFromPo(indivi, bean);
						}
					} else {
						if (party.getPartyId() != null) {
							PartyOrganization partyOrg = partyManager
									.getPartyOrg(party.getPartyId());
							if (null != partyOrg) {
								bean.getOrgs().setVisible(true);
								bean.getPel().setVisible(false);
								PubUtil.fillBeanFromPo(partyOrg, bean);
							}
							List<PartyRole> liPr = partyManager
									.getPartyRoleByPtId(party.getPartyId());
							mg = staticParameter.handling(new String[] {
									"PartyRole", "roleType",
									liPr.get(0).getRoleType() });
							if (null != mg) {
								this.bean.getRoleType().setValue(mg);
							}
							mg = null;
						}
					}
					if (party.getPartyId() != null) {
						List<PartyRole> liPr = partyManager
								.getPartyRoleByPtId(party.getPartyId());
						if (null != liPr && liPr.size() > 0) {
							mg = staticParameter.handling(new String[] {
									"PartyRole", "roleType",
									liPr.get(0).getRoleType() });
							bean.getRoleType().setValue(mg);
						}
					}
					PubUtil.fillBeanFromPo(party, this.bean);
					List<StaffExtendAttr> staffExtendAttrList = staffManager
							.getStaffExtendAttr(staff.getStaffId());
					if (null != staffExtendAttrList
							&& staffExtendAttrList.size() > 0) {
						this.bean.getStaffExtendAttrExt().setExtendValue(
								staffExtendAttrList);
					}
				}
			}
		}
	}
}
