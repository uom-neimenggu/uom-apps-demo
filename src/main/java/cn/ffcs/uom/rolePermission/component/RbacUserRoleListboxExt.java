package cn.ffcs.uom.rolePermission.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.rolePermission.component.bean.RbacUserRoleListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

public class RbacUserRoleListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacUserRoleListboxExtBean bean = new RbacUserRoleListboxExtBean();

	/**
	 * 角色树上选中的角色关系
	 */
	@Getter
	@Setter
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的关系
	 */
	private RbacUserRole rbacUserRole;

	/**
	 * 查询queryRbacUserRole.
	 */
	private RbacUserRole queryRbacUserRole;

	/**
	 * 员工角色
	 */
	private RbacUserRoleManager rbacUserRoleManager = (RbacUserRoleManager) ApplicationContextUtil
			.getBean("rbacUserRoleManager");
	
	/**
     * 日志服务队列
     */
    private LogService logService = (LogService) ApplicationContextUtil.getBean("logService");
	
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacUserRoleListboxExt() {
		Executions.createComponents(
				"/pages/rolePermission/comp/rbac_user_role_listbox_ext.zul",
				this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(RolePermissionConstants.ON_RBAC_USER_ROLE_QUERY, this,
				RolePermissionConstants.ON_RBAC_USER_ROLE_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询角色员工列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacUserRoleQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getStaffAccount().setValue("");
		this.bean.getStaffName().setValue("");

		this.rbacRoleRelation = (RbacRoleRelation) event.getOrigin().getData();
		this.onQueryRbacUserRole();
		this.setButtonValid(true, false);
	}

	/**
	 * 员工角色选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacUserRoleSelectRequest() throws Exception {
		if (bean.getRbacUserRoleListbox().getSelectedCount() > 0) {
			rbacUserRole = (RbacUserRole) bean.getRbacUserRoleListbox()
					.getSelectedItem().getValue();
			this.setButtonValid(true, true);
			Events.postEvent(
					RolePermissionConstants.ON_SELECT_RBAC_USER_ROLE_REQUEST,
					this, rbacUserRole);
		}
	}

	/**
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacUserRoleListPaging() throws Exception {
		this.queryRbacUserRole();
	}

	/**
	 * 新增角色员工关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacUserRoleAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacRoleRelation", rbacRoleRelation);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_user_role_edit.zul", this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacUserRole();
				}
			}
		});
	}

	/**
	 * 删除员工角色关系
	 */
	public void onRbacUserRoleDel() {
		if (this.rbacUserRole != null
				&& rbacUserRole.getRbacUserRoleId() != null) {

			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
					    /**
				         * 开始日志添加操作
				         * 添加日志到队列需要：
				         * 业务开始时间，日志消息类型，错误编码和描述
				         */
				        SysLog log = new SysLog();
				        log.startLog(new Date(), SysLogConstrants.ROLE);
						rbacUserRoleManager.removeRbacUserRole(rbacUserRole);
						Class clazz[] = {RbacRoleRelation.class};
					    log.endLog(logService, clazz, SysLogConstrants.DEL, SysLogConstrants.INFO, "删除员工角色关系记录日志");
						
						PubUtil.reDisplayListbox(bean.getRbacUserRoleListbox(),
								rbacUserRole, "del");

						rbacUserRoleManager
								.removeRbacUserRoleToRaptornuke(rbacUserRole);

						rbacUserRole = null;
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
	 * 设置员工角色按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacUserRoleButton().setDisabled(!canAdd);
		this.bean.getDelRbacUserRoleButton().setDisabled(!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacUserRole() {
		this.bean.getRbacUserRoleListPaging().setActivePage(0);
		this.queryRbacUserRole();
	}

	/**
	 * 查询员工角色.
	 */
	private void queryRbacUserRole() {
		if (this.rbacRoleRelation != null) {
			queryRbacUserRole = new RbacUserRole();
			queryRbacUserRole.setRbacRoleId(rbacRoleRelation.getRbacRoleId());
			queryRbacUserRole.setRbacRoleCode(this.bean.getRbacRoleCode()
					.getValue());
			queryRbacUserRole.setRbacRoleName(this.bean.getRbacRoleName()
					.getValue());
			queryRbacUserRole.setStaffAccount(this.bean.getStaffAccount()
					.getValue());
			queryRbacUserRole.setStaffName(this.bean.getStaffName().getValue());

			PageInfo pageInfo = rbacUserRoleManager.queryPageInfoRbacUserRole(
					queryRbacUserRole, this.bean.getRbacUserRoleListPaging()
							.getActivePage() + 1, this.bean
							.getRbacUserRoleListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacUserRoleListbox().setModel(dataList);
			this.bean.getRbacUserRoleListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacUserRoleListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacUserRole() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

		this.bean.getStaffAccount().setValue("");

		this.bean.getStaffName().setValue("");

	}

	/**
	 * 关闭窗口
	 */
	public void onCloseRbacUserRole() {
		Events.postEvent(RolePermissionConstants.ON_CLOSE_RBAC_USER_ROLE, this,
				null);

	}

	/**
	 * 清除角色
	 */
	public void onCleanRbacUserRole() {
		this.bean.getRbacUserRoleListbox().clearSelection();
		Events.postEvent(RolePermissionConstants.ON_CLEAN_RBAC_USER_ROLE, this,
				null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setRbacUserRoleOptDivVisible(boolean visible) {
		bean.getRbacUserRoleOptDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setRbacUserRoleBandboxDivVisible(boolean visible) {
		bean.getRbacUserRoleBandboxDiv().setVisible(visible);
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
					ActionKeys.RBAC_USER_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_USER_ROLE_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacUserRoleButton().setVisible(canAdd);
		this.bean.getDelRbacUserRoleButton().setVisible(canDelete);
	}

}
