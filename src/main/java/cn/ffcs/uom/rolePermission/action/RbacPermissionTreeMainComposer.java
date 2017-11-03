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
import cn.ffcs.uom.rolePermission.action.bean.RbacPermissionTreeMainBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;

@Controller
@Scope("prototype")
public class RbacPermissionTreeMainComposer extends BasePortletComposer
		implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private RbacPermissionTreeMainBean bean = new RbacPermissionTreeMainBean();

	/**
	 * 选中的权限
	 */
	private RbacPermission rbacPermission;

	/**
	 * 选中的权限关系
	 */
	private RbacPermissionRelation rbacPermissionRelation;

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
		bean.getRbacPermissionRelationTreeExt().setPortletInfoProvider(this);
		bean.getRbacPermissionEditExt().setPortletInfoProvider(this);
		bean.getRbacPermissionRoleListboxExt().setPortletInfoProvider(this);
		bean.getRbacPermissionResourceListboxExt().setPortletInfoProvider(this);

		/**
		 * 选择权限树上的权限
		 */
		this.bean
				.getRbacPermissionRelationTreeExt()
				.addForward(
						RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_TREE_REQUEST,
						this.self,
						RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_TREE_RESPONSE);

		/**
		 * 删除节点成功事件
		 */
		this.bean.getRbacPermissionRelationTreeExt().addForward(
				RolePermissionConstants.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 权限信息保存事件
		 */
		this.bean.getRbacPermissionEditExt().addForward(
				RolePermissionConstants.ON_SAVE_RBAC_PERMISSION_INFO,
				this.self, "onSaveRbacPermissionInfoResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$rbacPermissionTreeMainWindow() throws Exception {
		initPage();
	}

	/**
	 * 权限信息保存
	 */
	public void onSaveRbacPermissionInfoResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			RbacPermission rbacPermission = (RbacPermission) event.getOrigin()
					.getData();
			if (rbacPermission != null) {
				/**
				 * 权限信息保存可能对权限名称进行了修改
				 */
				Events.postEvent(
						RolePermissionConstants.ON_SAVE_RBAC_PERMISSION_INFO,
						this.bean.getRbacPermissionRelationTreeExt(),
						rbacPermission);
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

		RbacPermissionRelation rbacPermissionRelation = this.bean
				.getRbacPermissionRelationTreeExt()
				.getSelectRbacPermissionRelation();

		if (rbacPermissionRelation != null
				&& rbacPermissionRelation.getRbacPermissionRelaId() != null) {
			rbacPermission = rbacPermissionRelation.getRbacPermission();
		} else {
			rbacPermission = null;
		}

		this.callTab();

	}

	/**
	 * 选择权限树
	 */
	public void onSelectRbacPermissionTreeResponse(ForwardEvent event)
			throws Exception {

		RbacPermissionRelation rbacPermissionRelation = (RbacPermissionRelation) event
				.getOrigin().getData();

		if (rbacPermissionRelation != null
				&& rbacPermissionRelation.getRbacPermissionRelaId() != null) {
			this.rbacPermissionRelation = rbacPermissionRelation;
			rbacPermission = rbacPermissionRelation.getRbacPermission();
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

		if (rbacPermission != null) {

			if ("rbacPermissionTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_PERMISSION,
						this.bean.getRbacPermissionEditExt(),
						rbacPermissionRelation);
			}

			if ("rbacPermissionRoleTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				RbacPermissionRole rbacPermissionRole = new RbacPermissionRole();
				rbacPermissionRole.setRbacPermissionId(rbacPermissionRelation
						.getRbacPermissionId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY,
						this.bean.getRbacPermissionRoleListboxExt(),
						rbacPermissionRole);
			}

			if ("rbacPermissionResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacPermissionResource rbacPermissionResource = new RbacPermissionResource();
				rbacPermissionResource
						.setRbacPermissionRelaId(rbacPermissionRelation
								.getRbacPermissionRelaId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY,
						this.bean.getRbacPermissionResourceListboxExt(),
						rbacPermissionResource);
			}

		} else {

			if ("rbacPermissionTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_PERMISSION,
						this.bean.getRbacPermissionEditExt(), null);
			}

			if ("rbacPermissionRoleTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY,
						this.bean.getRbacPermissionRoleListboxExt(), null);

			}

			if ("rbacPermissionResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY,
						this.bean.getRbacPermissionResourceListboxExt(), null);

			}

		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getRbacPermissionRelationTreeExt().setPagePosition(
				"rbacPermissionTreePage");
		this.bean.getRbacPermissionEditExt().setPagePosition(
				"rbacPermissionTreePage");
		this.bean.getRbacPermissionRoleListboxExt().setPagePosition(
				"rbacPermissionTreePage");
		this.bean.getRbacPermissionResourceListboxExt().setPagePosition(
				"rbacPermissionTreePage");
	}

}
