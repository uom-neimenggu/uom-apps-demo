package cn.ffcs.uom.rolePermission.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.rolePermission.action.bean.RbacBusinessSystemEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

@Controller
@Scope("prototype")
public class RbacBusinessSystemEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * rbacBusinessSystemEditBean.
	 */
	private RbacBusinessSystemEditBean bean = new RbacBusinessSystemEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("rbacBusinessSystemManager")
	private RbacBusinessSystemManager rbacBusinessSystemManager;

	/**
	 * 系统.
	 */
	private RbacBusinessSystem rbacBusinessSystem;

	/**
	 * 父系统.
	 */
	@SuppressWarnings("unused")
	private RbacBusinessSystem rbacParentBusinessSystem;

	/**
	 * 修改系统.
	 */
	private RbacBusinessSystem oldRbacBusinessSystem;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacBusinessSystemEditWindow() {

		this.bindCombobox();

		opType = StrUtil.strnull(arg.get("opType"));

		if ("addChildNode".equals(opType)) {
			this.bean.getRbacBusinessSystemEditWindow().setTitle("新增子节点");
			rbacParentBusinessSystem = (RbacBusinessSystem) arg
					.get("rbacBusinessSystem");
		} else if ("addRootNode".equals(opType)) {
			this.bean.getRbacBusinessSystemEditWindow().setTitle("新增根节点");
		} else {
			if ("view".equals(opType)) {
				this.bean.getRbacBusinessSystemEditWindow().setTitle("系统查看");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			} else {
				this.bean.getRbacBusinessSystemEditWindow().setTitle("系统修改");
			}
			oldRbacBusinessSystem = (RbacBusinessSystem) arg
					.get("rbacBusinessSystem");
			if (oldRbacBusinessSystem != null) {
				this.bean.getRbacBusinessSystemCode().setValue(
						oldRbacBusinessSystem.getRbacBusinessSystemCode());
				this.bean.getRbacBusinessSystemName().setValue(
						oldRbacBusinessSystem.getRbacBusinessSystemName());
				this.bean.getRbacBusinessSystemUrl().setValue(
						oldRbacBusinessSystem.getRbacBusinessSystemUrl());
				ListboxUtils.selectByCodeValue(
						this.bean.getRbacBusinessSystemDomain(),
						oldRbacBusinessSystem.getRbacBusinessSystemDomain());
				this.bean.getRbacBusinessSystemDesc().setValue(
						oldRbacBusinessSystem.getRbacBusinessSystemDesc());
			}
		}
	}

	/**
	 * 绑定combobox.
	 */
	private void bindCombobox() {
		List<NodeVo> rbacBusinessSystemDomain = UomClassProvider.getValuesList(
				"RbacBusinessSystem", "rbacBusinessSystemDomain");
		ListboxUtils.rendererForEdit(bean.getRbacBusinessSystemDomain(),
				rbacBusinessSystemDomain);

	}

	/**
	 * 保存.
	 */
	public void onOk() {

		if (StrUtil.isEmpty(bean.getRbacBusinessSystemCode().getValue())) {
			ZkUtil.showError("系统编码不能为空,请填写", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(bean.getRbacBusinessSystemName().getValue())) {
			ZkUtil.showError("系统名称不能为空,请填写", "提示信息");
			return;
		}

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacBusinessSystem = new RbacBusinessSystem();
		} else if ("mod".equals(opType)) {
			rbacBusinessSystem = oldRbacBusinessSystem;
		}

		rbacBusinessSystem.setRbacBusinessSystemCode(bean
				.getRbacBusinessSystemCode().getValue());
		rbacBusinessSystem.setRbacBusinessSystemName(bean
				.getRbacBusinessSystemName().getValue());
		rbacBusinessSystem.setRbacBusinessSystemUrl(bean
				.getRbacBusinessSystemUrl().getValue());
		if (bean.getRbacBusinessSystemDomain().getSelectedItem().getValue() != null) {
			rbacBusinessSystem.setRbacBusinessSystemDomain(bean
					.getRbacBusinessSystemDomain().getSelectedItem().getValue()
					.toString());
		}
		rbacBusinessSystem.setRbacBusinessSystemDesc(bean
				.getRbacBusinessSystemDesc().getValue());

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacBusinessSystemManager
					.saveRbacBusinessSystem(rbacBusinessSystem);
		} else if ("mod".equals(opType)) {
			rbacBusinessSystemManager
					.updateRbacBusinessSystem(rbacBusinessSystem);
		}

		// 抛出成功事件
		Events.postEvent("onOK", bean.getRbacBusinessSystemEditWindow(),
				rbacBusinessSystem);
		bean.getRbacBusinessSystemEditWindow().onClose();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getRbacBusinessSystemEditWindow().onClose();
	}

}
