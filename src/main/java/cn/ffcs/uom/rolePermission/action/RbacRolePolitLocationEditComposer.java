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
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.rolePermission.action.bean.RbacRolePolitLocationEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacRolePolitLocationManager;
import cn.ffcs.uom.rolePermission.model.RbacRolePolitLocation;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRolePolitLocationEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacRolePolitLocationEditBean bean = new RbacRolePolitLocationEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选中的角色行政管理区域关系
	 */
	private RbacRolePolitLocation rbacRolePolitLocation;

	private RbacRolePolitLocationManager rbacRolePolitLocationManager = (RbacRolePolitLocationManager) ApplicationContextUtil
			.getBean("rbacRolePolitLocationManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacRolePolitLocationEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacRoleRelationTreeBandboxExt().setDisabled(true);
		this.bean.getRbacRoleRelationTreeBandboxExt().setReadonly(true);
		this.bean.getPoliticalLocationTreeBandbox().setReadonly(true);
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

			this.bean.getRbacRolePolitLocationEditWindow().setTitle(
					"新增角色行政管理区域关系");

		} else if ("mod".equals(opType)) {

			this.bean.getRbacRolePolitLocationEditWindow().setTitle(
					"修改角色行政管理区域关系");

			rbacRolePolitLocation = (RbacRolePolitLocation) arg
					.get("rbacRolePolitLocation");

			if (rbacRolePolitLocation != null
					&& rbacRolePolitLocation.getRbacRolePolitLocationId() != null) {

				bean.getPoliticalLocationTreeBandbox().setPoliticalLocation(
						rbacRolePolitLocation.getPoliticalLocation());

			}

		}

	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {

		PoliticalLocation politicalLocation = this.bean
				.getPoliticalLocationTreeBandbox().getPoliticalLocation();

		if (politicalLocation == null
				|| politicalLocation.getLocationId() == null) {
			ZkUtil.showError("请选择行政管理区域", "提示信息");
			return;
		}

		List<RbacRoleRelation> rbacRoleRelationList = this.bean
				.getRbacRoleRelationTreeBandboxExt().getRbacRoleRelationList();

		if (rbacRoleRelationList == null || rbacRoleRelationList.size() <= 0) {
			ZkUtil.showError("请选择角色", "提示信息");
			return;
		}

		List<RbacRolePolitLocation> rbacRolePolitLocationList = new ArrayList<RbacRolePolitLocation>();

		for (RbacRoleRelation rbacRoleRelation : rbacRoleRelationList) {

			RbacRolePolitLocation newRbacRolePolitLocation = new RbacRolePolitLocation();
			newRbacRolePolitLocation.setRbacPolitLocationId(politicalLocation
					.getLocationId());
			newRbacRolePolitLocation.setRbacRoleId(rbacRoleRelation
					.getRbacRoleId());

			RbacRolePolitLocation rbacRolePolitLocationDb = rbacRolePolitLocationManager
					.queryRbacRolePolitLocation(newRbacRolePolitLocation);

			if ("add".equals(opType)) {

				if (rbacRolePolitLocationDb != null) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacRolePolitLocationList.add(newRbacRolePolitLocation);

			} else if ("mod".equals(opType)) {

				if (rbacRolePolitLocationDb != null
						&& !rbacRolePolitLocation.getRbacRolePolitLocationId()
								.equals(rbacRolePolitLocationDb
										.getRbacRolePolitLocationId())) {
					ZkUtil.showError("角色:"
							+ rbacRoleRelation.getRbacRole().getRbacRoleName()
							+ ",已存在", "提示信息");
					return;
				}

				rbacRolePolitLocation.setRbacPolitLocationId(politicalLocation
						.getLocationId());

				rbacRolePolitLocationList.add(rbacRolePolitLocation);

			}

		}

		if ("add".equals(opType)) {
			rbacRolePolitLocationManager
					.addRbacRolePolitLocationList(rbacRolePolitLocationList);
		} else if ("mod".equals(opType)) {
			rbacRolePolitLocationManager
					.updateRbacRolePolitLocationList(rbacRolePolitLocationList);
		}

		Events.postEvent("onOK", this.self, rbacRolePolitLocationList);

		this.onCancel();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacRolePolitLocationEditWindow().onClose();
	}
}
