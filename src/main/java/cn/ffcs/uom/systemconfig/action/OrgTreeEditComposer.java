package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.OrgTreeEditBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class OrgTreeEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private OrgTreeEditBean bean = new OrgTreeEditBean();
	/**
	 * manager
	 */
	private OrgTreeManager orgTreeManager = (OrgTreeManager) ApplicationContextUtil
			.getBean("orgTreeManager");
	/**
	 * 操作类型
	 */
	private String opType;
	/**
	 * 当前组织树信息
	 */
	private OrgTree currentOrgTree;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate$orgTreeEditWin() throws Exception {
		opType = (String) arg.get("opType");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		if ("mod".equals(opType)) {
			currentOrgTree = (OrgTree) arg.get("orgTree");
			if (currentOrgTree == null) {
				ZkUtil.showError("参数错误", "提示信息");
				this.onCancel();
			}
			this.bean.getOrgTreeName()
					.setValue(currentOrgTree.getOrgTreeName());
			this.bean.getPreTime().setValue(currentOrgTree.getPreTime());
			this.bean.getLastTime().setValue(currentOrgTree.getLastTime());
			ListboxUtils.selectByCodeValue(this.bean.getISCalc(),
					currentOrgTree.getISCalc() + "");
			this.bean.getOrgTreeEditWin().setTitle("组织树信息修改");
		} else if ("add".equals(opType)) {
			this.bean.getOrgTreeEditWin().setTitle("组织树信息新增");
		}
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		if (StrUtil.isEmpty(this.bean.getOrgTreeName().getValue())) {
			ZkUtil.showError("组织树名称必填", "提示信息");
			return;
		}
		if (StrUtil.isNullOrEmpty(this.bean.getPreTime().getValue())) {
			ZkUtil.showError("上次发布时间必填必填", "提示信息");
			return;
		}
		if (StrUtil.isNullOrEmpty(this.bean.getLastTime().getValue())) {
			ZkUtil.showError("最后发布时间必填必填", "提示信息");
			return;
		}
		if (this.bean.getPreTime().getValue().after(
				this.bean.getLastTime().getValue())) {
			ZkUtil.showError("最后发布时间必须大于上一次发布时间", "提示信息");
			return;
		}
		OrgTree orgTree = null;
		if ("add".equals(opType)) {
			orgTree = new OrgTree();
		} else if ("mod".endsWith(opType)) {
			orgTree = currentOrgTree;
		}

		orgTree.setOrgTreeName(this.bean.getOrgTreeName().getValue());
		orgTree.setPreTime(this.bean.getPreTime().getValue());
		orgTree.setLastTime(this.bean.getLastTime().getValue());
		if (!StrUtil.isNullOrEmpty(this.bean.getISCalc().getSelectedItem()
				.getValue())) {
			orgTree.setISCalc(new Integer(this.bean.getISCalc()
					.getSelectedItem().getValue().toString()));
		}
		if ("add".equals(opType)) {
			orgTree.setIsPublishing(SystemConfigConstant.NOT_PUBLISHING);
			this.orgTreeManager.addOrgTree(orgTree);
		} else {
			this.orgTreeManager.updateOrgTree(orgTree);
		}
		this.bean.getOrgTreeEditWin().onClose();
		Events.postEvent("onOK", this.self, orgTree);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getOrgTreeEditWin().onClose();
	}
}
