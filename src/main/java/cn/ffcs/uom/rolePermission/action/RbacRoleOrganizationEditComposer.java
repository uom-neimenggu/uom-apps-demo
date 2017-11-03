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
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleOrganizationEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleOrganizationEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacRoleOrganizationEditBean bean = new RbacRoleOrganizationEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	private RbacRoleOrganizationManager rbacRoleOrganizationManager = (RbacRoleOrganizationManager) ApplicationContextUtil
			.getBean("rbacRoleOrganizationManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacRoleOrganizationEditWindow() throws Exception {
		this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getOrganizationBandboxExt().setReadonly(true);
		this.bindBean();
	}

	public void bindBean() {
		opType = (String) arg.get("opType");
		if ("add".equals(opType)) {
			this.bean.getRbacRoleOrganizationEditWindow().setTitle("新增角色组织关系");
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
		if ("add".equals(opType)) {

			Organization organization = this.bean.getOrganizationBandboxExt()
					.getOrganization();

			if (organization == null || organization.getOrgId() == null) {
				ZkUtil.showError("请选择组织", "提示信息");
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

			List<RbacRoleOrganization> rbacRoleOrganizationList = new ArrayList<RbacRoleOrganization>();

			for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

				RbacRoleOrganization rbacRoleOrganization = new RbacRoleOrganization();
				rbacRoleOrganization.setRbacOrgId(organization.getOrgId());
				rbacRoleOrganization.setRbacRoleId(rbacRoleRelation
						.getRbacRoleId());

				RbacRoleOrganization rbacRoleOrganizationDb = rbacRoleOrganizationManager
						.queryRbacRoleOrganization(rbacRoleOrganization);

				if (rbacRoleOrganizationDb != null) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacRoleOrganizationList.add(rbacRoleOrganization);

			}
			rbacRoleOrganizationManager
					.addRbacRoleOrganizationList(rbacRoleOrganizationList);

			for (RbacRoleOrganization rbacRoleOrganization : rbacRoleOrganizationList) {
				rbacRoleOrganizationManager
						.addRbacRoleOrganizationToRaptornuke(rbacRoleOrganization);
			}

			Events.postEvent("onOK", this.self, rbacRoleOrganizationList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacRoleOrganizationEditWindow().onClose();
	}
}
