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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.rolePermission.component.bean.RbacBusinessSystemListboxBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemManager;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

@Controller
@Scope("prototype")
public class RbacBusinessSystemListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private RbacBusinessSystemListboxBean bean = new RbacBusinessSystemListboxBean();

	@Autowired
	@Qualifier("rbacBusinessSystemManager")
	private RbacBusinessSystemManager rbacBusinessSystemManager = (RbacBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemManager");

	@Autowired
	@Qualifier("rbacBusinessSystemRelationManager")
	private RbacBusinessSystemRelationManager rbacBusinessSystemRelationManager = (RbacBusinessSystemRelationManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemRelationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_business_system_listbox_ext.zul";

	/**
	 * rbacBusinessSystem.
	 */
	private RbacBusinessSystem rbacBusinessSystem;

	/**
	 * 查询rbacBusinessSystem.
	 */
	private RbacBusinessSystem queryRbacBusinessSystem;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 */
	private String opType;

	public RbacBusinessSystemListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setRbacBusinessSystemButtonValid(true, false, false, false);
	}

	public void onQueryRbacBusinessSystemResponse(final ForwardEvent event)
			throws Exception {
		queryRbacBusinessSystem = (RbacBusinessSystem) event.getOrigin()
				.getData();
		this.onQueryRbacBusinessSystem();
		if (this.bean.getRbacBusinessSystemListbox().getItemCount() == 0) {
			this.setRbacBusinessSystemButtonValid(true, false, false, false);
		}
	}

	/**
	 * 系统选择.
	 */
	public void onRbacBusinessSystemSelectRequest() throws Exception {
		if (this.bean.getRbacBusinessSystemListbox().getSelectedCount() > 0) {
			rbacBusinessSystem = (RbacBusinessSystem) bean
					.getRbacBusinessSystemListbox().getSelectedItem()
					.getValue();
			this.setRbacBusinessSystemButtonValid(true, true, true, true);
			Events.postEvent(
					RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_REQUEST,
					this, rbacBusinessSystem);
		}

	}

	/**
	 * 新增系统.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemAdd() throws Exception {
		this.openRbacBusinessSystemEditWin("add");
	}

	/**
	 * 查看系统.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemView() throws Exception {
		this.openRbacBusinessSystemEditWin("view");
	}

	/**
	 * 修改系统 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacBusinessSystemEdit() throws Exception {
		this.openRbacBusinessSystemEditWin("mod");
	}

	/**
	 * 删除系统.
	 */
	public void onRbacBusinessSystemDel() throws Exception {

		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (rbacBusinessSystem == null
							|| rbacBusinessSystem.getRbacBusinessSystemId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {

						RbacBusinessSystemRelation rbacBusinessSystemRelation = new RbacBusinessSystemRelation();
						rbacBusinessSystemRelation
								.setRbacParentBusinessSystemId(rbacBusinessSystem
										.getRbacBusinessSystemId());

						List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = rbacBusinessSystemRelationManager
								.queryRbacBusinessSystemRelationList(rbacBusinessSystemRelation);

						if (rbacBusinessSystemRelationList != null
								&& rbacBusinessSystemRelationList.size() > 0) {
							ZkUtil.showError("你选择的系统存在子系统，请先删除该子系统", "提示信息");
							return;
						}
						rbacBusinessSystemManager
								.removeRbacBusinessSystem(rbacBusinessSystem);
						PubUtil.reDisplayListbox(
								bean.getRbacBusinessSystemListbox(),
								rbacBusinessSystem, "del");
					}
				}
			}
		});

	}

	/**
	 * 打开系统编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openRbacBusinessSystemEditWin(String operationType)
			throws Exception {
		Map arg = new HashMap();
		this.opType = operationType;
		arg.put("opType", opType);
		if (opType.equals("mod") || opType.equals("view")) {
			arg.put("rbacBusinessSystem", rbacBusinessSystem);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_business_system_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			public void onEvent(Event event) throws Exception {
				setRbacBusinessSystemButtonValid(true, false, false, false);
				if ("add".equals(opType)) {
					PubUtil.reDisplayListbox(
							bean.getRbacBusinessSystemListbox(),
							(RbacBusinessSystem) event.getData(), "add");
				} else if ("mod".equals(opType)) {
					queryRbacBusinessSystem(queryRbacBusinessSystem);
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
	private void setRbacBusinessSystemButtonValid(final Boolean canAdd,
			final Boolean canView, final Boolean canEdit,
			final Boolean canDelete) {
		this.bean.getAddRbacBusinessSystemButton().setDisabled(!canAdd);
		this.bean.getViewRbacBusinessSystemButton().setDisabled(!canView);
		this.bean.getEditRbacBusinessSystemButton().setDisabled(!canEdit);
		this.bean.getDelRbacBusinessSystemButton().setDisabled(!canDelete);
	}

	/**
	 * 按条件查询
	 */
	public void onQueryRbacBusinessSystem() throws Exception {

		queryRbacBusinessSystem = new RbacBusinessSystem();

		if (this.bean.getRbacBusinessSystemCode() != null
				&& !StrUtil.isEmpty(this.bean.getRbacBusinessSystemCode()
						.getValue())) {
			queryRbacBusinessSystem.setRbacBusinessSystemCode(this.bean
					.getRbacBusinessSystemCode().getValue());
		}

		if (this.bean.getRbacBusinessSystemName() != null
				&& !StrUtil.isEmpty(this.bean.getRbacBusinessSystemName()
						.getValue())) {
			queryRbacBusinessSystem.setRbacBusinessSystemName(this.bean
					.getRbacBusinessSystemName().getValue());
		}

		this.bean.getRbacBusinessSystemListboxPaging().setActivePage(0);
		this.queryRbacBusinessSystem(queryRbacBusinessSystem);
	}

	/**
	 * 分页.
	 */
	public void onRbacBusinessSystemListboxPaging() throws Exception {
		this.queryRbacBusinessSystem(queryRbacBusinessSystem);
		this.setRbacBusinessSystemButtonValid(true, false, false, false);
	}

	/**
	 * 查询系统.
	 */
	private void queryRbacBusinessSystem(RbacBusinessSystem rbacBusinessSystem)
			throws Exception {
		PageInfo pageInfo = this.rbacBusinessSystemManager
				.queryPageInfoRbacBusinessSystem(queryRbacBusinessSystem,
						this.bean.getRbacBusinessSystemListboxPaging()
								.getActivePage() + 1, this.bean
								.getRbacBusinessSystemListboxPaging()
								.getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getRbacBusinessSystemListbox().setModel(dataList);
		this.bean.getRbacBusinessSystemListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 重置
	 */
	public void onResetRbacBusinessSystem() {
		this.bean.getRbacBusinessSystemCode().setValue(null);
		this.bean.getRbacBusinessSystemName().setValue(null);
	}

	/**
	 * 关闭窗口
	 */
	public void onCloseRbacBusinessSystem() {
		Events.postEvent(RolePermissionConstants.ON_CLOSE_RBAC_BUSINESS_SYSTEM,
				this, null);

	}

	/**
	 * 清除系统
	 */
	public void onCleanRbacBusinessSystem() {
		this.bean.getRbacBusinessSystemListbox().clearSelection();
		Events.postEvent(RolePermissionConstants.ON_CLEAN_RBAC_BUSINESS_SYSTEM,
				this, null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setRbacBusinessSystemOptDivVisible(boolean visible) {
		bean.getRbacBusinessSystemOptDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setRbacBusinessSystemBandboxDivVisible(boolean visible) {
		bean.getRbacBusinessSystemBandboxDiv().setVisible(visible);
	}
}
