package cn.ffcs.uom.rolePermission.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacUserRoleEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class RbacUserRoleEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacUserRoleEditBean bean = new RbacUserRoleEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	private RbacUserRoleManager rbacUserRoleManager = (RbacUserRoleManager) ApplicationContextUtil
			.getBean("rbacUserRoleManager");
	
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

	public void onCreate$rbacUserRoleEditWindow() throws Exception {
		this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getStaffBandboxExt().setReadonly(true);
		this.bindBean();
	}

	public void bindBean() {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRbacUserRoleEditWindow().setTitle("新增员工角色关系");
			rbacRoleRelation = (RbacRoleRelation) arg.get("rbacRoleRelation");
			if (rbacRoleRelation != null) {
				List<RbacRoleRelation> rbacRoleRelationList = new ArrayList<RbacRoleRelation>();
				rbacRoleRelationList.add(rbacRoleRelation);
				this.bean.getRbacRoleRelationTreeBandboxExt()
						.setRbacRoleRelationList(rbacRoleRelationList);
			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
	    /**
         * 开始日志添加操作
         * 添加日志到队列需要：
         * 业务开始时间，日志消息类型，错误编码和描述
         */
        SysLog log = new SysLog();
        log.startLog(new Date(), SysLogConstrants.ROLE);
		if ("add".equals(opType)) {

			Staff staff = this.bean.getStaffBandboxExt().getStaff();

			if (staff == null || staff.getStaffId() == null) {
				ZkUtil.showError("请选择员工", "提示信息");
				return;
			}

			List<RbacRoleRelation> rbacRoleRelationList = this.bean
					.getRbacRoleRelationTreeBandboxExt()
					.getRbacRoleRelationList();

			if (rbacRoleRelationList == null
					|| rbacRoleRelationList.size() <= 0) {
				ZkUtil.showError("请选择角色", "提示信息");
				return;
			}

			List<RbacUserRole> rbacUserRoleList = new ArrayList<RbacUserRole>();

			for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

				RbacUserRole rbacUserRole = new RbacUserRole();
				rbacUserRole.setRbacUserId(staff.getStaffId());
				rbacUserRole.setRbacRoleId(rbacRoleRelation.getRbacRoleId());

				RbacUserRole rbacUserRoleDb = rbacUserRoleManager
						.queryRbacUserRole(rbacUserRole);

				if (rbacUserRoleDb != null) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacUserRoleList.add(rbacUserRole);

			}

			rbacUserRoleManager.addRbacUserRoleList(rbacUserRoleList);
			/**
             * 日志需要具体操作内容
             * 场景编码（这个配置常量）
             * 操作业务对象
             * 操作类型
             */
//            LogTest logTest = new LogTest();
//            logTest.setMsg("角色员工关系新增");
//            logTest.setOperatorObject("角色员工关系新增");
//            logTest.setOperatorType(LogConstants.OPERATOR_ADD);
//            logTestManager.saveLog(logTest.getMsg(), LogConstants.USERROLE_SCENCODE, logTest.getOperatorObject(), logTest.getOperatorType());
			for (RbacUserRole rbacUserRole : rbacUserRoleList) {
				rbacUserRoleManager.addRbacUserRoleToRaptornuke(rbacUserRole);
			}
			Class clazz[] = {RbacRoleRelation.class};
			log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "添加员工角色关系记录日志");
			Events.postEvent("onOK", this.self, rbacUserRoleList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacUserRoleEditWindow().onClose();
	}
}
