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
import cn.ffcs.uom.rolePermission.action.bean.RbacPermissionRoleEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacPermissionRoleEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacPermissionRoleEditBean bean = new RbacPermissionRoleEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	private RbacPermissionRole rbacPermissionRole;

	private RbacPermissionRoleManager rbacPermissionRoleManager = (RbacPermissionRoleManager) ApplicationContextUtil
			.getBean("rbacPermissionRoleManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacPermissionRoleEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getRbacPermissionRelationTreeBandboxExt().setReadonly(true);

		if (bean.getRbacRoleRelationTreeBandboxExt().getRbacRoleRelationList() != null
				&& bean.getRbacRoleRelationTreeBandboxExt()
						.getRbacRoleRelationList().size() > 0) {
			this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		}

		if (bean.getRbacPermissionRelationTreeBandboxExt()
				.getRbacPermissionRelationList() != null
				&& bean.getRbacPermissionRelationTreeBandboxExt()
						.getRbacPermissionRelationList().size() > 0) {
			this.bean.getRbacPermissionRelationTreeBandboxExt().setDisabled(
					true);
		}
	}

	public void bindBean() {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRbacPermissionRoleEditWindow().setTitle("新增权限角色关系");
			rbacPermissionRole = (RbacPermissionRole) arg
					.get("rbacPermissionRole");

			if (rbacPermissionRole != null) {

				if (rbacPermissionRole.getRbacRoleId() != null) {
					List<RbacRoleRelation> rbacRoleRelationList = new ArrayList<RbacRoleRelation>();
					RbacRoleRelation rbacRoleRelation = new RbacRoleRelation();
					rbacRoleRelation.setRbacRoleId(rbacPermissionRole
							.getRbacRoleId());
					rbacRoleRelationList.add(rbacRoleRelation);
					this.bean.getRbacRoleRelationTreeBandboxExt()
							.setRbacRoleRelationList(rbacRoleRelationList);
				}

				if (rbacPermissionRole.getRbacPermissionId() != null) {
					List<RbacPermissionRelation> rbacPermissionRelationList = new ArrayList<RbacPermissionRelation>();
					RbacPermissionRelation rbacPermissionRelation = new RbacPermissionRelation();
					rbacPermissionRelation
							.setRbacPermissionId(rbacPermissionRole
									.getRbacPermissionId());
					rbacPermissionRelationList.add(rbacPermissionRelation);
					this.bean.getRbacPermissionRelationTreeBandboxExt()
							.setRbacPermissionRelationList(
									rbacPermissionRelationList);
				}

			}
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		if ("add".equals(opType)) {

			List<RbacRoleRelation> rbacRoleRelationList = this.bean
					.getRbacRoleRelationTreeBandboxExt()
					.getRbacRoleRelationList();

			if (rbacRoleRelationList == null
					|| rbacRoleRelationList.size() <= 0) {
				ZkUtil.showError("请选择角色", "提示信息");
				return;
			}

			List<RbacPermissionRelation> rbacPermissionRelationList = this.bean
					.getRbacPermissionRelationTreeBandboxExt()
					.getRbacPermissionRelationList();

			if (rbacPermissionRelationList == null
					|| rbacPermissionRelationList.size() <= 0) {
				ZkUtil.showError("请选择权限", "提示信息");
				return;
			}

			List<RbacPermissionRole> rbacPermissionRoleList = new ArrayList<RbacPermissionRole>();

			for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

				for (RbacPermissionRelation rbacPermissionRelation : rbacPermissionRelationList) {

					RbacPermissionRole rbacPermissionRole = new RbacPermissionRole();
					rbacPermissionRole.setRbacRoleId(rbacRoleRelation
							.getRbacRoleId());
					rbacPermissionRole
							.setRbacPermissionId(rbacPermissionRelation
									.getRbacPermissionId());

					RbacPermissionRole rbacPermissionRoleDb = rbacPermissionRoleManager
							.queryRbacPermissionRole(rbacPermissionRole);

					if (rbacPermissionRoleDb != null) {
						ZkUtil.showError("角色:"
								+ rbacRoleRelation.getRbacRole()
										.getRbacRoleName()
								+ ",或权限："
								+ rbacPermissionRelation.getRbacPermission()
										.getRbacPermissionName() + ",已存在",
								"提示信息");
						return;
					}

					rbacPermissionRoleList.add(rbacPermissionRole);
				}

			}

			rbacPermissionRoleManager
					.addRbacPermissionRoleList(rbacPermissionRoleList);

			for (RbacPermissionRole rbacPermissionRole : rbacPermissionRoleList) {
				rbacPermissionRoleManager
						.addRbacPermissionRoleToRaptornuke(rbacPermissionRole);
			}

			Events.postEvent("onOK", this.self, rbacPermissionRoleList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacPermissionRoleEditWindow().onClose();
	}
}
