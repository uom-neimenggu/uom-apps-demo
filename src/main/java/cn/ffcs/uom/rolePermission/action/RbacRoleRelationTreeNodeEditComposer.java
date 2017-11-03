package cn.ffcs.uom.rolePermission.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleRelationTreeNodeEditBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleRelationTreeNodeEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = -1014502602479906821L;

	/**
	 * 页面bean
	 */
	private RbacRoleRelationTreeNodeEditBean bean = new RbacRoleRelationTreeNodeEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选择的角色
	 */
	private RbacRole rbacRole;

	private RbacRoleRelationManager rbacRoleRelationManager = (RbacRoleRelationManager) ApplicationContextUtil
			.getBean("rbacRoleRelationManager");
	
	/**
     * 日志服务队列
     */
    @Qualifier("logService")
    @Autowired
    private LogService logService;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacRoleRelationTreeNodeEditWindow() {
		this.bindBean();
	}

	/**
	 * bindBean
	 */
	private void bindBean() {

		opType = (String) arg.get("opType");

		if ("addChildNode".equals(opType)) {

			this.bean.getRbacRoleRelationTreeNodeEditWindow().setTitle("增加子节点");

			rbacRole = (RbacRole) arg.get("rbacRole");

			if (rbacRole == null) {
				ZkUtil.showError("上级节点未选择", "提示信息");
				this.bean.getRbacRoleRelationTreeNodeEditWindow().onClose();
			}

		} else if ("addRootNode".equals(opType)) {

			this.bean.getRbacRoleRelationTreeNodeEditWindow().setTitle("增加根节点");

		} else {

			ZkUtil.showError("未定义操作类型", "提示信息");

			this.bean.getRbacRoleRelationTreeNodeEditWindow().onClose();

			return;

		}
	}

	/**
	 * 确定
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() {

		this.bean.getRbacRoleRelationTreeNodeEditWindow().onClose();
		/**
         * 开始日志添加操作
         * 添加日志到队列需要：
         * 业务开始时间，日志消息类型，错误编码和描述
         */
        SysLog log = new SysLog();
        log.startLog(new Date(), SysLogConstrants.ROLE);
        
		Map map = new HashMap();
		RbacRole newRbacRole = this.bean.getRbacRoleBandboxExt().getRbacRole();

		if (newRbacRole == null) {
			ZkUtil.showError("请选择角色", "提示信息");
			return;
		}

		RbacRoleRelation rbacRoleRelation = new RbacRoleRelation();

		if (opType.equals("addChildNode")) {

			rbacRoleRelation.setRbacRoleId(newRbacRole.getRbacRoleId());
			rbacRoleRelation.setRbacParentRoleId(rbacRole.getRbacRoleId());

			if (rbacRoleRelation.getRbacRoleId() != null
					&& rbacRoleRelation.getRbacRoleId().equals(
							rbacRoleRelation.getRbacParentRoleId())) {
				ZkUtil.showError("该角色的上级不能是本身", "提示信息");
				return;
			}

			RbacRoleRelation queryRbacRoleRelation = new RbacRoleRelation();

			// queryRbacRoleRelation.setRbacRoleId(newRbacRole.getRbacRoleId());

			// List<RbacRoleRelation> queryRbacRoleRelationList =
			// this.rbacRoleRelationManager
			// .queryRbacRoleRelationList(queryRbacRoleRelation);
			//
			// if (queryRbacRoleRelationList != null
			// && queryRbacRoleRelationList.size() > 0) {
			// ZkUtil.showError("该角色已有上级,不能再挂上级角色!", "提示信息");
			// return;
			// }

			queryRbacRoleRelation = rbacRoleRelationManager
					.queryRbacRoleRelation(rbacRoleRelation);

			if (queryRbacRoleRelation != null
					&& queryRbacRoleRelation.getRbacRoleRelaId() != null) {
				ZkUtil.showError("该关系已经存在", "提示信息");
				return;
			}

			List<RbacRoleRelation> subRbacRoleRelationList = this.rbacRoleRelationManager
					.querySubTreeRbacRoleRelationList(rbacRoleRelation);

			if (subRbacRoleRelationList != null) {
				for (RbacRoleRelation subRbacRoleRelation : subRbacRoleRelationList) {
					if (rbacRoleRelation.getRbacParentRoleId().equals(
							subRbacRoleRelation.getRbacRoleId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}

			rbacRoleRelationManager.saveRbacRoleRelation(rbacRoleRelation);
			/**
             * 日志需要具体操作内容
             * 场景编码（这个配置常量）
             * 操作业务对象
             * 操作类型
             */
//            LogTest logTest = new LogTest();
//            logTest.setMsg("添加角色管理子节点");
//            logTest.setOperatorObject("角色新增");
//            logTest.setOperatorType(LogConstants.OPERATOR_ADD);
//            logTestManager.saveLog(logTest.getMsg(), LogConstants.ROLE_SCENCODE, logTest.getOperatorObject(), logTest.getOperatorType());
	        //存放日志进入日志队列
	        try {
	            Class clazz[] ={RbacRoleRelation.class};
	            log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "角色信息添加记录日志");
            } catch (Exception e) {
                e.printStackTrace();
            }

		} else if (opType.equals("addRootNode")) {

			RbacRoleRelation queryRbacRoleRelation = new RbacRoleRelation();
			queryRbacRoleRelation.setRbacRoleId(newRbacRole.getRbacRoleId());

			List<RbacRoleRelation> queryRbacRoleRelationList = this.rbacRoleRelationManager
					.queryRbacRoleRelationList(queryRbacRoleRelation);

			if (queryRbacRoleRelationList != null
					&& queryRbacRoleRelationList.size() > 0) {
				ZkUtil.showError("该角色已有上级,不能设置为角色根节点", "提示信息");
				return;
			}

			rbacRoleRelation.setRbacRoleId(newRbacRole.getRbacRoleId());
			rbacRoleRelation
					.setRbacParentRoleId(RolePermissionConstants.ROOT_ID);

			rbacRoleRelationManager.saveRbacRoleRelation(rbacRoleRelation);
			
	        //存放日志进入日志队列
	        try {
	            Class clazz[] ={RbacRoleRelation.class};
	            log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "角色根节点信息添加记录日志");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}

		map.put("rbacRoleRelation", rbacRoleRelation);
		Events.postEvent("onOK", this.self, map);

	}

	/**
	 * 取消
	 */
	public void onCancel() {
		this.bean.getRbacRoleRelationTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增角色
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onAddRbacRole() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("rbacRole", rbacRole);
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_edit.zul", this.self, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				RbacRole rbacRole = (RbacRole) event.getData();
				if (rbacRole != null) {
					bean.getRbacRoleBandboxExt().setRbacRole(rbacRole);
				}
			}
		});
	}
}
