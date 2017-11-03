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
import cn.ffcs.uom.rolePermission.action.bean.RbacResourceTreeMainBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;
import cn.ffcs.uom.rolePermission.model.RbacResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacResourceTreeMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private RbacResourceTreeMainBean bean = new RbacResourceTreeMainBean();

	/**
	 * 选中的资源
	 */
	private RbacResource rbacResource;

	/**
	 * 选中的资源关系
	 */
	private RbacResourceRelation rbacResourceRelation;

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
		bean.getRbacResourceRelationTreeExt().setPortletInfoProvider(this);
		bean.getRbacResourceEditExt().setPortletInfoProvider(this);
		bean.getRbacPermissionResourceListboxExt().setPortletInfoProvider(this);
		bean.getRbacBusinessSystemResourceListboxExt().setPortletInfoProvider(
				this);

		/**
		 * 选择资源树上的资源
		 */
		this.bean.getRbacResourceRelationTreeExt().addForward(
				RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_TREE_REQUEST,
				this.self,
				RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_TREE_RESPONSE);

		/**
		 * 删除节点成功事件
		 */
		this.bean.getRbacResourceRelationTreeExt().addForward(
				RolePermissionConstants.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 资源信息保存事件
		 */
		this.bean.getRbacResourceEditExt().addForward(
				RolePermissionConstants.ON_SAVE_RBAC_RESOURCE_INFO, this.self,
				"onSaveRbacResourceInfoResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$rbacResourceTreeMainWindow() throws Exception {
		initPage();
	}

	/**
	 * 资源信息保存
	 */
	public void onSaveRbacResourceInfoResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			RbacResource rbacResource = (RbacResource) event.getOrigin()
					.getData();
			if (rbacResource != null) {
				/**
				 * 资源信息保存可能对资源名称进行了修改
				 */
				Events.postEvent(
						RolePermissionConstants.ON_SAVE_RBAC_RESOURCE_INFO,
						this.bean.getRbacResourceRelationTreeExt(),
						rbacResource);
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

		RbacResourceRelation rbacResourceRelation = this.bean
				.getRbacResourceRelationTreeExt()
				.getSelectRbacResourceRelation();

		if (rbacResourceRelation != null
				&& rbacResourceRelation.getRbacResourceRelaId() != null) {
			rbacResource = rbacResourceRelation.getRbacResource();
		} else {
			rbacResource = null;
		}

		this.callTab();

	}

	/**
	 * 选择资源树
	 */
	public void onSelectRbacResourceTreeResponse(ForwardEvent event)
			throws Exception {

		RbacResourceRelation rbacResourceRelation = (RbacResourceRelation) event
				.getOrigin().getData();

		if (rbacResourceRelation != null
				&& rbacResourceRelation.getRbacResourceRelaId() != null) {
			this.rbacResourceRelation = rbacResourceRelation;
			rbacResource = rbacResourceRelation.getRbacResource();
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

		if (rbacResource != null) {

			if ("rbacResourceTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_RESOURCE,
						this.bean.getRbacResourceEditExt(), rbacResource);
			}

			if ("rbacPermissionResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacPermissionResource rbacPermissionResource = new RbacPermissionResource();
				rbacPermissionResource.setRbacResourceId(rbacResourceRelation
						.getRbacResourceId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY,
						this.bean.getRbacPermissionResourceListboxExt(),
						rbacPermissionResource);
			}

			if ("rbacBusinessSystemResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacBusinessSystemResource rbacBusinessSystemResource = new RbacBusinessSystemResource();
				rbacBusinessSystemResource
						.setRbacResourceId(rbacResourceRelation
								.getRbacResourceId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY,
						this.bean.getRbacBusinessSystemResourceListboxExt(),
						rbacBusinessSystemResource);
			}

		} else {

			if ("rbacResourceTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_RESOURCE,
						this.bean.getRbacResourceEditExt(), null);
			}

			if ("rbacPermissionResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY,
						this.bean.getRbacPermissionResourceListboxExt(), null);

			}

			if ("rbacBusinessSystemResourceTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_BUSINESS_SYSTEM_RESOURCE_QUERY,
						this.bean.getRbacBusinessSystemResourceListboxExt(),
						null);

			}

		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getRbacResourceRelationTreeExt().setPagePosition(
				"rbacResourceTreePage");
		this.bean.getRbacResourceEditExt().setPagePosition(
				"rbacResourceTreePage");
		this.bean.getRbacPermissionResourceListboxExt().setPagePosition(
				"rbacResourceTreePage");
		this.bean.getRbacBusinessSystemResourceListboxExt().setPagePosition(
				"rbacResourceTreePage");
	}

}
