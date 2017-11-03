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
import cn.ffcs.uom.rolePermission.component.bean.RbacUserRoleBusinessSystemListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

public class RbacUserRoleBusinessSystemListboxExt extends Div implements
		IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacUserRoleBusinessSystemListboxExtBean bean = new RbacUserRoleBusinessSystemListboxExtBean();

	private RbacUserRoleBusinessSystem selectRbacUserRoleBusinessSystem;

	/**
	 * 选中的关系
	 */
	private RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem;

	/**
	 * 查询queryRbacUserRoleBusinessSystem.
	 */
	private RbacUserRoleBusinessSystem queryRbacUserRoleBusinessSystem;

	/**
	 * 用户角色系统
	 */
	private RbacUserRoleBusinessSystemManager rbacUserRoleBusinessSystemManager = (RbacUserRoleBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacUserRoleBusinessSystemManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacUserRoleBusinessSystemListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_user_role_business_system_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询用户角色系统列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacUserRoleBusinessSystemQueryResponse(
			final ForwardEvent event) throws Exception {
		this.bean.getRbacBusinessSystemCode().setValue("");
		this.bean.getRbacBusinessSystemName().setValue("");
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");

		this.selectRbacUserRoleBusinessSystem = (RbacUserRoleBusinessSystem) event
				.getOrigin().getData();
		this.onQueryRbacUserRoleBusinessSystem();
		this.setButtonValid(true, false);
	}

	/**
	 * 用户角色系统选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacUserRoleBusinessSystemSelectRequest() throws Exception {
		if (bean.getRbacUserRoleBusinessSystemListbox().getSelectedCount() > 0) {
			rbacUserRoleBusinessSystem = (RbacUserRoleBusinessSystem) bean
					.getRbacUserRoleBusinessSystemListbox().getSelectedItem()
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
	public void onRbacUserRoleBusinessSystemListPaging() throws Exception {
		this.queryRbacUserRoleBusinessSystem();
	}

	/**
	 * 新增用户角色系统关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacUserRoleBusinessSystemAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacUserRoleBusinessSystem", selectRbacUserRoleBusinessSystem);
		Window window = (Window) Executions
				.createComponents(
						"/pages/rolePermission/rbac_user_role_business_system_edit.zul",
						this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacUserRoleBusinessSystem();
				}
			}
		});
	}

	/**
	 * 删除用户角色系统关系
	 */
	public void onRbacUserRoleBusinessSystemDel() {
		if (this.rbacUserRoleBusinessSystem != null
				&& rbacUserRoleBusinessSystem.getRbacUserRoleBusSysId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacUserRoleBusinessSystemManager
								.removeRbacUserRoleBusinessSystem(rbacUserRoleBusinessSystem);
						PubUtil.reDisplayListbox(
								bean.getRbacUserRoleBusinessSystemListbox(),
								rbacUserRoleBusinessSystem, "del");
						rbacUserRoleBusinessSystem = null;
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
	 * 设置用户角色系统按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacUserRoleBusinessSystemButton().setDisabled(!canAdd);
		this.bean.getDelRbacUserRoleBusinessSystemButton().setDisabled(
				!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacUserRoleBusinessSystem() {
		this.bean.getRbacUserRoleBusinessSystemListPaging().setActivePage(0);
		this.queryRbacUserRoleBusinessSystem();
	}

	/**
	 * 查询用户角色系统.
	 */
	private void queryRbacUserRoleBusinessSystem() {
		if (this.selectRbacUserRoleBusinessSystem != null) {
			queryRbacUserRoleBusinessSystem = new RbacUserRoleBusinessSystem();
			queryRbacUserRoleBusinessSystem
					.setRbacUserRoleId(selectRbacUserRoleBusinessSystem
							.getRbacUserRoleId());
			queryRbacUserRoleBusinessSystem
					.setRbacBusinessSystemId(selectRbacUserRoleBusinessSystem
							.getRbacBusinessSystemId());
			queryRbacUserRoleBusinessSystem.setRbacBusinessSystemCode(this.bean
					.getRbacBusinessSystemCode().getValue());
			queryRbacUserRoleBusinessSystem.setRbacBusinessSystemName(this.bean
					.getRbacBusinessSystemName().getValue());
			queryRbacUserRoleBusinessSystem.setStaffName(this.bean
					.getStaffName().getValue());
			queryRbacUserRoleBusinessSystem.setStaffAccount(this.bean
					.getStaffAccount().getValue());
			queryRbacUserRoleBusinessSystem.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacUserRoleBusinessSystem.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());

			PageInfo pageInfo = rbacUserRoleBusinessSystemManager
					.queryPageInfoRbacUserRoleBusinessSystem(
							queryRbacUserRoleBusinessSystem, this.bean
									.getRbacUserRoleBusinessSystemListPaging()
									.getActivePage() + 1, this.bean
									.getRbacUserRoleBusinessSystemListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacUserRoleBusinessSystemListbox().setModel(dataList);
			this.bean.getRbacUserRoleBusinessSystemListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean
					.getRbacUserRoleBusinessSystemListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacUserRoleBusinessSystem() {

		this.bean.getRbacBusinessSystemCode().setValue("");

		this.bean.getRbacBusinessSystemName().setValue("");

		this.bean.getStaffName().setValue("");

		this.bean.getStaffAccount().setValue("");

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

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
					ActionKeys.RBAC_USER_ROLE_BUSINESS_SYSTEM_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_USER_ROLE_BUSINESS_SYSTEM_DEL)) {
				canDelete = true;
			}
		} else if ("rbacBusinessSystemTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_USER_ROLE_BUSINESS_SYSTEM_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_USER_ROLE_BUSINESS_SYSTEM_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacUserRoleBusinessSystemButton().setVisible(canAdd);
		this.bean.getDelRbacUserRoleBusinessSystemButton()
				.setVisible(canDelete);
	}

}
