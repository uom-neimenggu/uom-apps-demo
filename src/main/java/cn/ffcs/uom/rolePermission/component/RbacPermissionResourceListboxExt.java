package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.rolePermission.component.bean.RbacPermissionResourceListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;

public class RbacPermissionResourceListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacPermissionResourceListboxExtBean bean = new RbacPermissionResourceListboxExtBean();

	private RbacPermissionResource selectRbacPermissionResource;

	/**
	 * 选中的关系
	 */
	private RbacPermissionResource rbacPermissionResource;

	/**
	 * 查询queryRbacPermissionResource.
	 */
	private RbacPermissionResource queryRbacPermissionResource;

	/**
	 * 权限资源
	 */
	private RbacPermissionResourceManager rbacPermissionResourceManager = (RbacPermissionResourceManager) ApplicationContextUtil
			.getBean("rbacPermissionResourceManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacPermissionResourceListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_permission_resource_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_PERMISSION_RESOURCE_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询权限资源列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionResourceQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacResourceCode().setValue("");
		this.bean.getRbacResourceName().setValue("");
		this.bean.getRbacPermissionCode().setValue("");
		this.bean.getRbacPermissionName().setValue("");

		this.selectRbacPermissionResource = (RbacPermissionResource) event
				.getOrigin().getData();
		this.onQueryRbacPermissionResource();
		this.setButtonValid(true, false);
	}

	/**
	 * 权限资源选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionResourceSelectRequest() throws Exception {
		if (bean.getRbacPermissionResourceListbox().getSelectedCount() > 0) {
			rbacPermissionResource = (RbacPermissionResource) bean
					.getRbacPermissionResourceListbox().getSelectedItem()
					.getValue();
			this.setButtonValid(true, true);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionResourceListPaging() throws Exception {
		this.queryRbacPermissionResource();
	}

	/**
	 * 新增权限资源关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacPermissionResourceAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacPermissionResource", selectRbacPermissionResource);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_permission_resource_edit.zul",
				this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacPermissionResource();
				}
			}
		});
	}

	/**
	 * 删除权限资源关系
	 */
	public void onRbacPermissionResourceDel() {
		if (this.rbacPermissionResource != null
				&& rbacPermissionResource.getRbacPermissionResourceId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacPermissionResourceManager
								.removeRbacPermissionResource(rbacPermissionResource);
						PubUtil.reDisplayListbox(
								bean.getRbacPermissionResourceListbox(),
								rbacPermissionResource, "del");
						rbacPermissionResource = null;
						setButtonValid(true, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录", "提示信息");
			return;
		}
	}

	/**
	 * 设置权限资源按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacPermissionResourceButton().setDisabled(!canAdd);
		this.bean.getDelRbacPermissionResourceButton().setDisabled(!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacPermissionResource() {
		this.bean.getRbacPermissionResourceListPaging().setActivePage(0);
		this.queryRbacPermissionResource();
	}

	/**
	 * 查询权限资源.
	 */
	private void queryRbacPermissionResource() {
		if (this.selectRbacPermissionResource != null) {
			queryRbacPermissionResource = new RbacPermissionResource();
			queryRbacPermissionResource
					.setRbacResourceId(selectRbacPermissionResource
							.getRbacResourceId());
			queryRbacPermissionResource
					.setRbacPermissionRelaId(selectRbacPermissionResource
							.getRbacPermissionRelaId());
			queryRbacPermissionResource.setRbacResourceCode(this.bean
					.getRbacResourceCode().getValue());
			queryRbacPermissionResource.setRbacResourceName(this.bean
					.getRbacResourceName().getValue());
			queryRbacPermissionResource.setRbacPermissionCode(this.bean
					.getRbacPermissionCode().getValue());
			queryRbacPermissionResource.setRbacPermissionName(this.bean
					.getRbacPermissionName().getValue());

			PageInfo pageInfo = rbacPermissionResourceManager
					.queryPageInfoRbacPermissionResource(
							queryRbacPermissionResource, this.bean
									.getRbacPermissionResourceListPaging()
									.getActivePage() + 1, this.bean
									.getRbacPermissionResourceListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacPermissionResourceListbox().setModel(dataList);
			this.bean.getRbacPermissionResourceListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacPermissionResourceListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacPermissionResource() {

		this.bean.getRbacResourceCode().setValue("");

		this.bean.getRbacResourceName().setValue("");

		this.bean.getRbacPermissionCode().setValue("");

		this.bean.getRbacPermissionName().setValue("");

	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDelete = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDelete = true;
		} else if ("rbacPermissionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_RESOURCE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_RESOURCE_DEL)) {
				canDelete = true;
			}
		} else if ("rbacResourceTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_RESOURCE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_RESOURCE_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacPermissionResourceButton().setVisible(canAdd);
		this.bean.getDelRbacPermissionResourceButton().setVisible(canDelete);
	}

}
