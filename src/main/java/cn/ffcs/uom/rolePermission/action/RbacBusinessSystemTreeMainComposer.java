package cn.ffcs.uom.rolePermission.action;

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
import cn.ffcs.uom.rolePermission.action.bean.RbacBusinessSystemTreeMainBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

@Controller
@Scope("prototype")
public class RbacBusinessSystemTreeMainComposer extends BasePortletComposer
		implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private RbacBusinessSystemTreeMainBean bean = new RbacBusinessSystemTreeMainBean();

	/**
	 * 选中的业务系统
	 */
	private RbacBusinessSystem rbacBusinessSystem;

	/**
	 * 选中的业务系统关系
	 */
	private RbacBusinessSystemRelation rbacBusinessSystemRelation;

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
		bean.getRbacBusinessSystemRelationTreeExt()
				.setPortletInfoProvider(this);
		bean.getRbacBusinessSystemEditExt().setPortletInfoProvider(this);
		bean.getRbacBusinessSystemResourceListboxExt().setPortletInfoProvider(
				this);
		bean.getRbacRoleBusinessSystemListboxExt().setPortletInfoProvider(this);
		bean.getRbacUserRoleBusinessSystemListboxExt().setPortletInfoProvider(
				this);

		/**
		 * 选择业务系统树上的业务系统
		 */
		this.bean
				.getRbacBusinessSystemRelationTreeExt()
				.addForward(
						RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_TREE_REQUEST,
						this.self,
						RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_TREE_RESPONSE);

		/**
		 * 删除节点成功事件
		 */
		this.bean.getRbacBusinessSystemRelationTreeExt().addForward(
				RolePermissionConstants.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 业务系统信息保存事件
		 */
		this.bean.getRbacBusinessSystemEditExt().addForward(
				RolePermissionConstants.ON_SAVE_RBAC_BUSINESS_SYSTEM_INFO,
				this.self, "onSaveRbacBusinessSystemInfoResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$rbacBusinessSystemTreeMainWindow() throws Exception {
		initPage();
	}

	/**
	 * 业务系统信息保存
	 */
	public void onSaveRbacBusinessSystemInfoResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			RbacBusinessSystem rbacBusinessSystem = (RbacBusinessSystem) event
					.getOrigin().getData();
			if (rbacBusinessSystem != null) {
				/**
				 * 业务系统信息保存可能对业务系统名称进行了修改
				 */
				Events.postEvent(
						RolePermissionConstants.ON_SAVE_RBAC_BUSINESS_SYSTEM_INFO,
						this.bean.getRbacBusinessSystemRelationTreeExt(),
						rbacBusinessSystem);
			}
		}
	}

	/**
	 * 删除节点事件,属性tab清空
	 */
	public void onDelNodeResponse() throws Exception {
		this.callLeftTab();
	}

	public void callLeftTab() {

		RbacBusinessSystemRelation rbacBusinessSystemRelation = this.bean
				.getRbacBusinessSystemRelationTreeExt()
				.getSelectRbacBusinessSystemRelation();

		if (rbacBusinessSystemRelation != null
				&& rbacBusinessSystemRelation.getRbacBusinessSystemRelaId() != null) {
			rbacBusinessSystem = rbacBusinessSystemRelation
					.getRbacBusinessSystem();
		} else {
			rbacBusinessSystem = null;
		}

		this.callTab();

	}

	/**
	 * 选择业务系统树
	 */
	public void onSelectRbacBusinessSystemTreeResponse(ForwardEvent event)
			throws Exception {

		RbacBusinessSystemRelation rbacBusinessSystemRelation = (RbacBusinessSystemRelation) event
				.getOrigin().getData();

		if (rbacBusinessSystemRelation != null
				&& rbacBusinessSystemRelation.getRbacBusinessSystemRelaId() != null) {
			this.rbacBusinessSystemRelation = rbacBusinessSystemRelation;
			rbacBusinessSystem = rbacBusinessSystemRelation
					.getRbacBusinessSystem();
			callTab();
		}

	}

	/**
	 * 点击tab
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				this.bean.setRightSelectTab(clickTab);
				callTab();
			}
		}
	}

	public void callTab() {

		if (this.bean.getRightSelectTab() == null) {
			bean.setRightSelectTab(this.bean.getRightTabbox().getSelectedTab());
		}

		if (rbacBusinessSystem != null) {

			if ("rbacBusinessSystemTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_BUSINESS_SYSTEM,
						this.bean.getRbacBusinessSystemEditExt(),
						rbacBusinessSystem);
			}

			if ("rbacBusinessSystemResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacBusinessSystemResource rbacBusinessSystemResource = new RbacBusinessSystemResource();
				rbacBusinessSystemResource
						.setRbacBusinessSystemId(rbacBusinessSystemRelation
								.getRbacBusinessSystemId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY,
						this.bean.getRbacBusinessSystemResourceListboxExt(),
						rbacBusinessSystemResource);
			}

			if ("rbacRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacRoleBusinessSystem rbacRoleBusinessSystem = new RbacRoleBusinessSystem();
				rbacRoleBusinessSystem
						.setRbacBusinessSystemId(rbacBusinessSystemRelation
								.getRbacBusinessSystemId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacRoleBusinessSystemListboxExt(),
						rbacRoleBusinessSystem);
			}

			if ("rbacUserRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem = new RbacUserRoleBusinessSystem();
				rbacUserRoleBusinessSystem
						.setRbacBusinessSystemId(rbacBusinessSystemRelation
								.getRbacBusinessSystemId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacUserRoleBusinessSystemListboxExt(),
						rbacUserRoleBusinessSystem);
			}

		} else {

			if ("rbacBusinessSystemTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_BUSINESS_SYSTEM,
						this.bean.getRbacBusinessSystemEditExt(), null);
			}

			if ("rbacBusinessSystemResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY,
						this.bean.getRbacBusinessSystemResourceListboxExt(),
						null);

			}

			if ("rbacRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacRoleBusinessSystemListboxExt(), null);

			}

			if ("rbacUserRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacUserRoleBusinessSystemListboxExt(),
						null);

			}

		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getRbacBusinessSystemRelationTreeExt().setPagePosition(
				"rbacBusinessSystemTreePage");
		this.bean.getRbacBusinessSystemEditExt().setPagePosition(
				"rbacBusinessSystemTreePage");
		this.bean.getRbacBusinessSystemResourceListboxExt().setPagePosition(
				"rbacBusinessSystemTreePage");
		this.bean.getRbacRoleBusinessSystemListboxExt().setPagePosition(
				"rbacBusinessSystemTreePage");
		this.bean.getRbacUserRoleBusinessSystemListboxExt().setPagePosition(
				"rbacBusinessSystemTreePage");
	}

}
