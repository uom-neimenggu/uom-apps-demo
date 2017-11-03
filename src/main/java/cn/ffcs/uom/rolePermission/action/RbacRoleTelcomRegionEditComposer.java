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
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleTelcomRegionEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRoleTelcomRegionManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class RbacRoleTelcomRegionEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacRoleTelcomRegionEditBean bean = new RbacRoleTelcomRegionEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的角色电信管理区域关系
	 */
	private RbacRoleTelcomRegion rbacRoleTelcomRegion;

	private RbacRoleTelcomRegionManager rbacRoleTelcomRegionManager = (RbacRoleTelcomRegionManager) ApplicationContextUtil
			.getBean("rbacRoleTelcomRegionManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacRoleTelcomRegionEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getTelcomRegionTreeBandbox().setReadonly(true);
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

			this.bean.getRbacRoleTelcomRegionEditWindow().setTitle(
					"新增角色电信管理区域关系");

		} else if ("mod".equals(opType)) {

			this.bean.getRbacRoleTelcomRegionEditWindow().setTitle(
					"修改角色电信管理区域关系");

			rbacRoleTelcomRegion = (RbacRoleTelcomRegion) arg
					.get("rbacRoleTelcomRegion");

			if (rbacRoleTelcomRegion != null
					&& rbacRoleTelcomRegion.getRbacRoleTelcomRegionId() != null) {

				bean.getTelcomRegionTreeBandbox().setTelcomRegion(
						rbacRoleTelcomRegion.getTelcomRegion());

			}

		}

	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {

		TelcomRegion telcomRegion = this.bean.getTelcomRegionTreeBandbox()
				.getTelcomRegion();

		if (telcomRegion == null || telcomRegion.getTelcomRegionId() == null) {
			ZkUtil.showError("请选择电信管理区域", "提示信息");
			return;
		}

		List<RbacRoleRelation> rbacRoleRelationList = this.bean
				.getRbacRoleRelationTreeBandboxExt().getRbacRoleRelationList();

		if (rbacRoleRelationList == null || rbacRoleRelationList.size() <= 0) {
			ZkUtil.showError("请选择角色", "提示信息");
			return;
		}

		List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList = new ArrayList<RbacRoleTelcomRegion>();

		for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

			RbacRoleTelcomRegion newRbacRoleTelcomRegion = new RbacRoleTelcomRegion();
			newRbacRoleTelcomRegion.setRbacTelcomRegionId(telcomRegion
					.getTelcomRegionId());
			newRbacRoleTelcomRegion.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());

			RbacRoleTelcomRegion rbacRoleTelcomRegionDb = rbacRoleTelcomRegionManager
					.queryRbacRoleTelcomRegion(newRbacRoleTelcomRegion);

			if ("add".equals(opType)) {

				if (rbacRoleTelcomRegionDb != null) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacRoleTelcomRegionList.add(newRbacRoleTelcomRegion);

			} else if ("mod".equals(opType)) {

				if (rbacRoleTelcomRegionDb != null
						&& !rbacRoleTelcomRegion.getRbacTelcomRegionId()
								.equals(rbacRoleTelcomRegionDb
										.getRbacTelcomRegionId())) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacRoleTelcomRegion.setRbacTelcomRegionId(telcomRegion
						.getTelcomRegionId());

				rbacRoleTelcomRegionList.add(rbacRoleTelcomRegion);

			}

		}

		if ("add".equals(opType)) {
			rbacRoleTelcomRegionManager
					.addRbacRoleTelcomRegionList(rbacRoleTelcomRegionList);
			for (RbacRoleTelcomRegion rbacRoleTelcomRegion : rbacRoleTelcomRegionList) {

				rbacRoleTelcomRegionManager
						.updateRbacRoleTelcomRegionToRaptornuke(rbacRoleTelcomRegion);
			}
		} else if ("mod".equals(opType)) {
			rbacRoleTelcomRegionManager
					.updateRbacRoleTelcomRegionList(rbacRoleTelcomRegionList);
			rbacRoleTelcomRegionManager
					.updateRbacRoleTelcomRegionToRaptornuke(rbacRoleTelcomRegion);
		}

		Events.postEvent("onOK", this.self, rbacRoleTelcomRegionList);

		this.onCancel();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacRoleTelcomRegionEditWindow().onClose();
	}
}
