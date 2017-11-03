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
import cn.ffcs.uom.rolePermission.action.bean.RbacBusinessSystemRelationTreeNodeEditBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

@Controller
@Scope("prototype")
public class RbacBusinessSystemRelationTreeNodeEditComposer extends
		BasePortletComposer {

	private static final long serialVersionUID = -1014502602479906821L;

	/**
	 * 页面bean
	 */
	private RbacBusinessSystemRelationTreeNodeEditBean bean = new RbacBusinessSystemRelationTreeNodeEditBean();

	/**
	 * 操作类型
	 */
	private String opType;

	/**
	 * 选择的系统
	 */
	private RbacBusinessSystem rbacBusinessSystem;

	private RbacBusinessSystemRelationManager rbacBusinessSystemRelationManager = (RbacBusinessSystemRelationManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemRelationManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacBusinessSystemRelationTreeNodeEditWindow() {
		this.bindBean();
	}

	/**
	 * bindBean
	 */
	private void bindBean() {

		opType = (String) arg.get("opType");

		if ("addChildNode".equals(opType)) {

			this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow()
					.setTitle("增加子节点");

			rbacBusinessSystem = (RbacBusinessSystem) arg
					.get("rbacBusinessSystem");

			if (rbacBusinessSystem == null) {
				ZkUtil.showError("上级节点未选择", "提示信息");
				this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow()
						.onClose();
			}

		} else if ("addRootNode".equals(opType)) {

			this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow()
					.setTitle("增加根节点");

		} else {

			ZkUtil.showError("未定义操作类型", "提示信息");

			this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow()
					.onClose();

			return;

		}
	}

	/**
	 * 确定
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onAdd() {

		this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow().onClose();

		Map map = new HashMap();
		RbacBusinessSystem newRbacBusinessSystem = this.bean
				.getRbacBusinessSystemBandboxExt().getRbacBusinessSystem();

		if (newRbacBusinessSystem == null) {
			ZkUtil.showError("请选择系统", "提示信息");
			return;
		}

		RbacBusinessSystemRelation rbacBusinessSystemRelation = new RbacBusinessSystemRelation();

		if (opType.equals("addChildNode")) {

			rbacBusinessSystemRelation
					.setRbacBusinessSystemId(newRbacBusinessSystem
							.getRbacBusinessSystemId());
			rbacBusinessSystemRelation
					.setRbacParentBusinessSystemId(rbacBusinessSystem
							.getRbacBusinessSystemId());

			if (rbacBusinessSystemRelation.getRbacBusinessSystemId() != null
					&& rbacBusinessSystemRelation.getRbacBusinessSystemId()
							.equals(rbacBusinessSystemRelation
									.getRbacParentBusinessSystemId())) {
				ZkUtil.showError("该系统的上级不能是本身", "提示信息");
				return;
			}

			RbacBusinessSystemRelation queryRbacBusinessSystemRelation = new RbacBusinessSystemRelation();

			// queryRbacBusinessSystemRelation.setRbacBusinessSystemId(newRbacBusinessSystem.getRbacBusinessSystemId());

			// List<RbacBusinessSystemRelation>
			// queryRbacBusinessSystemRelationList =
			// this.rbacBusinessSystemRelationManager
			// .queryRbacBusinessSystemRelationList(queryRbacBusinessSystemRelation);
			//
			// if (queryRbacBusinessSystemRelationList != null
			// && queryRbacBusinessSystemRelationList.size() > 0) {
			// ZkUtil.showError("该系统已有上级,不能再挂上级系统!", "提示信息");
			// return;
			// }

			queryRbacBusinessSystemRelation = rbacBusinessSystemRelationManager
					.queryRbacBusinessSystemRelation(rbacBusinessSystemRelation);

			if (queryRbacBusinessSystemRelation != null
					&& queryRbacBusinessSystemRelation
							.getRbacBusinessSystemRelaId() != null) {
				ZkUtil.showError("该关系已经存在", "提示信息");
				return;
			}

			List<RbacBusinessSystemRelation> subRbacBusinessSystemRelationList = this.rbacBusinessSystemRelationManager
					.querySubTreeRbacBusinessSystemRelationList(rbacBusinessSystemRelation);

			if (subRbacBusinessSystemRelationList != null) {
				for (RbacBusinessSystemRelation subRbacBusinessSystemRelation : subRbacBusinessSystemRelationList) {
					if (rbacBusinessSystemRelation
							.getRbacParentBusinessSystemId().equals(
									subRbacBusinessSystemRelation
											.getRbacBusinessSystemId())) {
						ZkUtil.showError("存在环不可添加", "提示信息");
						return;
					}
				}
			}

			rbacBusinessSystemRelationManager
					.saveRbacBusinessSystemRelation(rbacBusinessSystemRelation);

		} else if (opType.equals("addRootNode")) {

			RbacBusinessSystemRelation queryRbacBusinessSystemRelation = new RbacBusinessSystemRelation();
			queryRbacBusinessSystemRelation
					.setRbacBusinessSystemId(newRbacBusinessSystem
							.getRbacBusinessSystemId());

			List<RbacBusinessSystemRelation> queryRbacBusinessSystemRelationList = this.rbacBusinessSystemRelationManager
					.queryRbacBusinessSystemRelationList(queryRbacBusinessSystemRelation);

			if (queryRbacBusinessSystemRelationList != null
					&& queryRbacBusinessSystemRelationList.size() > 0) {
				ZkUtil.showError("该系统已有上级,不能设置为系统根节点", "提示信息");
				return;
			}

			rbacBusinessSystemRelation
					.setRbacBusinessSystemId(newRbacBusinessSystem
							.getRbacBusinessSystemId());
			rbacBusinessSystemRelation
					.setRbacParentBusinessSystemId(RolePermissionConstants.ROOT_ID);

			rbacBusinessSystemRelationManager
					.saveRbacBusinessSystemRelation(rbacBusinessSystemRelation);

		}

		map.put("rbacBusinessSystemRelation", rbacBusinessSystemRelation);
		Events.postEvent("onOK", this.self, map);

	}

	/**
	 * 取消
	 */
	public void onCancel() {
		this.bean.getRbacBusinessSystemRelationTreeNodeEditWindow().onClose();
	}

	/**
	 * 新增系统
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onAddRbacBusinessSystem() throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("rbacBusinessSystem", rbacBusinessSystem);
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_business_system_edit.zul",
				this.self, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				RbacBusinessSystem rbacBusinessSystem = (RbacBusinessSystem) event
						.getData();
				if (rbacBusinessSystem != null) {
					bean.getRbacBusinessSystemBandboxExt()
							.setRbacBusinessSystem(rbacBusinessSystem);
				}
			}
		});
	}
}
