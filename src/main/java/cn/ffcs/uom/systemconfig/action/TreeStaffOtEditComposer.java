package cn.ffcs.uom.systemconfig.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeStaffOtEditBean;

@Controller
@Scope("prototype")
public class TreeStaffOtEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeStaffOtEditBean bean = new TreeStaffOtEditBean();
	/**
	 * manager
	 */
	private TreeStaffOtRuleManager treeStaffOtRuleManager = (TreeStaffOtRuleManager) ApplicationContextUtil
			.getBean("treeStaffOtRuleManager");

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
	public void onCreate$treeStaffOtEditWin() throws Exception {
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
		this.bean.getTreeStaffOtEditWin().setTitle("新增员工推导,组织类型规则");
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		List<Pair<String, String>> orgTypeCdList = this.bean.getOrgTypeCd()
				.getResultArr();
		if (orgTypeCdList == null || orgTypeCdList.size() <= 0) {
			ZkUtil.showError("组织类型必填", "提示信息");
			return;
		}
		List<TreeStaffOtRule> treeStaffOtRuleList = new ArrayList<TreeStaffOtRule>();
		List<TreeStaffOtRule> existList = currentOrgTree.getTreeStaffOrgTypeList();
		if (currentOrgTree != null && currentOrgTree.getOrgTreeId() != null) {
			for (Pair<String, String> pair : orgTypeCdList) {
				/**
				 * 重复判断
				 */
				if (existList != null && existList.size() > 0) {
					for (TreeStaffOtRule tsor : existList) {
						if (tsor.getRefTypeValue().equals(pair.getLeft())) {
							ZkUtil.showError("存在重复配置,请确认", "提示信息");
							return;
						}
					}
				}
				TreeStaffOtRule temp = new TreeStaffOtRule();
				temp.setOrgTreeId(currentOrgTree.getOrgTreeId());
				temp.setRefTypeValue(pair.getLeft());
				treeStaffOtRuleList.add(temp);
			}
		}
		if (treeStaffOtRuleList != null && treeStaffOtRuleList.size() > 0) {
			this.treeStaffOtRuleManager
					.addTreeStaffOtRuleList(treeStaffOtRuleList);
		}
		this.bean.getTreeStaffOtEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeStaffOtEditWin().onClose();
	}
}
