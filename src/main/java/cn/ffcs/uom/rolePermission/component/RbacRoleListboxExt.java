package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleListboxBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleManager;
import cn.ffcs.uom.rolePermission.manager.RbacRoleRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacRoleListboxBean bean = new RbacRoleListboxBean();

	@Autowired
	@Qualifier("rbacRoleManager")
	private RbacRoleManager rbacRoleManager = (RbacRoleManager) ApplicationContextUtil
			.getBean("rbacRoleManager");

	@Autowired
	@Qualifier("rbacRoleRelationManager")
	private RbacRoleRelationManager rbacRoleRelationManager = (RbacRoleRelationManager) ApplicationContextUtil
			.getBean("rbacRoleRelationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_listbox_ext.zul";

	/**
	 * rbacRole.
	 */
	private RbacRole rbacRole;

	/**
	 * 查询rbacRole.
	 */
	private RbacRole queryRbacRole;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 */
	private String opType;

	public RbacRoleListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setRbacRoleButtonValid(true, false, false, false);
	}

	public void onQueryRbacRoleResponse(final ForwardEvent event)
			throws Exception {
		queryRbacRole = (RbacRole) event.getOrigin().getData();
		this.onQueryRbacRole();
		if (this.bean.getRbacRoleListbox().getItemCount() == 0) {
			this.setRbacRoleButtonValid(true, false, false, false);
		}
	}

	/**
	 * 角色选择.
	 */
	public void onRbacRoleSelectRequest() throws Exception {
		if (this.bean.getRbacRoleListbox().getSelectedCount() > 0) {
			rbacRole = (RbacRole) bean.getRbacRoleListbox().getSelectedItem()
					.getValue();
			this.setRbacRoleButtonValid(true, true, true, true);
			Events.postEvent(
					RolePermissionConstants.ON_SELECT_RBAC_ROLE_REQUEST, this,
					rbacRole);
		}

	}

	/**
	 * 新增角色.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacRoleEditWin("add");
	}

	/**
	 * 查看角色.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleView() throws Exception {
		this.openRbacRoleEditWin("view");
	}

	/**
	 * 修改角色 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacRoleEditWin("mod");
	}

	/**
	 * 删除角色.
	 */
	public void onRbacRoleDel() throws Exception {

		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (rbacRole == null || rbacRole.getRbacRoleId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {

						RbacRoleRelation rbacRoleRelation = new RbacRoleRelation();
						rbacRoleRelation.setRbacParentRoleId(rbacRole
								.getRbacRoleId());

						List<RbacRoleRelation> rbacRoleRelationList = rbacRoleRelationManager
								.queryRbacRoleRelationList(rbacRoleRelation);

						if (rbacRoleRelationList != null
								&& rbacRoleRelationList.size() > 0) {
							ZkUtil.showError("你选择的角色存在子角色，请先删除该子角色", "提示信息");
							return;
						}
						rbacRoleManager.removeRbacRole(rbacRole);
						PubUtil.reDisplayListbox(bean.getRbacRoleListbox(),
								rbacRole, "del");
					}
				}
			}
		});

	}

	/**
	 * 打开角色编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacRoleEditWin(String operationType) throws Exception {
		Map arg = new HashMap();
		this.opType = operationType;
		arg.put("opType", opType);
		if (opType.equals("mod") || opType.equals("view")) {
			arg.put("rbacRole", rbacRole);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_edit.zul", this, arg);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			public void onEvent(Event event) throws Exception {
				setRbacRoleButtonValid(true, false, false, false);
				if ("add".equals(opType)) {
					PubUtil.reDisplayListbox(bean.getRbacRoleListbox(),
							(RbacRole) event.getData(), "add");
				} else if ("mod".equals(opType)) {
					queryRbacRole(queryRbacRole);
				}
			}
		});
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canView
	 *            查看按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setRbacRoleButtonValid(final Boolean canAdd,
			final Boolean canView, final Boolean canEdit,
			final Boolean canDelete) {
		this.bean.getAddRbacRoleButton().setDisabled(!canAdd);
		this.bean.getViewRbacRoleButton().setDisabled(!canView);
		this.bean.getEditRbacRoleButton().setDisabled(!canEdit);
		this.bean.getDelRbacRoleButton().setDisabled(!canDelete);
	}

	/**
	 * 按条件查询
	 */
	public void onQueryRbacRole() throws Exception {

		queryRbacRole = new RbacRole();

		if (this.bean.getRbacRoleCode() != null
				&& !StrUtil.isEmpty(this.bean.getRbacRoleCode().getValue())) {
			queryRbacRole.setRbacRoleCode(this.bean.getRbacRoleCode()
					.getValue());
		}

		if (this.bean.getRbacRoleName() != null
				&& !StrUtil.isEmpty(this.bean.getRbacRoleName().getValue())) {
			queryRbacRole.setRbacRoleName(this.bean.getRbacRoleName()
					.getValue());
		}

		this.bean.getRbacRoleListboxPaging().setActivePage(0);
		this.queryRbacRole(queryRbacRole);
	}

	/**
	 * 分页.
	 */
	public void onRbacRoleListboxPaging() throws Exception {
		this.queryRbacRole(queryRbacRole);
		this.setRbacRoleButtonValid(true, false, false, false);
	}

	/**
	 * 查询角色.
	 */
	private void queryRbacRole(RbacRole rbacRole) throws Exception {
		PageInfo pageInfo = this.rbacRoleManager.queryPageInfoRbacRole(
				queryRbacRole, this.bean.getRbacRoleListboxPaging()
						.getActivePage() + 1, this.bean
						.getRbacRoleListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getRbacRoleListbox().setModel(dataList);
		this.bean.getRbacRoleListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 重置
	 */
	public void onResetRbacRole() {
		this.bean.getRbacRoleCode().setValue(null);
		this.bean.getRbacRoleName().setValue(null);
	}

	/**
	 * 关闭窗口
	 */
	public void onCloseRbacRole() {
		Events.postEvent(RolePermissionConstants.ON_CLOSE_RBAC_ROLE, this, null);

	}

	/**
	 * 清除角色
	 */
	public void onCleanRbacRole() {
		this.bean.getRbacRoleListbox().clearSelection();
		Events.postEvent(RolePermissionConstants.ON_CLEAN_RBAC_ROLE, this, null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setRbacRoleOptDivVisible(boolean visible) {
		bean.getRbacRoleOptDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setRbacRoleBandboxDivVisible(boolean visible) {
		bean.getRbacRoleBandboxDiv().setVisible(visible);
	}
}
