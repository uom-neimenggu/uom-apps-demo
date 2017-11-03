package cn.ffcs.uom.rolePermission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacResourceRelationTreeNodeEditBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacResourceRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacResourceRelationTreeNodeEditComposer extends
		BasePortletComposer {

	private static final long serialVersionUID = -1014502602479906821L;

	/**
	 * 页面bean
	 */
	private RbacResourceRelationTreeNodeEditBean bean = new RbacResourceRelationTreeNodeEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选择的资源
	 */
	private RbacResource rbacResource;

	private RbacResourceRelationManager rbacResourceRelationManager = (RbacResourceRelationManager) ApplicationContextUtil
			.getBean("rbacResourceRelationManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacResourceRelationTreeNodeEditWindow() {
		this.bindBean();
	}

	/**
	 * bindBean
	 */
	private void bindBean() {

		opType = (String) arg.get("opType");

		if ("addChildNode".equals(opType)) {

			this.bean.getRbacResourceRelationTreeNodeEditWindow().setTitle(
					"增加子节点");

			rbacResource = (RbacResource) arg.get("rbacResource");

			if (rbacResource == null) {
				ZkUtil.showError("上级节点未选择", "提示信息");
				this.bean.getRbacResourceRelationTreeNodeEditWindow().onClose();
			}

		} else if ("addRootNode".equals(opType)) {

			this.bean.getRbacResourceRelationTreeNodeEditWindow().setTitle(
					"增加根节点");

		} else {

			ZkUtil.showError("未定义操作类型", "提示信息");

			this.bean.getRbacResourceRelationTreeNodeEditWindow().onClose();

			return;

		}
	}

	/**
	 * 确定
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() {

		this.bean.getRbacResourceRelationTreeNodeEditWindow().onClose();

		Map map = new HashMap();
		RbacResource newRbacResource = this.bean.getRbacResourceBandboxExt()
				.getRbacResource();

		if (newRbacResource == null) {
			ZkUtil.showError("请选择资源", "提示信息");
			return;
		}

		RbacResourceRelation rbacResourceRelation = new RbacResourceRelation();

		if (opType.equals("addChildNode")) {

			if (RolePermissionConstants.RBAC_RESOURCE_LEAF_1
					.equals(rbacResource.getRbacResourceLeaf())) {
				ZkUtil.showError("叶子节点下不能添加子节点", "提示信息");
				return;
			}

			rbacResourceRelation.setRbacResourceId(newRbacResource
					.getRbacResourceId());
			rbacResourceRelation.setRbacParentResourceId(rbacResource
					.getRbacResourceId());

			if (rbacResourceRelation.getRbacResourceId() != null
					&& rbacResourceRelation.getRbacResourceId().equals(
							rbacResourceRelation.getRbacParentResourceId())) {
				ZkUtil.showError("该资源的上级不能是本身", "提示信息");
				return;
			}

			RbacResourceRelation queryRbacResourceRelation = new RbacResourceRelation();

			// queryRbacResourceRelation.setRbacResourceId(newRbacResource.getRbacResourceId());

			// List<RbacResourceRelation> queryRbacResourceRelationList =
			// this.rbacResourceRelationManager
			// .queryRbacResourceRelationList(queryRbacResourceRelation);
			//
			// if (queryRbacResourceRelationList != null
			// && queryRbacResourceRelationList.size() > 0) {
			// ZkUtil.showError("该资源已有上级,不能再挂上级资源!", "提示信息");
			// return;
			// }

			queryRbacResourceRelation = rbacResourceRelationManager
					.queryRbacResourceRelation(rbacResourceRelation);

			if (queryRbacResourceRelation != null
					&& queryRbacResourceRelation.getRbacResourceRelaId() != null) {
				ZkUtil.showError("该关系已经存在", "提示信息");
				return;
			}

			List<RbacResourceRelation> subRbacResourceRelationList = this.rbacResourceRelationManager
					.querySubTreeRbacResourceRelationList(rbacResourceRelation);

			if (subRbacResourceRelationList != null) {
				for (RbacResourceRelation subRbacResourceRelation : subRbacResourceRelationList) {
					if (rbacResourceRelation.getRbacParentResourceId().equals(
							subRbacResourceRelation.getRbacResourceId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}

			rbacResourceRelationManager
					.saveRbacResourceRelation(rbacResourceRelation);

		} else if (opType.equals("addRootNode")) {

			RbacResourceRelation queryRbacResourceRelation = new RbacResourceRelation();
			queryRbacResourceRelation.setRbacResourceId(newRbacResource
					.getRbacResourceId());

			List<RbacResourceRelation> queryRbacResourceRelationList = this.rbacResourceRelationManager
					.queryRbacResourceRelationList(queryRbacResourceRelation);

			if (queryRbacResourceRelationList != null
					&& queryRbacResourceRelationList.size() > 0) {
				ZkUtil.showError("该资源已有上级,不能设置为资源根节点", "提示信息");
				return;
			}

			rbacResourceRelation.setRbacResourceId(newRbacResource
					.getRbacResourceId());
			rbacResourceRelation
					.setRbacParentResourceId(RolePermissionConstants.ROOT_ID);

			rbacResourceRelationManager
					.saveRbacResourceRelation(rbacResourceRelation);

		}

		map.put("rbacResourceRelation", rbacResourceRelation);
		Events.postEvent("onOK", this.self, map);

	}

	/**
	 * 取消
	 */
	public void onCancel() {
		this.bean.getRbacResourceRelationTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增资源
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onAddRbacResource() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("rbacResource", rbacResource);
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_resource_edit.zul", this.self, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				RbacResource rbacResource = (RbacResource) event.getData();
				if (rbacResource != null) {
					bean.getRbacResourceBandboxExt().setRbacResource(
							rbacResource);
				}
			}
		});
	}
}
