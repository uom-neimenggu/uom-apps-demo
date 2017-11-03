package cn.ffcs.uom.organization.action;

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
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.action.bean.NewMarketingUnitOrganizationMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;

/**
 * 组织管理.
 * 
 * @author xuxs
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class NewMarketingUnitOrganizationMainComposer extends
		BasePortletComposer implements IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4200151504937637783L;
	/**
	 * bean.
	 */
	private NewMarketingUnitOrganizationMainBean bean = new NewMarketingUnitOrganizationMainBean();
	/**
	 * 选中的组织
	 */
	private Organization organization;

	@Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		// TODO Auto-generated method stub
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		UomZkUtil.autoFitHeight(comp);
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getOrganizationListbox().setPortletInfoProvider(this);
		this.bean.getOrganizationRelationListbox().setPortletInfoProvider(this);
		this.bean.getStaffOrganizationListbox().setPortletInfoProvider(this);
		this.bean.getStaffOrgTranListboxExt().setPortletInfoProvider(this);
		this.bean.getOrganizationPositionExt().setPortletInfoProvider(this);
		this.bean.getOrganizationListbox().addForward(
				OrganizationConstant.ON_SELECT_ORGANIZATION, comp,
				"onSelectOrganizationResponse");
		this.bean.getOrganizationListbox().addForward(
				OrganizationConstant.ON_ORGANIZATION_QUERY, comp,
				"onOrganiztionQueryResponse");
		/**
		 * 组织列表删除组织事件
		 */
		this.bean.getOrganizationListbox().addForward(
				OrganizationConstant.ON_DEL_ORGANIZAITON, comp,
				"onDelOrganizationResponse");
		initPage();

	}

	/**
	 * 组织列表选中事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectOrganizationResponse(ForwardEvent event)
			throws Exception {
		organization = (Organization) event.getOrigin().getData();
		callTab();
	}

	/**
	 * 组织列表查询事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrganiztionQueryResponse(ForwardEvent event) throws Exception {
		this.organization = null;
		/**
		 * 清空tab数据
		 */
		Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY,
				bean.getOrganizationRelationListbox(), null);
		Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY,
				bean.getStaffOrganizationListbox(), null);
		Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY,
				bean.getStaffOrgTranListboxExt(), null);
		Events.postEvent(OrganizationConstant.ON_ORGANIZATION_QUERY,
				bean.getOrganizationPositionExt(), null);
	}

	/**
	 * 点击tab
	 * 
	 * @throws Exception
	 */
	/**
	 * 点击tab
	 * 
	 * @throws Exception
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setSelectTab(clickTab);
				callTab();
			}
		}
	}

	/**
	 * 响应tab事件
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabBox().getSelectedTab());
		}
		/**
		 * 当前页查询数据
		 */
		if (organization != null && organization.getOrgId() != null) {
			if ("organizationRelationTab".equals(this.bean.getSelectTab()
					.getId())) {
				this.bean.getOrganizationRelationListbox()
						.setVariablePagePosition("newMarketingUnitPage");
				OrganizationRelation orgRela = new OrganizationRelation();
				orgRela.setOrgId(organization.getOrgId());
				Events.postEvent(
						OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
						bean.getOrganizationRelationListbox(), orgRela);
			}
			if ("staffOrganizationTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getStaffOrganizationListbox()
						.setVariablePagePosition("newMarketingUnitPage");
				StaffOrganization staffOrganization = new StaffOrganization();
				staffOrganization.setOrgId(organization.getOrgId());
				Events.postEvent(
						StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
						bean.getStaffOrganizationListbox(), staffOrganization);
			}
			if ("staffOrgTranTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getStaffOrgTranListboxExt().setVariablePagePosition(
						"newMarketingUnitPage");
				this.bean.getStaffOrgTranListboxExt().setIsOrgTreePage(true);
				StaffOrganizationTran staffOrganizationTran = new StaffOrganizationTran();
				staffOrganizationTran.setOrgId(organization.getOrgId());
				Events.postEvent(
						OrganizationConstant.ON_STAFF_ORGANIZATION_TRAN_QUERY,
						bean.getStaffOrgTranListboxExt(), staffOrganizationTran);
			}
			if ("organizationPositionTab".equals(this.bean.getSelectTab()
					.getId())) {
				this.bean.getOrganizationPositionExt().setVariablePagePosition(
						"newMarketingUnitPage");
				Events.postEvent(
						OrganizationConstant.ON_ORGANIZATION_POSITION_QUERY,
						bean.getOrganizationPositionExt(), organization);
			}
		}

	}

	/**
	 * 组织列表删除组织响应
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDelOrganizationResponse(ForwardEvent event) throws Exception {
		/**
		 * 组织关系要清空
		 */
		Events.postEvent(OrganizationConstant.ON_DEL_ORGANIZAITON,
				bean.getOrganizationRelationListbox(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getOrganizationListbox().setPagePosition(
				"newMarketingUnitPage");
		this.bean.getOrganizationRelationListbox().setPagePosition(
				"newMarketingUnitPage");
		this.bean.getStaffOrganizationListbox().setPagePosition(
				"newMarketingUnitPage");
		this.bean.getStaffOrgTranListboxExt().setPagePosition(
				"newMarketingUnitPage");
		this.bean.getOrganizationPositionExt().setPagePosition(
				"newMarketingUnitPage");
	}
}
