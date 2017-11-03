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
import cn.ffcs.uom.rolePermission.component.bean.RbacRoleBusinessSystemListboxExtBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

public class RbacRoleBusinessSystemListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面bean
	 */
	private RbacRoleBusinessSystemListboxExtBean bean = new RbacRoleBusinessSystemListboxExtBean();

	private RbacRoleBusinessSystem selectRbacRoleBusinessSystem;

	/**
	 * 选中的关系
	 */
	private RbacRoleBusinessSystem rbacRoleBusinessSystem;

	/**
	 * 查询queryRbacRoleBusinessSystem.
	 */
	private RbacRoleBusinessSystem queryRbacRoleBusinessSystem;

	/**
	 * 系统资源
	 */
	private RbacRoleBusinessSystemManager rbacRoleBusinessSystemManager = (RbacRoleBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacRoleBusinessSystemManager");
	
	/**
     * 日志服务队列
     */
    @Qualifier("logService")
    @Autowired
    private LogService logService;
	
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacRoleBusinessSystemListboxExt() {
		Executions
				.createComponents(
						"/pages/rolePermission/comp/rbac_role_business_system_listbox_ext.zul",
						this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addForward(
				RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY,
				this,
				RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY_RESPONSE);
	}

	/**
	 * 初始化
	 */
	public void onCreate() {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询系统资源列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleBusinessSystemQueryResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getRbacRoleCode().setValue("");
		this.bean.getRbacRoleName().setValue("");
		this.bean.getRbacBusinessSystemCode().setValue("");
		this.bean.getRbacBusinessSystemName().setValue("");

		this.selectRbacRoleBusinessSystem = (RbacRoleBusinessSystem) event
				.getOrigin().getData();
		this.onQueryRbacRoleBusinessSystem();
		this.setButtonValid(true, false);
	}

	/**
	 * 系统资源选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onRbacRoleBusinessSystemSelectRequest() throws Exception {
		if (bean.getRbacRoleBusinessSystemListbox().getSelectedCount() > 0) {
			rbacRoleBusinessSystem = (RbacRoleBusinessSystem) bean
					.getRbacRoleBusinessSystemListbox().getSelectedItem()
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
	public void onRbacRoleBusinessSystemListPaging() throws Exception {
		this.queryRbacRoleBusinessSystem();
	}

	/**
	 * 新增系统资源关系
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRbacRoleBusinessSystemAdd() throws Exception {
		Map map = new HashMap();
		map.put("opType", "add");
		map.put("rbacRoleBusinessSystem", selectRbacRoleBusinessSystem);
		Window window = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_business_system_edit.zul",
				this, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					queryRbacRoleBusinessSystem();
				}
			}
		});
	}

	/**
	 * 删除系统资源关系
	 */
	public void onRbacRoleBusinessSystemDel() {
		if (this.rbacRoleBusinessSystem != null
				&& rbacRoleBusinessSystem.getRbacRoleBusinessSystemId() != null) {

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
				        log.startLog(new Date(), SysLogConstrants.PRIM);
						rbacRoleBusinessSystemManager
								.removeRbacRoleBusinessSystem(rbacRoleBusinessSystem);
						
						Class clazz[] = {RbacRoleBusinessSystem.class};
					    log.endLog(logService, clazz, SysLogConstrants.DEL, SysLogConstrants.INFO, "删除角色系统关系记录日志");
						
						PubUtil.reDisplayListbox(
								bean.getRbacRoleBusinessSystemListbox(),
								rbacRoleBusinessSystem, "del");
						rbacRoleBusinessSystem = null;
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
	 * 设置系统资源按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canView
	 *            编辑按钮
	 */
	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		this.bean.getAddRbacRoleBusinessSystemButton().setDisabled(!canAdd);
		this.bean.getDelRbacRoleBusinessSystemButton().setDisabled(!canDelete);
	}

	/**
	 * 查询按钮
	 */
	public void onQueryRbacRoleBusinessSystem() {
		this.bean.getRbacRoleBusinessSystemListPaging().setActivePage(0);
		this.queryRbacRoleBusinessSystem();
	}

	/**
	 * 查询系统资源.
	 */
	private void queryRbacRoleBusinessSystem() {
		if (this.selectRbacRoleBusinessSystem != null) {
			queryRbacRoleBusinessSystem = new RbacRoleBusinessSystem();
			queryRbacRoleBusinessSystem
					.setRbacRoleId(selectRbacRoleBusinessSystem.getRbacRoleId());
			queryRbacRoleBusinessSystem
					.setRbacBusinessSystemId(selectRbacRoleBusinessSystem
							.getRbacBusinessSystemId());
			queryRbacRoleBusinessSystem.setRbacRoleCode(this.bean
					.getRbacRoleCode().getValue());
			queryRbacRoleBusinessSystem.setRbacRoleName(this.bean
					.getRbacRoleName().getValue());
			queryRbacRoleBusinessSystem.setRbacBusinessSystemCode(this.bean
					.getRbacBusinessSystemCode().getValue());
			queryRbacRoleBusinessSystem.setRbacBusinessSystemName(this.bean
					.getRbacBusinessSystemName().getValue());

			PageInfo pageInfo = rbacRoleBusinessSystemManager
					.queryPageInfoRbacRoleBusinessSystem(
							queryRbacRoleBusinessSystem, this.bean
									.getRbacRoleBusinessSystemListPaging()
									.getActivePage() + 1, this.bean
									.getRbacRoleBusinessSystemListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getRbacRoleBusinessSystemListbox().setModel(dataList);
			this.bean.getRbacRoleBusinessSystemListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
			ListboxUtils.clearListbox(bean.getRbacRoleBusinessSystemListbox());
		}
	}

	/**
	 * 重置按钮
	 */
	public void onResetRbacRoleBusinessSystem() {

		this.bean.getRbacRoleCode().setValue("");

		this.bean.getRbacRoleName().setValue("");

		this.bean.getRbacBusinessSystemCode().setValue("");

		this.bean.getRbacBusinessSystemName().setValue("");
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
					ActionKeys.RBAC_ROLE_BUSINESS_SYSTEM_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_BUSINESS_SYSTEM_DEL)) {
				canDelete = true;
			}
		} else if ("rbacBusinessSystemTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_BUSINESS_SYSTEM_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_BUSINESS_SYSTEM_DEL)) {
				canDelete = true;
			}
		}
		this.bean.getAddRbacRoleBusinessSystemButton().setVisible(canAdd);
		this.bean.getDelRbacRoleBusinessSystemButton().setVisible(canDelete);
	}

}
