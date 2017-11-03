package cn.ffcs.uom.rolePermission.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacUserRoleBusinessSystemEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleBusinessSystemManager;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

@Controller
@Scope("prototype")
public class RbacUserRoleBusinessSystemEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacUserRoleBusinessSystemEditBean bean = new RbacUserRoleBusinessSystemEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	private RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem;

	private RbacUserRoleManager rbacUserRoleManager = (RbacUserRoleManager) ApplicationContextUtil
			.getBean("rbacUserRoleManager");

	private RbacUserRoleBusinessSystemManager rbacUserRoleBusinessSystemManager = (RbacUserRoleBusinessSystemManager) ApplicationContextUtil
			.getBean("rbacUserRoleBusinessSystemManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacUserRoleBusinessSystemEditWindow()
			throws Exception {

		this.bindBean();

		this.bean.getRbacBusinessSystemRelationTreeBandboxExt().setReadonly(
				true);
		this.bean.getRbacUserRoleBandboxExt().setReadonly(true);

		if (bean.getRbacBusinessSystemRelationTreeBandboxExt()
				.getRbacBusinessSystemRelationList() != null
				&& bean.getRbacBusinessSystemRelationTreeBandboxExt()
						.getRbacBusinessSystemRelationList().size() > 0) {
			this.bean.getRbacBusinessSystemRelationTreeBandboxExt()
					.setDisabled(true);
		}

		if (bean.getRbacUserRoleBandboxExt().getRbacUserRole() != null
				&& bean.getRbacUserRoleBandboxExt().getRbacUserRole()
						.getRbacUserRoleId() != null) {
			this.bean.getRbacUserRoleBandboxExt().setDisabled(true);
		}

		this.bean.getRbacUserRoleBandboxExt().getRbacUserRoleListboxExt()
				.setRbacRoleRelation(new RbacRoleRelation());

	}

	public void bindBean() {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRbacUserRoleBusinessSystemEditWindow().setTitle(
					"新增员工角色系统关系");
			rbacUserRoleBusinessSystem = (RbacUserRoleBusinessSystem) arg
					.get("rbacUserRoleBusinessSystem");

			if (rbacUserRoleBusinessSystem != null) {

				if (rbacUserRoleBusinessSystem.getRbacBusinessSystemId() != null) {
					List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = new ArrayList<RbacBusinessSystemRelation>();
					RbacBusinessSystemRelation rbacBusinessSystemRelation = new RbacBusinessSystemRelation();
					rbacBusinessSystemRelation
							.setRbacBusinessSystemId(rbacUserRoleBusinessSystem
									.getRbacBusinessSystemId());
					rbacBusinessSystemRelationList
							.add(rbacBusinessSystemRelation);
					this.bean.getRbacBusinessSystemRelationTreeBandboxExt()
							.setRbacBusinessSystemRelationList(
									rbacBusinessSystemRelationList);
				}

				if (rbacUserRoleBusinessSystem.getRbacUserRoleId() != null) {
					RbacUserRole rbacUserRole = new RbacUserRole();
					rbacUserRole.setRbacUserRoleId(rbacUserRoleBusinessSystem
							.getRbacUserRoleId());
					rbacUserRole = rbacUserRoleManager
							.queryRbacUserRole(rbacUserRole);
					this.bean.getRbacUserRoleBandboxExt().setRbacUserRole(
							rbacUserRole);
				}

			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {

			List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = this.bean
					.getRbacBusinessSystemRelationTreeBandboxExt()
					.getRbacBusinessSystemRelationList();

			if (rbacBusinessSystemRelationList == null
					|| rbacBusinessSystemRelationList.size() <= 0) {
				ZkUtil.showError("请选择系统", "提示信息");
				return;
			}

			RbacUserRole rbacUserRole = this.bean.getRbacUserRoleBandboxExt()
					.getRbacUserRole();

			if (rbacUserRole == null
					|| rbacUserRole.getRbacUserRoleId() == null) {
				ZkUtil.showError("请选择员工角色", "提示信息");
				return;
			}

			List<RbacUserRoleBusinessSystem> rbacUserRoleBusinessSystemList = new ArrayList<RbacUserRoleBusinessSystem>();

			for (RbacBusinessSystemRelation rbacBusinessSystemRelation : rbacBusinessSystemRelationList) {

				RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem = new RbacUserRoleBusinessSystem();
				rbacUserRoleBusinessSystem
						.setRbacBusinessSystemId(rbacBusinessSystemRelation
								.getRbacBusinessSystemId());
				rbacUserRoleBusinessSystem.setRbacUserRoleId(rbacUserRole
						.getRbacUserRoleId());

				RbacUserRoleBusinessSystem rbacUserRoleBusinessSystemDb = rbacUserRoleBusinessSystemManager
						.queryRbacUserRoleBusinessSystem(rbacUserRoleBusinessSystem);

				if (rbacUserRoleBusinessSystemDb != null) {
					ZkUtil.showError("系统:"
							+ rbacBusinessSystemRelation
									.getRbacBusinessSystem()
									.getRbacBusinessSystemName() + ",或角色："
							+ rbacUserRole.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacUserRoleBusinessSystemList.add(rbacUserRoleBusinessSystem);
			}

			rbacUserRoleBusinessSystemManager
					.addRbacUserRoleBusinessSystemList(rbacUserRoleBusinessSystemList);

			Events.postEvent("onOK", this.self, rbacUserRoleBusinessSystemList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacUserRoleBusinessSystemEditWindow().onClose();
	}
}
