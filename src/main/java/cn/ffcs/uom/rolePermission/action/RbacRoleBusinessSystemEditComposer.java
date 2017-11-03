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
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleBusinessSystemEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;

@Controller
@Scope("prototype")
public class RbacRoleBusinessSystemEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacRoleBusinessSystemEditBean bean = new RbacRoleBusinessSystemEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	private RbacRoleBusinessSystem rbacRoleBusinessSystem;

	private RbacRoleBusinessSystemManager rbacRoleBusinessSystemManager = (RbacRoleBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacRoleBusinessSystemManager");
	
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

	public void onCreate$rbacRoleBusinessSystemEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getRbacBusinessSystemRelationTreeBandboxExt().setReadonly(
				true);

		if (bean.getRbacRoleRelationTreeBandboxExt().getRbacRoleRelationList() != null
				&& bean.getRbacRoleRelationTreeBandboxExt()
						.getRbacRoleRelationList().size() > 0) {
			this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		}

		if (bean.getRbacBusinessSystemRelationTreeBandboxExt()
				.getRbacBusinessSystemRelationList() != null
				&& bean.getRbacBusinessSystemRelationTreeBandboxExt()
						.getRbacBusinessSystemRelationList().size() > 0) {
			this.bean.getRbacBusinessSystemRelationTreeBandboxExt()
					.setDisabled(true);
		}

	}

	public void bindBean() {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRbacRoleBusinessSystemEditWindow()
					.setTitle("新增角色系统关系");
			rbacRoleBusinessSystem = (RbacRoleBusinessSystem) arg
					.get("rbacRoleBusinessSystem");

			if (rbacRoleBusinessSystem != null) {

				if (rbacRoleBusinessSystem.getRbacRoleId() != null) {
					List<RbacRoleRelation> rbacRoleRelationList = new ArrayList<RbacRoleRelation>();
					RbacRoleRelation rbacRoleRelation = new RbacRoleRelation();
					rbacRoleRelation.setRbacRoleId(rbacRoleBusinessSystem
							.getRbacRoleId());
					rbacRoleRelationList.add(rbacRoleRelation);
					this.bean.getRbacRoleRelationTreeBandboxExt()
							.setRbacRoleRelationList(rbacRoleRelationList);
				}

				if (rbacRoleBusinessSystem.getRbacBusinessSystemId() != null) {
					List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = new ArrayList<RbacBusinessSystemRelation>();
					RbacBusinessSystemRelation rbacBusinessSystemRelation = new RbacBusinessSystemRelation();
					rbacBusinessSystemRelation
							.setRbacBusinessSystemId(rbacRoleBusinessSystem
									.getRbacBusinessSystemId());
					rbacBusinessSystemRelationList
							.add(rbacBusinessSystemRelation);
					this.bean.getRbacBusinessSystemRelationTreeBandboxExt()
							.setRbacBusinessSystemRelationList(
									rbacBusinessSystemRelationList);
				}

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
        log.startLog(new Date(), SysLogConstrants.PRIM);
		if ("add".equals(opType)) {

			List<RbacRoleRelation> rbacRoleRelationList = this.bean
					.getRbacRoleRelationTreeBandboxExt()
					.getRbacRoleRelationList();

			if (rbacRoleRelationList == null
					|| rbacRoleRelationList.size() <= 0) {
				ZkUtil.showError("请选择角色", "提示信息");
				return;
			}

			List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = this.bean
					.getRbacBusinessSystemRelationTreeBandboxExt()
					.getRbacBusinessSystemRelationList();

			if (rbacBusinessSystemRelationList == null
					|| rbacBusinessSystemRelationList.size() <= 0) {
				ZkUtil.showError("请选择系统", "提示信息");
				return;
			}

			List<RbacRoleBusinessSystem> rbacRoleBusinessSystemList = new ArrayList<RbacRoleBusinessSystem>();

			for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

				for (RbacBusinessSystemRelation rbacBusinessSystemRelation : rbacBusinessSystemRelationList) {

					RbacRoleBusinessSystem rbacRoleBusinessSystem = new RbacRoleBusinessSystem();
					rbacRoleBusinessSystem.setRbacRoleId(rbacRoleRelation
							.getRbacRoleId());
					rbacRoleBusinessSystem
							.setRbacBusinessSystemId(rbacBusinessSystemRelation
									.getRbacBusinessSystemId());

					RbacRoleBusinessSystem rbacRoleBusinessSystemDb = rbacRoleBusinessSystemManager
							.queryRbacRoleBusinessSystem(rbacRoleBusinessSystem);

					if (rbacRoleBusinessSystemDb != null) {
						ZkUtil.showError("角色:"
								+ rbacRoleRelation.getRbacRole()
										.getRbacRoleName()
								+ ",或系统："
								+ rbacBusinessSystemRelation
										.getRbacBusinessSystem()
										.getRbacBusinessSystemName() + ",已存在",
								"提示信息");
						return;
					}

					rbacRoleBusinessSystemList.add(rbacRoleBusinessSystem);
				}

			}

			rbacRoleBusinessSystemManager
					.addRbacRoleBusinessSystemList(rbacRoleBusinessSystemList);
			Class clazz[] = {RbacRoleBusinessSystem.class};
			log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "角色系统关系添加记录日志");
			
			Events.postEvent("onOK", this.self, rbacRoleBusinessSystemList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacRoleBusinessSystemEditWindow().onClose();
	}
}
