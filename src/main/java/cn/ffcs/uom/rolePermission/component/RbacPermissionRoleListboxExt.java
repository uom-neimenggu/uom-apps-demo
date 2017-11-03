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
import cn.ffcs.uom.rolePermission.component.bean.RbacPermissionRoleListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;

public class RbacPermissionRoleListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacPermissionRoleListboxExtBean bean = new RbacPermissionRoleListboxExtBean();

	private RbacPermissionRole selectRbacPermissionRole;

	/**
	 * 选中的关系
	 */
	private RbacPermissionRole rbacPermissionRole;

	/**
	 * 查询queryRbacPermissionRole.
	 */
	private RbacPermissionRole queryRbacPermissionRole;

	/**
	 * 权限角色
	 */
	private RbacPermissionRoleManager rbacPermissionRoleManager = (RbacPermissionRoleManager) ApplicationContextUtil
			.getBean("rbacPermissionRoleManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacPermissionRoleListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_permission_role_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询权限角色列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionRoleQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getRbacPermissionCode().setValue("");
		this.bean.getRbacPermissionName().setValue("");

		this.selectRbacPermissionRole = (RbacPermissionRole) event.getOrigin()
				.getData();
		this.onQueryRbacPermissionRole();
		this.setButtonValid(true, false);
	}

	/**
	 * 权限角色选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionRoleSelectRequest() throws Exception {
		if (bean.getRbacPermissionRoleListbox().getSelectedCount() > 0) {
			rbacPermissionRole = (RbacPermissionRole) bean
					.getRbacPermissionRoleListbox().getSelectedItem()
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
	public void onRbacPermissionRoleListPaging() throws Exception {
		this.queryRbacPermissionRole();
	}

	/**
	 * 新增权限角色关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacPermissionRoleAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacPermissionRole", selectRbacPermissionRole);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_permission_role_edit.zul", this,
				map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacPermissionRole();
				}
			}
		});
	}

	/**
	 * 删除权限角色关系
	 */
	public void onRbacPermissionRoleDel() {
		if (this.rbacPermissionRole != null
				&& rbacPermissionRole.getRbacPermissionRoleId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacPermissionRoleManager
								.removeRbacPermissionRole(rbacPermissionRole);
						PubUtil.reDisplayListbox(
								bean.getRbacPermissionRoleListbox(),
								rbacPermissionRole, "del");
						rbacPermissionRoleManager
								.removeRbacPermissionRoleToRaptornuke(rbacPermissionRole);
						rbacPermissionRole = null;
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
	 * 设置权限角色按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacPermissionRoleButton().setDisabled(!canAdd);
		this.bean.getDelRbacPermissionRoleButton().setDisabled(!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacPermissionRole() {
		this.bean.getRbacPermissionRoleListPaging().setActivePage(0);
		this.queryRbacPermissionRole();
	}

	/**
	 * 查询权限角色.
	 */
	private void queryRbacPermissionRole() {
		if (this.selectRbacPermissionRole != null) {
			queryRbacPermissionRole = new RbacPermissionRole();
			queryRbacPermissionRole.setRbacRoleId(selectRbacPermissionRole
					.getRbacRoleId());
			queryRbacPermissionRole
					.setRbacPermissionId(selectRbacPermissionRole
							.getRbacPermissionId());
			queryRbacPermissionRole.setRbacRoleCode(this.bean.getRbacRoleCode()
					.getValue());
			queryRbacPermissionRole.setRbacRoleName(this.bean.getRbacRoleName()
					.getValue());
			queryRbacPermissionRole.setRbacPermissionCode(this.bean
					.getRbacPermissionCode().getValue());
			queryRbacPermissionRole.setRbacPermissionName(this.bean
					.getRbacPermissionName().getValue());

			PageInfo pageInfo = rbacPermissionRoleManager
					.queryPageInfoRbacPermissionRole(queryRbacPermissionRole,
							this.bean.getRbacPermissionRoleListPaging()
									.getActivePage() + 1, this.bean
									.getRbacPermissionRoleListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacPermissionRoleListbox().setModel(dataList);
			this.bean.getRbacPermissionRoleListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacPermissionRoleListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacPermissionRole() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

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
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_ROLE_DEL)) {
				canDelete = true;
			}
		} else if ("rbacPermissionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_ROLE_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacPermissionRoleButton().setVisible(canAdd);
		this.bean.getDelRbacPermissionRoleButton().setVisible(canDelete);
	}

}
