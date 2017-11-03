package cn.ffcs.uom.dataOperatorQuery.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Tab;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataOperatorQuery.action.bean.DataOperatorQueryBean;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014年11月19日
 * @功能说明：数据操作人查询
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class DataOperatorQueryComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	private DataOperatorQueryBean bean = new DataOperatorQueryBean();
	/**
	 * 选中的员工
	 */
	private Staff staff;
	/**
	 * 查询的员工
	 */
	private Staff qryStaff;

	private Organization organization;

	private Organization qryOrg;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private OrganizationManager orgManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffOrganizationOperatorListboxExt().setPortletInfoProvider(
				this);
		bean.getOrganizationRelationOperatorListboxExt()
				.setPortletInfoProvider(this);
		bean.getStaffOperatorListBox().addForward(
				SffOrPtyCtants.ON_STAFF_SELECT, comp, "onSelectStaffResponse");
	}

	public DataOperatorQueryComposer() throws Exception {
		permissionOrganizationList = null;
		loadPermission();
	}

	/**
	 * 加载数据权
	 * 
	 * @throws Exception
	 */
	public void loadPermission() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg
						.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);
			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffOperator() throws Exception {

		this.bean.getStaffAccount().setValue("");

		this.bean.getStaffName().setValue("");

		this.bean.getStaffCode().setValue("");

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetOrganizationOperator() throws Exception {

		this.bean.getOrgCode().setValue("");

		this.bean.getOrgName().setValue("");

	}

	/**
	 * 查询员工
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffOperator() throws Exception {
		try {
			qryStaff = null;
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			qryStaff = objSff;
			qryStaff.setPermissionOrganizationList(permissionOrganizationList);
			this.bean.getStaffOperatorListBoxPaging().setActivePage(0);
			if (qryStaff != null) {
				loadStaffOperatorInfo();
			} else {
				ZkUtil.showError("请输入要查询的账号！", "系统提示");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 员工分页
	 * 
	 * @throws Exception
	 * @author fangy 2014年11月21日
	 */
	public void onStaffOperatorListBoxPaging() throws Exception {
		PageInfo pageInfo = this.loadStaffOperatorInfo();
	}

	/**
	 * 组织分页
	 * 
	 * @throws Exception
	 * @author fangy 2014年11月21日
	 */
	public void onOrgOperatorListBoxPaging() throws Exception {
		PageInfo pageInfo = this.loadOrgOperatorInfo();
	}

	/**
	 * 查询组织
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganizationOperator() throws Exception {

		try {
			qryOrg = null;
			Organization objOrg = Organization.newInstance();
			PubUtil.fillPoFromBean(bean, objOrg);
			qryOrg = objOrg;
			qryOrg.setPermissionOrganizationList(permissionOrganizationList);
			this.bean.getOrgOperatorListBoxPaging().setActivePage(0);
			if (!(StrUtil.isNullOrEmpty(objOrg.getOrgCode()) && StrUtil
					.isNullOrEmpty(objOrg.getOrgName()))) {
				loadOrgOperatorInfo();
			} else {
				ZkUtil.showError("请输入要查询的组织！", "系统提示");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加载用户信息
	 * 
	 * @throws Exception
	 */
	public PageInfo loadStaffOperatorInfo() throws Exception {
		PageInfo pageInfo = null;
		if (this.qryStaff != null) {
			pageInfo = staffManager.forQuertyStaffNoStatus(qryStaff, bean
					.getStaffOperatorListBoxPaging().getActivePage() + 1, bean
					.getStaffOperatorListBoxPaging().getPageSize());
			List<Staff> list = pageInfo.getDataList();
			if (list.size() == 0) {
				ZkUtil.showError("查询无记录，请重新输入！", "系统提示");
				return null;
			}
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getStaffOperatorListBox().setModel(dataList);
			bean.getStaffOperatorListBoxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		return pageInfo;
	}

	/**
	 * 加载组织修改信息
	 * 
	 * @throws Exception
	 */
	public PageInfo loadOrgOperatorInfo() throws Exception {
		PageInfo pageInfo = null;
		if (this.qryOrg != null) {
			pageInfo = orgManager.queryPageInfoByOrganizationNoStatusCd(qryOrg,
					bean.getOrgOperatorListBoxPaging().getActivePage() + 1,
					bean.getOrgOperatorListBoxPaging().getPageSize());
			List<Organization> list = pageInfo.getDataList();
			if (list.size() == 0) {
				ZkUtil.showError("查询无记录，请重新输入！", "系统提示");
				return null;
			}
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getOrgOperatorListBox().setModel(dataList);
			bean.getOrgOperatorListBoxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		return pageInfo;
	}

	/**
	 * 员工选择.
	 * 
	 * @throws Exception
	 */
	public void onStaffSelectRequest() throws Exception {
		if (bean.getStaffOperatorListBox().getSelectedCount() > 0) {
			Staff staff = (Staff) bean.getStaffOperatorListBox()
					.getSelectedItem().getValue();
			Events.postEvent(SffOrPtyCtants.ON_STAFF_SELECT,
					this.bean.getStaffOperatorListBox(), staff);
		}
		if (bean.getStaffOperatorListBox().getSelectedCount() == 0) {
			// Events.postEvent(SffOrPtyCtants.ON_STAFF_NOT_SELECT, this, null);
			ZkUtil.showInformation("HI", "Hi");
		}
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 */
	public void onOrganizationSelectRequest() throws Exception {
		if (bean.getOrgOperatorListBox().getSelectedCount() > 0) {
			Organization organization = (Organization) bean
					.getOrgOperatorListBox().getSelectedItem().getValue();
			Events.postEvent(OrganizationConstant.ON_SELECT_ORGANIZATION,
					this.bean.getOrganizationRelationOperatorListboxExt(),
					organization);
		}
		if (bean.getOrgOperatorListBox().getSelectedCount() == 0) {
			ZkUtil.showInformation("HI", "Hi");
		}
	}

	/**
	 * 选择员工列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onSelectStaffResponse(final ForwardEvent event)
			throws Exception {
		staff = (Staff) event.getOrigin().getData();
		callTab();
	}

	/**
	 * 根据员工ID获取参与人实体
	 * 
	 * @return
	 */
	private Party getPartyByStaff() {
		Party p = partyManager.getPartyByStaff(staff);
		return p;
	}

	public void onClickTab(ForwardEvent event) {
		try {
			Event origin = event.getOrigin();
			if (origin != null) {
				Component comp = origin.getTarget();
				if (comp != null && comp instanceof Tab) {
					bean.setTab((Tab) comp);
					callTab();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void callTab() {
		try {
			if (bean.getTab() == null) {
				bean.setTab(bean.getTabBox().getSelectedTab());
			}
			if (staff != null) {
				String tab = this.bean.getTab().getId();
				if ("staffOrganizationTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_Org_QUERY,
							this.bean.getStaffOrganizationOperatorListboxExt(),
							staff);
				} else if ("partyContactInfoTab".equals(tab)) {
					bean.getPartyContactInfoListboxExt()
							.onCleaningPartyContactInfo();
					Party party = getPartyByStaff();
					bean.getPartyContactInfoListboxExt().setParty(party);
					bean.getPartyContactInfoListboxExt().init();
				} else if ("partyCertificationTab".equals(tab)) {
					bean.getPartyCertificationListboxExt()
							.onCleaningPartyCertification();
					Party party = getPartyByStaff();
					bean.getPartyCertificationListboxExt().setParty(party);
					bean.getPartyCertificationListboxExt().init();
				}
			} else {
				bean.getPartyContactInfoListboxExt()
						.onCleaningPartyContactInfo();
				bean.getPartyCertificationListboxExt()
						.onCleaningPartyCertification();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
