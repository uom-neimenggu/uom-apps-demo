package cn.ffcs.uom.dataPermission.action;

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
import cn.ffcs.uom.dataPermission.action.bean.RoleMainBean;
import cn.ffcs.uom.dataPermission.constants.RoleConstant;
import cn.ffcs.uom.dataPermission.constants.RoleOrganizationConstant;
import cn.ffcs.uom.dataPermission.constants.RoleProfessionalTreeConstant;
import cn.ffcs.uom.dataPermission.constants.RoleTelcomRegionConstant;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.dataPermission.vo.RoleAdapter;

@Controller
@Scope("prototype")
public class RoleMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	/**
	 * bean.
	 */
	private RoleMainBean bean = new RoleMainBean();
	/**
	 * 选中的组织
	 */
	private RoleAdapter roleAdapter;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		UomZkUtil.autoFitHeight(comp);
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getRoleListbox().setPortletInfoProvider(this);
		this.bean.getRoleTelcomRegionListbox().setPortletInfoProvider(this);
		this.bean.getRoleOrganizationListbox().setPortletInfoProvider(this);
		this.bean.getRoleProfessionalTreeListbox().setPortletInfoProvider(this);
		/**
		 * 选择角色响应
		 */
		this.bean.getRoleListbox().addForward(RoleConstant.ON_SELECT_ROLE,
				comp, "onSelectRoleResponse");
		/**
		 * 角色查询，清理tab
		 */
		this.bean.getRoleListbox().addForward(RoleConstant.ON_ROLE_QUERY, comp,
				"onRoleQueryResponse");

	}

	/**
	 * 角色列表选中角色事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRoleResponse(ForwardEvent event) throws Exception {
		roleAdapter = (RoleAdapter) event.getOrigin().getData();
		callTab();
	}

	/**
	 * 角色列表重新查询事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onRoleQueryResponse(ForwardEvent event) throws Exception {
		this.roleAdapter = null;
		/**
		 * 清空tab数据
		 */
		Events.postEvent(RoleConstant.ON_ROLE_QUERY, bean
				.getRoleTelcomRegionListbox(), null);
		Events.postEvent(RoleConstant.ON_ROLE_QUERY, bean
				.getRoleOrganizationListbox(), null);
		Events.postEvent(RoleConstant.ON_ROLE_QUERY, bean
				.getRoleProfessionalTreeListbox(), null);
	}

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
		 * 未选择角色或者角色为系统角色时情况tab
		 */
		if (roleAdapter == null || roleAdapter.getIsSystemRole()) {
			onRoleQueryResponse(null);
			return;
		}
		/**
		 * 当前页查询数据
		 */
		if (roleAdapter != null && roleAdapter.getRole() != null) {
			if ("roleTelcomRegionTab".equals(this.bean.getSelectTab().getId())) {
				AroleTelcomRegion aroleTelcomRegion = new AroleTelcomRegion();
				aroleTelcomRegion.setAroleId(roleAdapter.getRole().getRoleId());
				Events.postEvent(
						RoleTelcomRegionConstant.ON_ROLE_TELCOM_REGION_QUERY,
						bean.getRoleTelcomRegionListbox(), aroleTelcomRegion);
			}
			if ("roleOrganizationTab".equals(this.bean.getSelectTab().getId())) {
				AroleOrganization aroleOrganization = new AroleOrganization();
				aroleOrganization.setAroleId(roleAdapter.getRole().getRoleId());
				Events.postEvent(
						RoleOrganizationConstant.ON_ROLE_ORGANIZATION_QUERY,
						bean.getRoleOrganizationListbox(), aroleOrganization);
			}
			if ("roleProfessionalTreeTab".equals(this.bean.getSelectTab().getId())) {
				AroleProfessionalTree aroleProfessionalTree = new AroleProfessionalTree();
				aroleProfessionalTree.setAroleId(roleAdapter.getRole().getRoleId());
				Events.postEvent(
						RoleProfessionalTreeConstant.ON_ROLE_PROFESSIONAL_TREE_QUERY,
						bean.getRoleProfessionalTreeListbox(), aroleProfessionalTree);
			}
		}

	}
}
