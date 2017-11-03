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
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgTypeRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeOrgOtEditBean;

@Controller
@Scope("prototype")
public class TreeOrgOtEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeOrgOtEditBean bean = new TreeOrgOtEditBean();
	/**
	 * manager
	 */
	private TreeOrgTypeRuleManager treeOrgTypeRuleManager = (TreeOrgTypeRuleManager) ApplicationContextUtil
			.getBean("treeOrgTypeRuleManager");

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
	public void onCreate$treeOrgOtEditWin() throws Exception {
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
		this.bean.getTreeOrgOtEditWin().setTitle("新增组织推导,组织类型规则");
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
		List<TreeOrgTypeRule> treeOrgTypeRuleList = new ArrayList<TreeOrgTypeRule>();
		List<TreeOrgTypeRule> existList = currentOrgTree.getTreeOrgOrgTypeList();
		if (currentOrgTree != null && currentOrgTree.getOrgTreeId() != null) {
			for (Pair<String, String> pair : orgTypeCdList) {
				/**
				 * 重复判断
				 */
				if (existList != null && existList.size() > 0) {
					for (TreeOrgTypeRule totr : existList) {
						if (totr.getRefTypeValue().equals(pair.getLeft())) {
							ZkUtil.showError("该配置已存在,请确认", "提示信息");
							return;
						}
					}
				}
				TreeOrgTypeRule temp = new TreeOrgTypeRule();
				temp.setOrgTreeId(currentOrgTree.getOrgTreeId());
				temp.setRefTypeValue(pair.getLeft());
				treeOrgTypeRuleList.add(temp);
			}
		}
		if (treeOrgTypeRuleList != null && treeOrgTypeRuleList.size() > 0) {
			this.treeOrgTypeRuleManager
					.addTreeOrgTypeRuleList(treeOrgTypeRuleList);
		}
		this.bean.getTreeOrgOtEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeOrgOtEditWin().onClose();
	}
}
