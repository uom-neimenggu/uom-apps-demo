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
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffSftRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeStaffSftEditBean;

@Controller
@Scope("prototype")
public class TreeStaffSftEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeStaffSftEditBean bean = new TreeStaffSftEditBean();
	/**
	 * manager
	 */
	private TreeStaffSftRuleManager treeStaffSftRuleManager = (TreeStaffSftRuleManager) ApplicationContextUtil
			.getBean("treeStaffSftRuleManager");

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
	public void onCreate$treeStaffSftEditWin() throws Exception {
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
		this.bean.getTreeStaffSftEditWin().setTitle("新增员工推导,员工类型规则");
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		List<Pair<String, String>> staffTypeCdList = this.bean.getWorkProp()
				.getResultArr();
		if (staffTypeCdList == null || staffTypeCdList.size() <= 0) {
			ZkUtil.showError("组织类型必填", "提示信息");
			return;
		}
		List<TreeStaffSftRule> treeStaffSftRuleList = new ArrayList<TreeStaffSftRule>();
		List<TreeStaffSftRule> existList = currentOrgTree
				.getTreeStaffStaffTypeList();
		if (currentOrgTree != null && currentOrgTree.getOrgTreeId() != null) {
			for (Pair<String, String> pair : staffTypeCdList) {
				/**
				 * 重复判断
				 */
				if (existList != null && existList.size() > 0) {
					for (TreeStaffSftRule tssftr : existList) {
						if (tssftr.getRefStaffTypeCd().equals(pair.getLeft())) {
							ZkUtil.showError("该配置已存在,请确认", "提示信息");
							return;
						}
					}
				}
				TreeStaffSftRule temp = new TreeStaffSftRule();
				temp.setOrgTreeId(currentOrgTree.getOrgTreeId());
				temp.setRefStaffTypeCd(pair.getLeft());
				treeStaffSftRuleList.add(temp);
			}
		}
		if (treeStaffSftRuleList != null && treeStaffSftRuleList.size() > 0) {
			this.treeStaffSftRuleManager
					.addTreeStaffSftRule(treeStaffSftRuleList);
		}
		this.bean.getTreeStaffSftEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeStaffSftEditWin().onClose();
	}
}
