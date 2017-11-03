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
import cn.ffcs.uom.rolePermission.action.bean.RbacBusinessSystemResourceEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacBusinessSystemResourceEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 2507005075323523801L;

	/**
	 * 页面bean
	 */
	private RbacBusinessSystemResourceEditBean bean = new RbacBusinessSystemResourceEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	private RbacBusinessSystemResource rbacBusinessSystemResource;

	private RbacBusinessSystemResourceManager rbacBusinessSystemResourceManager = (RbacBusinessSystemResourceManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemResourceManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	public void onCreate$rbacBusinessSystemResourceEditWindow()
			throws Exception {
		this.bindBean();
		this.bean.getRbacResourceRelationTreeBandboxExt().setReadonly(true);
		this.bean.getRbacBusinessSystemRelationTreeBandboxExt().setReadonly(
				true);

		if (bean.getRbacResourceRelationTreeBandboxExt()
				.getRbacResourceRelationList() != null
				&& bean.getRbacResourceRelationTreeBandboxExt()
						.getRbacResourceRelationList().size() > 0) {
			this.bean.getRbacResourceRelationTreeBandboxExt().setDisabled(true);
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
			this.bean.getRbacBusinessSystemResourceEditWindow().setTitle(
					"新增系统资源关系");
			rbacBusinessSystemResource = (RbacBusinessSystemResource) arg
					.get("rbacBusinessSystemResource");

			if (rbacBusinessSystemResource != null) {

				if (rbacBusinessSystemResource.getRbacResourceId() != null) {
					List<RbacResourceRelation> rbacResourceRelationList = new ArrayList<RbacResourceRelation>();
					RbacResourceRelation rbacResourceRelation = new RbacResourceRelation();
					rbacResourceRelation
							.setRbacResourceId(rbacBusinessSystemResource
									.getRbacResourceId());
					rbacResourceRelationList.add(rbacResourceRelation);
					this.bean.getRbacResourceRelationTreeBandboxExt()
							.setRbacResourceRelationList(
									rbacResourceRelationList);
				}

				if (rbacBusinessSystemResource.getRbacBusinessSystemId() != null) {
					List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList = new ArrayList<RbacBusinessSystemRelation>();
					RbacBusinessSystemRelation rbacBusinessSystemRelation = new RbacBusinessSystemRelation();
					rbacBusinessSystemRelation
							.setRbacBusinessSystemId(rbacBusinessSystemResource
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
		if ("add".equals(opType)) {

			List<RbacResourceRelation> rbacResourceRelationList = this.bean
					.getRbacResourceRelationTreeBandboxExt()
					.getRbacResourceRelationList();

			if (rbacResourceRelationList == null
					|| rbacResourceRelationList.size() <= 0) {
				ZkUtil.showError("请选择资源", "提示信息");
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

			List<RbacBusinessSystemResource> rbacBusinessSystemResourceList = new ArrayList<RbacBusinessSystemResource>();

			for (RbacResourceRelation rbacResourceRelation : rbacResourceRelationList) {

				for (RbacBusinessSystemRelation rbacBusinessSystemRelation : rbacBusinessSystemRelationList) {

					RbacBusinessSystemResource rbacBusinessSystemResource = new RbacBusinessSystemResource();
					rbacBusinessSystemResource
							.setRbacResourceId(rbacResourceRelation
									.getRbacResourceId());
					rbacBusinessSystemResource
							.setRbacBusinessSystemId(rbacBusinessSystemRelation
									.getRbacBusinessSystemId());

					RbacBusinessSystemResource rbacBusinessSystemResourceDb = rbacBusinessSystemResourceManager
							.queryRbacBusinessSystemResource(rbacBusinessSystemResource);

					if (rbacBusinessSystemResourceDb != null) {
						ZkUtil.showError("系统:"
								+ rbacBusinessSystemRelation
										.getRbacBusinessSystem()
										.getRbacBusinessSystemName()
								+ ",或资源："
								+ rbacResourceRelation.getRbacResource()
										.getRbacResourceName() + ",已存在", "提示信息");
						return;
					}

					rbacBusinessSystemResourceList
							.add(rbacBusinessSystemResource);
				}

			}

			rbacBusinessSystemResourceManager
					.addRbacBusinessSystemResourceList(rbacBusinessSystemResourceList);

			Events.postEvent("onOK", this.self, rbacBusinessSystemResourceList);
			this.onCancel();
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRbacBusinessSystemResourceEditWindow().onClose();
	}
}
