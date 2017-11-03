package cn.ffcs.uom.systemconfig.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaTypeRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeOrgOrEditBean;

@Controller
@Scope("prototype")
public class TreeOrgOrEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeOrgOrEditBean bean = new TreeOrgOrEditBean();
	/**
	 * manager
	 */
	private TreeOrgRelaTypeRuleManager treeOrgRelaTypeRuleManager = (TreeOrgRelaTypeRuleManager) ApplicationContextUtil
			.getBean("treeOrgRelaTypeRuleManager");

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
	public void onCreate$treeOrgOrEditWin() throws Exception {
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
		this.bean.getTreeOrgOrEditWin().setTitle("新增组织推导-组织关系规则");
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
			List<TreeOrgRelaTypeRule> list = new ArrayList<TreeOrgRelaTypeRule>();
			List<TreeOrgRelaTypeRule> exitsList = currentOrgTree
					.getTreeOrgOrgRelaList();
			for (String str : relaCdlist) {
				/**
				 * 重复判断
				 */
				if (exitsList != null && exitsList.size() > 0) {
					for (TreeOrgRelaTypeRule tortr : exitsList) {
						if (tortr.getRefOrgRelaCd().equals(str)) {
							ZkUtil.showError("存在重复配置,请确认", "提示信息");
							return;
						}
					}
				}
				TreeOrgRelaTypeRule temp = new TreeOrgRelaTypeRule();
				temp.setOrgTreeId(this.currentOrgTree.getOrgTreeId());
				temp.setRefOrgRelaCd(str);
				list.add(temp);
			}
			if (list.size() > 0) {
				treeOrgRelaTypeRuleManager.addTreeOrgRelaTypeRuleList(list);
			}
		}
		this.bean.getTreeOrgOrEditWin().onClose();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeOrgOrEditWin().onClose();
	}
}
