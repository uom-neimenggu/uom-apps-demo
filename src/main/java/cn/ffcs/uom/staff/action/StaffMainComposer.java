package cn.ffcs.uom.staff.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.action.bean.StaffMainBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 员工管理实体Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-27
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private StaffMainBean bean = new StaffMainBean();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * staff.
	 */
	private Staff staff;

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
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffListboxExt().setPortletInfoProvider(this);
		bean.getStaffOrganizationListbox().setPortletInfoProvider(this);
		bean.getStaffPositionListboxExt().setPortletInfoProvider(this);
		bean.getStaffCtPositionListboxExt().setPortletInfoProvider(this);
		bean.getStaffOrgTranListboxExt().setPortletInfoProvider(this);
		bean.getStaffGrpStaffListboxExt().setPortletInfoProvider(this);
		bean.getPartyContactInfoListboxExt().setPortletInfoProvider(this);
		bean.getPartyCertificationListboxExt().setPortletInfoProvider(this);
		bean.getPartyRoleListboxExt().setPortletInfoProvider(this);
		bean.getStaffListboxExt().addForward(SffOrPtyCtants.ON_STAFF_SELECT,
				comp, "onSelectStaffResponse");
		bean.getStaffListboxExt().addForward(
				SffOrPtyCtants.ON_STAFF_MANAGE_QUERY, comp,
				"onSelectStaffResponse");
		bean.getStaffListboxExt().addForward(SffOrPtyCtants.ON_STAFF_DELETE,
				comp, "onDeleteStaffResponse");
		bean.getStaffListboxExt().addForward(SffOrPtyCtants.ON_STAFF_SAVE,
				comp, "onSaveStaffResponse");
		bean.getStaffListboxExt().addForward(
				SffOrPtyCtants.ON_STAFF_CLEAR_TABS, comp,
				SffOrPtyCtants.ON_STAFF_CLEAR_TABS_RESPONS);
		bean.getStaffListboxExt().setStaffWindowDivVisible(true);
		/**
		 * 隐藏“新增员工组织”、“修改员工组织”、“查看员工组织”按钮
		 */
		Events.postEvent(SffOrPtyCtants.ON_STAFF_Org_QUERY,
				this.bean.getStaffOrganizationListbox(), staff);
		/**
		 * 员工角色listbox注册
		 */
		bean.setStaffRoleListbox(this.self.getFellow("staffRoleListbox")
				.getFellow("staffRoleMainWin"));
		/**
		 * 员工系统listbox注册
		 */
		bean.setStaffSysListbox(this.self.getFellow("staffSysListbox")
				.getFellow("staffSysMainWin"));

	}

	/**
	 * 设置角色保存事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveRoleResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			Staff staff = (Staff) event.getOrigin().getData();
			if (staff != null) {
				Events.postEvent(SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_ROLE,
						this.bean.getStaffRoleListbox(), staff);
			}
		}
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffMainWin() {
		try {
			bindBox();
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 绑定combobox和listBox. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	private void bindBox() {

	}

	/**
	 * tab响应
	 * 
	 * @throws Exception
	 * 
	 */
	public void callTab() {
		try {
			if (bean.getTab() == null) {
				bean.setTab(bean.getTabBox().getSelectedTab());
			}
			if (staff != null) {
				String tab = this.bean.getTab().getId();
				if ("staffOrgTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_Org_QUERY,
							this.bean.getStaffOrganizationListbox(), staff);
				} else if ("staffPositionTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_POSITION_QUERY,
							this.bean.getStaffPositionListboxExt(), staff);
				} else if ("staffCtPositionTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_CT_POSITION_QUERY,
							this.bean.getStaffCtPositionListboxExt(), staff);
				} else if ("staffOrgTranTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_ORG_TRAN_SELECT,
							bean.getStaffOrgTranListboxExt(), staff);
				} else if ("staffGrpStaffTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_GRP_STAFF_QUERY,
							bean.getStaffGrpStaffListboxExt(), staff);
				} else if ("staffRoleTab".equals(tab)) {
					Events.postEvent(
							SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_ROLE,
							bean.getStaffRoleListbox(), staff);
				} else if ("staffSysTab".equals(tab)) {
					Events.postEvent(
							SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_SYS,
							bean.getStaffSysListbox(), staff);
				} else if ("partyContactInfoTab".equals(tab)) {
					Party party = getPartyByStaff();
					bean.getPartyContactInfoListboxExt().setParty(party);
					bean.getPartyContactInfoListboxExt().init();
				} else if ("partyCertificationTab".equals(tab)) {
					Party party = getPartyByStaff();
					bean.getPartyCertificationListboxExt().setParty(party);
					bean.getPartyCertificationListboxExt().init();
				} else if ("partyRoleTab".equals(tab)) {
					Party party = getPartyByStaff();
					Events.postEvent(SffOrPtyCtants.ON_PARTY_ROLE_QUERY,
							this.bean.getPartyRoleListboxExt(), party);
				}
			} else {
				bean.getPartyContactInfoListboxExt()
						.onCleaningPartyContactInfo();
				bean.getPartyCertificationListboxExt()
						.onCleaningPartyCertification();
				bean.getStaffPositionListboxExt().onCleanStaffPositiRespons(
						null);
				bean.getStaffCtPositionListboxExt().onCleanStaffCtPositionResponse(
						null);
				bean.getStaffOrgTranListboxExt().onCleanStaffOrgTranRespons();
				bean.getStaffGrpStaffListboxExt().onCleanStaffGrpStaffRespons(
						null);
				bean.getPartyRoleListboxExt().onCleanPartyRole();
				bean.getStaffOrganizationListbox().onCleanStaffOrgRespons(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/**
	 * 处理点击后Tabs .
	 * 
	 * @param event
	 * @author wangyong 2013-6-11 wangyong
	 */
	public void onStaffClearTabsRespons(final ForwardEvent event) {
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFF_ORG,
				bean.getStaffOrganizationListbox(), null);
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFF_POSITI,
				bean.getStaffPositionListboxExt(), null);
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFF_CT_POSITI,
				bean.getStaffCtPositionListboxExt(), null);
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFFORG_TRAN,
				bean.getStaffOrgTranListboxExt(), null);
		Events.postEvent(SffOrPtyCtants.ON_CLEAN_STAFF_GRP_STAFF,
				bean.getStaffGrpStaffListboxExt(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getStaffListboxExt().setPagePosition("staffPage");
		this.bean.getStaffOrganizationListbox().setPagePosition("staffPage");
		this.bean.getStaffPositionListboxExt().setPagePosition("staffPage");
		this.bean.getStaffCtPositionListboxExt().setPagePosition("staffPage");
		this.bean.getStaffOrgTranListboxExt().setPagePosition("staffPage");
		this.bean.getStaffGrpStaffListboxExt().setPagePosition("staffPage");
		this.bean.getPartyContactInfoListboxExt().setPagePosition("staffPage");
		this.bean.getPartyCertificationListboxExt()
				.setPagePosition("staffPage");
		this.bean.getPartyRoleListboxExt().setPagePosition("staffPage");
		Events.postEvent(SffOrPtyCtants.ON_STAFF_ROLE_PAGE_POSITION,
				bean.getStaffRoleListbox(), "staffPage");
	}
}
