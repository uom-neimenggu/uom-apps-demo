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
import cn.ffcs.uom.rolePermission.action.bean.RbacPermissionResourceEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRelationManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;
import cn.ffcs.uom.rolePermission.model.RbacPermissionResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacPermissionResourceEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacPermissionResourceEditBean bean = new RbacPermissionResourceEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	private RbacPermissionResource rbacPermissionResource;

	private RbacPermissionRelationManager rbacPermissionRelationManager = (RbacPermissionRelationManager) ApplicationContextUtil
			.getBean("rbacPermissionRelationManager");

	private RbacPermissionResourceManager rbacPermissionResourceManager = (RbacPermissionResourceManager) ApplicationContextUtil
			.getBean("rbacPermissionResourceManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacPermissionResourceEditWindow() throws Exception {
		this.bindBean();
		this.bean.getRbacResourceRelationTreeBandboxExt().setReadonly(true);
		this.bean.getRbacPermissionRelationTreeBandboxExt().setReadonly(true);

		if (bean.getRbacResourceRelationTreeBandboxExt()
				.getRbacResourceRelationList() != null
				&& bean.getRbacResourceRelationTreeBandboxExt()
						.getRbacResourceRelationList().size() > 0) {
			this.bean.getRbacResourceRelationTreeBandboxExt().setDisabled(true);
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
			this.bean.getRbacPermissionResourceEditWindow()
					.setTitle("新增权限资源关系");
			rbacPermissionResource = (RbacPermissionResource) arg
					.get("rbacPermissionResource");

			if (rbacPermissionResource != null) {

				if (rbacPermissionResource.getRbacResourceId() != null) {
					List<RbacResourceRelation> rbacResourceRelationList = new ArrayList<RbacResourceRelation>();
					RbacResourceRelation rbacResourceRelation = new RbacResourceRelation();
					rbacResourceRelation
							.setRbacResourceId(rbacPermissionResource
									.getRbacResourceId());
					rbacResourceRelationList.add(rbacResourceRelation);
					this.bean.getRbacResourceRelationTreeBandboxExt()
							.setRbacResourceRelationList(
									rbacResourceRelationList);
				}

				if (rbacPermissionResource.getRbacPermissionRelaId() != null) {
					List<RbacPermissionRelation> rbacPermissionRelationList = new ArrayList<RbacPermissionRelation>();
					RbacPermissionRelation rbacPermissionRelation = new RbacPermissionRelation();
					rbacPermissionRelation
							.setRbacPermissionRelaId(rbacPermissionResource
									.getRbacPermissionRelaId());
					rbacPermissionRelation = rbacPermissionRelationManager
							.queryRbacPermissionRelation(rbacPermissionRelation);
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

			List<RbacResourceRelation> rbacResourceRelationList = this.bean
					.getRbacResourceRelationTreeBandboxExt()
					.getRbacResourceRelationList();

			if (rbacResourceRelationList == null
					|| rbacResourceRelationList.size() <= 0) {
				ZkUtil.showError("请选择资源", "提示信息");
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

			List<RbacPermissionResource> rbacPermissionResourceList = new ArrayList<RbacPermissionResource>();

			for (RbacResourceRelation rbacResourceRelation : rbacResourceRelationList) {

				for (RbacPermissionRelation rbacPermissionRelation : rbacPermissionRelationList) {

					RbacPermissionResource rbacPermissionResource = new RbacPermissionResource();
					rbacPermissionResource
							.setRbacResourceId(rbacResourceRelation
									.getRbacResourceId());
					rbacPermissionResource
							.setRbacPermissionRelaId(rbacPermissionRelation
									.getRbacPermissionRelaId());

					RbacPermissionResource rbacPermissionResourceDb = rbacPermissionResourceManager
							.queryRbacPermissionResource(rbacPermissionResource);

					if (rbacPermissionResourceDb != null) {
						ZkUtil.showError("资源:"
								+ rbacResourceRelation.getRbacResource()
										.getRbacResourceName()
								+ ",或权限："
								+ rbacPermissionRelation.getRbacPermission()
										.getRbacPermissionName() + ",已存在",
								"提示信息");
						return;
					}

					rbacPermissionResourceList.add(rbacPermissionResource);
				}

			}

			rbacPermissionResourceManager
					.addRbacPermissionResourceList(rbacPermissionResourceList);

			Events.postEvent("onOK", this.self, rbacPermissionResourceList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacPermissionResourceEditWindow().onClose();
	}
}
