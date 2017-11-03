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
import cn.ffcs.uom.rolePermission.action.bean.RbacResourceEditBean;
import cn.ffcs.uom.rolePermission.manager.RbacResourceManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;

@Controller
@Scope("prototype")
public class RbacResourceEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * rbacResourceEditBean.
	 */
	private RbacResourceEditBean bean = new RbacResourceEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("rbacResourceManager")
	private RbacResourceManager rbacResourceManager;

	/**
	 * 资源.
	 */
	private RbacResource rbacResource;

	/**
	 * 父资源.
	 */
	@SuppressWarnings("unused")
	private RbacResource rbacParentResource;

	/**
	 * 修改资源.
	 */
	private RbacResource oldRbacResource;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 */
	public void onCreate$rbacResourceEditWindow() {

		this.bindCombobox();

		opType = StrUtil.strnull(arg.get("opType"));

		if ("addChildNode".equals(opType)) {
			this.bean.getRbacResourceEditWindow().setTitle("新增子节点");
			rbacParentResource = (RbacResource) arg.get("rbacResource");
		} else if ("addRootNode".equals(opType)) {
			this.bean.getRbacResourceEditWindow().setTitle("新增根节点");
		} else {
			if ("view".equals(opType)) {
				this.bean.getRbacResourceEditWindow().setTitle("资源查看");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			} else {
				this.bean.getRbacResourceEditWindow().setTitle("资源修改");
			}
			oldRbacResource = (RbacResource) arg.get("rbacResource");
			if (oldRbacResource != null) {
				this.bean.getRbacResourceCode().setValue(
						oldRbacResource.getRbacResourceCode());
				this.bean.getRbacResourceName().setValue(
						oldRbacResource.getRbacResourceName());
				ListboxUtils.selectByCodeValue(this.bean.getRbacResourceLeaf(),
						oldRbacResource.getRbacResourceLeaf());
				this.bean.getRbacResourceUrl().setValue(
						oldRbacResource.getRbacResourceUrl());
				this.bean.getRbacResourceDesc().setValue(
						oldRbacResource.getRbacResourceDesc());
			}
		}
	}

	/**
	 * 绑定combobox.
	 */
	private void bindCombobox() {
		List<NodeVo> rbacResourceLeaf = UomClassProvider.getValuesList(
				"RbacResource", "rbacResourceLeaf");
		ListboxUtils.rendererForEdit(bean.getRbacResourceLeaf(),
				rbacResourceLeaf);

	}

	/**
	 * 保存.
	 */
	public void onOk() {

		if (StrUtil.isEmpty(bean.getRbacResourceCode().getValue())) {
			ZkUtil.showError("资源编码不能为空,请填写", "提示信息");
			return;
		}

		if (StrUtil.isEmpty(bean.getRbacResourceName().getValue())) {
			ZkUtil.showError("资源名称不能为空,请填写", "提示信息");
			return;
		}

		if (StrUtil.isNullOrEmpty(bean.getRbacResourceLeaf().getSelectedItem()
				.getValue())
				|| StrUtil.isEmpty(bean.getRbacResourceLeaf().getSelectedItem()
						.getValue().toString())) {
			ZkUtil.showError("叶子节点不能为空,请选择", "提示信息");
			return;
		}

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacResource = new RbacResource();
		} else if ("mod".equals(opType)) {
			rbacResource = oldRbacResource;
		}

		rbacResource.setRbacResourceCode(bean.getRbacResourceCode().getValue());
		rbacResource.setRbacResourceName(bean.getRbacResourceName().getValue());
		rbacResource.setRbacResourceLeaf(bean.getRbacResourceLeaf()
				.getSelectedItem().getValue().toString());
		rbacResource.setRbacResourceUrl(bean.getRbacResourceUrl().getValue());
		rbacResource.setRbacResourceDesc(bean.getRbacResourceDesc().getValue());

		if ("addChildNode".equals(opType) || "addRootNode".equals(opType)) {
			rbacResourceManager.saveRbacResource(rbacResource);
		} else if ("mod".equals(opType)) {
			rbacResourceManager.updateRbacResource(rbacResource);
		}

		// 抛出成功事件
		Events.postEvent("onOK", bean.getRbacResourceEditWindow(), rbacResource);
		bean.getRbacResourceEditWindow().onClose();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getRbacResourceEditWindow().onClose();
	}

}
