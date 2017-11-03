package cn.ffcs.uom.rolePermission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacPermissionRelationTreeNodeEditBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionExtAttrManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionManager;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Controller
@Scope("prototype")
public class RbacPermissionRelationTreeNodeEditComposer extends
		BasePortletComposer {

	private static final long serialVersionUID = -1014502602479906821L;

	/**
	 * 页面bean
	 */
	private RbacPermissionRelationTreeNodeEditBean bean = new RbacPermissionRelationTreeNodeEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选择的权限
	 */
	private RbacPermission rbacPermission;

	@Autowired
	@Qualifier("rbacPermissionManager")
	private RbacPermissionManager rbacPermissionManager = (RbacPermissionManager) ApplicationContextUtil
			.getBean("rbacPermissionManager");

	private RbacPermissionRelationManager rbacPermissionRelationManager = (RbacPermissionRelationManager) ApplicationContextUtil
			.getBean("rbacPermissionRelationManager");

	@Autowired
	@Qualifier("rbacPermissionExtAttrManager")
	private RbacPermissionExtAttrManager rbacPermissionExtAttrManager = (RbacPermissionExtAttrManager) ApplicationContextUtil
			.getBean("rbacPermissionExtAttrManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacPermissionRelationTreeNodeEditWindow() {
		this.bindBean();
	}

	/**
	 * bindBean
	 */
	private void bindBean() {

		opType = (String) arg.get("opType");

		if ("addChildNode".equals(opType)) {

			this.bean.getRbacPermissionRelationTreeNodeEditWindow().setTitle(
					"增加子节点");

			rbacPermission = (RbacPermission) arg.get("rbacPermission");

			if (rbacPermission == null) {
				ZkUtil.showError("上级节点未选择", "提示信息");
				this.bean.getRbacPermissionRelationTreeNodeEditWindow()
						.onClose();
			}

		} else if ("addRootNode".equals(opType)) {

			this.bean.getRbacPermissionRelationTreeNodeEditWindow().setTitle(
					"增加根节点");

		} else {

			ZkUtil.showError("未定义操作类型", "提示信息");

			this.bean.getRbacPermissionRelationTreeNodeEditWindow().onClose();

			return;

		}
	}

	/**
	 * 确定
	 * 
	 * @throws SystemException
	 * @throws PortalException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() throws PortalException, SystemException {

		this.bean.getRbacPermissionRelationTreeNodeEditWindow().onClose();

		Map map = new HashMap();
		RbacPermission newRbacPermission = this.bean
				.getRbacPermissionBandboxExt().getRbacPermission();

		if (newRbacPermission == null) {
			ZkUtil.showError("请选择权限", "提示信息");
			return;
		}

		RbacPermissionRelation rbacPermissionRelation = new RbacPermissionRelation();

		if (opType.equals("addChildNode")) {

			rbacPermissionRelation.setRbacPermissionId(newRbacPermission
					.getRbacPermissionId());
			rbacPermissionRelation.setRbacParentPermissionId(rbacPermission
					.getRbacPermissionId());

			if (rbacPermissionRelation.getRbacPermissionId() != null
					&& rbacPermissionRelation.getRbacPermissionId().equals(
							rbacPermissionRelation.getRbacParentPermissionId())) {
				ZkUtil.showError("该权限的上级不能是本身", "提示信息");
				return;
			}

			RbacPermissionRelation queryRbacPermissionRelation = new RbacPermissionRelation();

			// queryRbacPermissionRelation.setRbacPermissionId(newRbacPermission.getRbacPermissionId());

			// List<RbacPermissionRelation> queryRbacPermissionRelationList =
			// this.rbacPermissionRelationManager
			// .queryRbacPermissionRelationList(queryRbacPermissionRelation);
			//
			// if (queryRbacPermissionRelationList != null
			// && queryRbacPermissionRelationList.size() > 0) {
			// ZkUtil.showError("该权限已有上级,不能再挂上级权限!", "提示信息");
			// return;
			// }

			queryRbacPermissionRelation = rbacPermissionRelationManager
					.queryRbacPermissionRelation(rbacPermissionRelation);

			if (queryRbacPermissionRelation != null
					&& queryRbacPermissionRelation.getRbacPermissionRelaId() != null) {
				ZkUtil.showError("该关系已经存在", "提示信息");
				return;
			}

			List<RbacPermissionRelation> subRbacPermissionRelationList = this.rbacPermissionRelationManager
					.querySubTreeRbacPermissionRelationList(rbacPermissionRelation);

			if (subRbacPermissionRelationList != null) {
				for (RbacPermissionRelation subRbacPermissionRelation : subRbacPermissionRelationList) {
					if (rbacPermissionRelation.getRbacParentPermissionId()
							.equals(subRbacPermissionRelation
									.getRbacPermissionId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}

			rbacPermissionRelationManager
					.saveRbacPermissionRelation(rbacPermissionRelation);
			this.updateRbacPermissionToRaptornuke();

		} else if (opType.equals("addRootNode")) {

			RbacPermissionRelation queryRbacPermissionRelation = new RbacPermissionRelation();
			queryRbacPermissionRelation.setRbacPermissionId(newRbacPermission
					.getRbacPermissionId());

			List<RbacPermissionRelation> queryRbacPermissionRelationList = this.rbacPermissionRelationManager
					.queryRbacPermissionRelationList(queryRbacPermissionRelation);

			if (queryRbacPermissionRelationList != null
					&& queryRbacPermissionRelationList.size() > 0) {
				ZkUtil.showError("该权限已有上级,不能设置为权限根节点", "提示信息");
				return;
			}

			rbacPermissionRelation.setRbacPermissionId(newRbacPermission
					.getRbacPermissionId());
			rbacPermissionRelation
					.setRbacParentPermissionId(RolePermissionConstants.ROOT_ID);

			rbacPermissionRelationManager
					.saveRbacPermissionRelation(rbacPermissionRelation);

		}

		map.put("rbacPermissionRelation", rbacPermissionRelation);
		Events.postEvent("onOK", this.self, map);

	}

	public void updateRbacPermissionToRaptornuke() throws PortalException,
			SystemException {

		RbacPermission rbacParentPermission = rbacPermission;

		if (rbacParentPermission != null) {

			RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();
			rbacPermissionExtAttr.setRbacPermissionId(rbacParentPermission
					.getRbacPermissionId());
			rbacPermissionExtAttr
					.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1);
			rbacPermissionExtAttr = rbacPermissionExtAttrManager
					.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

			if (rbacPermissionExtAttr != null
					&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

				rbacPermissionExtAttr = new RbacPermissionExtAttr();
				rbacPermissionExtAttr.setRbacPermissionId(rbacParentPermission
						.getRbacPermissionId());
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2);
				rbacPermissionExtAttr = rbacPermissionExtAttrManager
						.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

				if (rbacPermissionExtAttr != null
						&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2_ATTR_VALUE_1
								.equals(rbacPermissionExtAttr
										.getRbacPermissionAttrValue())) {

					rbacPermissionExtAttr = new RbacPermissionExtAttr();
					rbacPermissionExtAttr
							.setRbacPermissionId(rbacParentPermission
									.getRbacPermissionId());
					rbacPermissionExtAttr
							.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_4);

					rbacPermissionExtAttr = rbacPermissionExtAttrManager
							.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

					if (rbacPermissionExtAttr != null
							&& !StrUtil.isEmpty(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {
						rbacPermissionManager.updateRbacPermissionToRaptornuke(
								bean.getRbacPermissionBandboxExt()
										.getRbacPermission(),
								rbacPermissionExtAttr);
					}
				}
			}
		}
	}

	/**
	 * 取消
	 */
	public void onCancel() {
		this.bean.getRbacPermissionRelationTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增权限
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onAddRbacPermission() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("rbacPermission", rbacPermission);
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_permission_edit.zul", this.self,
				map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				RbacPermission rbacPermission = (RbacPermission) event
						.getData();
				if (rbacPermission != null) {
					bean.getRbacPermissionBandboxExt().setRbacPermission(
							rbacPermission);
				}
			}
		});
	}
}
