package cn.ffcs.uom.activation.action;

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
import cn.ffcs.uom.activation.action.bean.ActivationMainBean;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 激活管理实体Composer.
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-09-13
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class ActivationMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private ActivationMainBean bean = new ActivationMainBean();

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	/**
	 * staff.
	 */
	private Staff staff;

	/**
	 * organization.
	 */
	private Organization organization;

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
		bean.getStaffActivationListboxExt().setPortletInfoProvider(this);
		bean.getOrganizationActivationListboxExt().setPortletInfoProvider(this);
		initPage();
		/*
		 * bean.getStaffActivationListboxExt().addForward(
		 * ActivationConstant.ON_ACTIVATION_CLEAR_TABS, comp,
		 * ActivationConstant.ON_ACTIVATION_CLEAR_TABS_RESPONS);
		 */
	}

	/**
	 * tab响应
	 * 
	 * @throws Exception
	 * 
	 */
	public void callTab() {
		try {
			if (this.bean.getTab() == null) {
				this.bean.setTab(this.bean.getTabBox().getSelectedTab());
			}

			String tab = this.bean.getTab().getId();
			if ("staffActivationTab".equals(tab)) {
				Events.postEvent(SffOrPtyCtants.ON_STAFF_QUERY,
						this.bean.getStaffActivationListboxExt(), staff);
			} else if ("organizationActivationTab".equals(tab)) {
				Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY,
						this.bean.getOrganizationActivationListboxExt(),
						organization);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public void onActivationClearTabsRespons(final ForwardEvent event) {
		Events.postEvent(SffOrPtyCtants.ON_STAFF_CLEAN,
				bean.getStaffActivationListboxExt(), "onClickTab");
		Events.postEvent(OrganizationConstant.ON_CLEAN_ORGANIZATION,
				bean.getOrganizationActivationListboxExt(), "onClickTab");
	}
	
	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getStaffActivationListboxExt().setPagePosition(
				"activationPage");
		this.bean.getOrganizationActivationListboxExt().setPagePosition(
				"activationPage");
	}
}
