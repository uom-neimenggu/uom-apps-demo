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
import cn.ffcs.uom.rolePermission.component.bean.RbacPermissionListboxBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Controller
@Scope("prototype")
public class RbacPermissionListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacPermissionListboxBean bean = new RbacPermissionListboxBean();

	@Autowired
	@Qualifier("rbacPermissionManager")
	private RbacPermissionManager rbacPermissionManager = (RbacPermissionManager) ApplicationContextUtil
			.getBean("rbacPermissionManager");

	@Autowired
	@Qualifier("rbacPermissionRelationManager")
	private RbacPermissionRelationManager rbacPermissionRelationManager = (RbacPermissionRelationManager) ApplicationContextUtil
			.getBean("rbacPermissionRelationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_listbox_ext.zul";

	/**
	 * rbacPermission.
	 */
	private RbacPermission rbacPermission;

	/**
	 * 查询rbacPermission.
	 */
	private RbacPermission queryRbacPermission;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 */
	private String opType;

	public RbacPermissionListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setRbacPermissionButtonValid(true, false, false, false);
	}

	public void onQueryRbacPermissionResponse(final ForwardEvent event)
			throws Exception {
		queryRbacPermission = (RbacPermission) event.getOrigin().getData();
		this.onQueryRbacPermission();
		if (this.bean.getRbacPermissionListbox().getItemCount() == 0) {
			this.setRbacPermissionButtonValid(true, false, false, false);
		}
	}

	/**
	 * 权限选择.
	 */
	public void onRbacPermissionSelectRequest() throws Exception {
		if (this.bean.getRbacPermissionListbox().getSelectedCount() > 0) {
			rbacPermission = (RbacPermission) bean.getRbacPermissionListbox()
					.getSelectedItem().getValue();
			this.setRbacPermissionButtonValid(true, true, true, true);
			Events.postEvent(
					RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_REQUEST,
					this, rbacPermission);
		}

	}

	/**
	 * 新增权限.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacPermissionEditWin("add");
	}

	/**
	 * 查看权限.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionView() throws Exception {
		this.openRbacPermissionEditWin("view");
	}

	/**
	 * 修改权限 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacPermissionEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openRbacPermissionEditWin("mod");
	}

	/**
	 * 删除权限.
	 */
	public void onRbacPermissionDel() throws Exception {

		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (rbacPermission == null
							|| rbacPermission.getRbacPermissionId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {

						RbacPermissionRelation rbacPermissionRelation = new RbacPermissionRelation();
						rbacPermissionRelation
								.setRbacParentPermissionId(rbacPermission
										.getRbacPermissionId());

						List<RbacPermissionRelation> rbacPermissionRelationList = rbacPermissionRelationManager
								.queryRbacPermissionRelationList(rbacPermissionRelation);

						if (rbacPermissionRelationList != null
								&& rbacPermissionRelationList.size() > 0) {
							ZkUtil.showError("你选择的权限存在子权限，请先删除该子权限", "提示信息");
							return;
						}
						rbacPermissionManager
								.removeRbacPermission(rbacPermission);
						PubUtil.reDisplayListbox(
								bean.getRbacPermissionListbox(),
								rbacPermission, "del");
					}
				}
			}
		});

	}

	/**
	 * 打开权限编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacPermissionEditWin(String operationType)
			throws Exception {
		Map arg = new HashMap();
		this.opType = operationType;
		arg.put("opType", opType);
		if (opType.equals("mod") || opType.equals("view")) {
			arg.put("rbacPermission", rbacPermission);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_permission_edit.zul", this, arg);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			public void onEvent(Event event) throws Exception {
				setRbacPermissionButtonValid(true, false, false, false);
				if ("add".equals(opType)) {
					PubUtil.reDisplayListbox(bean.getRbacPermissionListbox(),
							(RbacPermission) event.getData(), "add");
				} else if ("mod".equals(opType)) {
					queryRbacPermission(queryRbacPermission);
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
	private void setRbacPermissionButtonValid(final Boolean canAdd,
			final Boolean canView, final Boolean canEdit,
			final Boolean canDelete) {
		this.bean.getAddRbacPermissionButton().setDisabled(!canAdd);
		this.bean.getViewRbacPermissionButton().setDisabled(!canView);
		this.bean.getEditRbacPermissionButton().setDisabled(!canEdit);
		this.bean.getDelRbacPermissionButton().setDisabled(!canDelete);
	}

	/**
	 * 按条件查询
	 */
	public void onQueryRbacPermission() throws Exception {

		queryRbacPermission = new RbacPermission();

		if (this.bean.getRbacPermissionCode() != null
				&& !StrUtil.isEmpty(this.bean.getRbacPermissionCode()
						.getValue())) {
			queryRbacPermission.setRbacPermissionCode(this.bean
					.getRbacPermissionCode().getValue());
		}

		if (this.bean.getRbacPermissionName() != null
				&& !StrUtil.isEmpty(this.bean.getRbacPermissionName()
						.getValue())) {
			queryRbacPermission.setRbacPermissionName(this.bean
					.getRbacPermissionName().getValue());
		}

		this.bean.getRbacPermissionListboxPaging().setActivePage(0);
		this.queryRbacPermission(queryRbacPermission);
	}

	/**
	 * 分页.
	 */
	public void onRbacPermissionListboxPaging() throws Exception {
		this.queryRbacPermission(queryRbacPermission);
		this.setRbacPermissionButtonValid(true, false, false, false);
	}

	/**
	 * 查询权限.
	 */
	private void queryRbacPermission(RbacPermission rbacPermission)
			throws Exception {
		PageInfo pageInfo = this.rbacPermissionManager
				.queryPageInfoRbacPermission(queryRbacPermission, this.bean
						.getRbacPermissionListboxPaging().getActivePage() + 1,
						this.bean.getRbacPermissionListboxPaging()
								.getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getRbacPermissionListbox().setModel(dataList);
		this.bean.getRbacPermissionListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 重置
	 */
	public void onResetRbacPermission() {
		this.bean.getRbacPermissionCode().setValue(null);
		this.bean.getRbacPermissionName().setValue(null);
	}

	/**
	 * 关闭窗口
	 */
	public void onCloseRbacPermission() {
		Events.postEvent(RolePermissionConstants.ON_CLOSE_RBAC_PERMISSION,
				this, null);

	}

	/**
	 * 清除权限
	 */
	public void onCleanRbacPermission() {
		this.bean.getRbacPermissionListbox().clearSelection();
		Events.postEvent(RolePermissionConstants.ON_CLEAN_RBAC_PERMISSION,
				this, null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setRbacPermissionOptDivVisible(boolean visible) {
		bean.getRbacPermissionOptDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setRbacPermissionBandboxDivVisible(boolean visible) {
		bean.getRbacPermissionBandboxDiv().setVisible(visible);
	}
}
