package cn.ffcs.uom.systemconfig.action;

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
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOrtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeStaffOrEditBean;

@Controller
@Scope("prototype")
public class TreeStaffOrEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeStaffOrEditBean bean = new TreeStaffOrEditBean();
	/**
	 * manager
	 */
	private TreeStaffOrtRuleManager treeStaffOrtRuleManager = (TreeStaffOrtRuleManager) ApplicationContextUtil
			.getBean("treeStaffOrtRuleManager");

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
	public void onCreate$treeStaffOrEditWin() throws Exception {
		currentOrgTree = (OrgTree) arg.get("orgTree");
		if (currentOrgTree != null) {
			this.bindBean();
		} else {
			ZkUtil.showError("参数错误", "提示信息");
			this.onCancel();
		}

	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		this.bean.getTreeStaffOrEditWin().setTitle("新增员工推导,组织关系规则");
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		List<String> relaCdlist = this.bean.getRelaCdBandBox().getRelaCdList();
		if (relaCdlist == null || relaCdlist.size() <= 0) {
			ZkUtil.showError("组织关系必填", "提示信息");
			return;
		}
		if (currentOrgTree != null && currentOrgTree.getOrgTreeId() != null) {
			List<TreeStaffOrtRule> treeStaffOrtRuleList = new ArrayList<TreeStaffOrtRule>();
			List<TreeStaffOrtRule> exitsList = currentOrgTree
					.getTreeStaffOrgRelaList();
			for (String str : relaCdlist) {
				/**
				 * 重复判断
				 */
				if (exitsList != null && exitsList.size() > 0) {
					for (TreeStaffOrtRule tsor : exitsList) {
						if (tsor.getRefOrgRelaCd().equals(str)) {
							ZkUtil.showError("存在重复配置,请确认", "提示信息");
							return;
						}
					}
				}
				TreeStaffOrtRule temp = new TreeStaffOrtRule();
				temp.setOrgTreeId(this.currentOrgTree.getOrgTreeId());
				temp.setRefOrgRelaCd(str);
				treeStaffOrtRuleList.add(temp);
			}
			if (treeStaffOrtRuleList.size() > 0) {
				treeStaffOrtRuleManager
						.addTreeStaffOrRuleList(treeStaffOrtRuleList);
			}
		}
		this.bean.getTreeStaffOrEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeStaffOrEditWin().onClose();
	}
}
