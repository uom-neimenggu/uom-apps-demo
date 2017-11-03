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
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleOrganizationLevelEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationLevelManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleOrganizationLevelEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacRoleOrganizationLevelEditBean bean = new RbacRoleOrganizationLevelEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的角色组织层级关系
	 */
	private RbacRoleOrganizationLevel rbacRoleOrganizationLevel;

	private RbacRoleOrganizationLevelManager rbacRoleOrganizationLevelManager = (RbacRoleOrganizationLevelManager) ApplicationContextUtil
			.getBean("rbacRoleOrganizationLevelManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacRoleOrganizationLevelEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getOrganizationBandboxExt().setReadonly(true);
		if ("mod".equals(opType)) {
			this.bean.getRelaCd().setDisabled(true);
			this.bean.getOrganizationBandboxExt().setDisabled(true);
		}
	}

	public void bindBean() {

		opType = (String) arg.get("opType");

		rbacRoleRelation = (RbacRoleRelation) arg.get("rbacRoleRelation");
		if (rbacRoleRelation != null) {
			List<RbacRoleRelation> rbacRoleRelationList = new ArrayList<RbacRoleRelation>();
			rbacRoleRelationList.add(rbacRoleRelation);
			this.bean.getRbacRoleRelationTreeBandboxExt()
					.setRbacRoleRelationList(rbacRoleRelationList);
		}

		if ("add".equals(opType)) {

			this.bean.getRbacRoleOrganizationLevelEditWindow().setTitle(
					"新增角色组织层级关系");

		} else if ("mod".equals(opType)) {

			this.bean.getRbacRoleOrganizationLevelEditWindow().setTitle(
					"修改角色组织层级关系");

			rbacRoleOrganizationLevel = (RbacRoleOrganizationLevel) arg
					.get("rbacRoleOrganizationLevel");

			if (rbacRoleOrganizationLevel != null
					&& rbacRoleOrganizationLevel.getRbacOrgId() != null) {

				bean.getRbacRoleOrgLevelId().setValue(
						rbacRoleOrganizationLevel.getRbacRoleOrgLevelId());

				bean.getOrganizationBandboxExt().setOrganization(
						rbacRoleOrganizationLevel.getOrganization());

				List<String> relaCdList = new ArrayList<String>();
				relaCdList.add(rbacRoleOrganizationLevel.getRelaCd());
				bean.getRelaCd().setInitialValue(relaCdList);

				bean.getRbacLowerLevel().setValue(
						rbacRoleOrganizationLevel.getRbacLowerLevel());

				bean.getRbacHigherLevel().setValue(
						rbacRoleOrganizationLevel.getRbacHigherLevel());

			}

		}

	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {

		String relaCd = this.bean.getRelaCd().getAttrValue();
		if (StrUtil.isEmpty(relaCd)) {
			ZkUtil.showError("请关系类型", "提示信息");
			return;
		}

		Organization organization = this.bean.getOrganizationBandboxExt()
				.getOrganization();

		if (organization == null || organization.getOrgId() == null) {
			ZkUtil.showError("请选择组织", "提示信息");
			return;
		}

		List<RbacRoleRelation> rbacRoleRelationList = this.bean
				.getRbacRoleRelationTreeBandboxExt().getRbacRoleRelationList();

		if (rbacRoleRelationList == null || rbacRoleRelationList.size() <= 0) {
			ZkUtil.showError("请选择角色", "提示信息");
			return;
		}

		Long rbacLowerLevel = this.bean.getRbacLowerLevel().getValue();
		if (rbacLowerLevel == null) {
			ZkUtil.showError("最低层级不能为空", "提示信息");
			return;
		}

		Long rbacHigherLevel = this.bean.getRbacHigherLevel().getValue();
		if (rbacHigherLevel == null) {
			ZkUtil.showError("最高层级不能为空", "提示信息");
			return;
		}

		List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList = new ArrayList<RbacRoleOrganizationLevel>();

		for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

			if ("add".equals(opType)) {

				RbacRoleOrganizationLevel newRbacRoleOrganizationLevel = new RbacRoleOrganizationLevel();
				newRbacRoleOrganizationLevel.setRbacOrgId(organization
						.getOrgId());
				newRbacRoleOrganizationLevel.setRbacRoleId(rbacRoleRelation
						.getRbacRoleId());
				newRbacRoleOrganizationLevel.setRelaCd(relaCd);

				RbacRoleOrganizationLevel rbacRoleOrganizationLevelDb = rbacRoleOrganizationLevelManager
						.queryRbacRoleOrganizationLevel(newRbacRoleOrganizationLevel);

				if (rbacRoleOrganizationLevelDb != null) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				newRbacRoleOrganizationLevel.setRbacLowerLevel(rbacLowerLevel);
				newRbacRoleOrganizationLevel
						.setRbacHigherLevel(rbacHigherLevel);
				rbacRoleOrganizationLevelList.add(newRbacRoleOrganizationLevel);

			} else if ("mod".equals(opType)) {

				rbacRoleOrganizationLevel.setRbacLowerLevel(rbacLowerLevel);
				rbacRoleOrganizationLevel.setRbacHigherLevel(rbacHigherLevel);

				rbacRoleOrganizationLevelList.add(rbacRoleOrganizationLevel);

			}

		}

		if ("add".equals(opType)) {
			rbacRoleOrganizationLevelManager
					.addRbacRoleOrganizationLevelList(rbacRoleOrganizationLevelList);
		} else if ("mod".equals(opType)) {
			rbacRoleOrganizationLevelManager
					.updateRbacRoleOrganizationLevelList(rbacRoleOrganizationLevelList);
		}

		for (RbacRoleOrganizationLevel rbacRoleOrganizationLevel : rbacRoleOrganizationLevelList) {
			rbacRoleOrganizationLevelManager
					.updateRbacRoleOrganizationLevelToRaptornuke(rbacRoleOrganizationLevel);
		}

		Events.postEvent("onOK", this.self, rbacRoleOrganizationLevelList);

		this.onCancel();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacRoleOrganizationLevelEditWindow().onClose();
	}
}
