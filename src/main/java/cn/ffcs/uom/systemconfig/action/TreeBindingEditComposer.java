package cn.ffcs.uom.systemconfig.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.orgTreeCalc.manager.TreeBindingRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.TreeBindingEditBean;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;

@Controller
@Scope("prototype")
public class TreeBindingEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private TreeBindingEditBean bean = new TreeBindingEditBean();

	/**
	 * 当前组织树信息
	 */
	private OrgTree currentOrgTree;
	
	@Resource(name = "orgTreeConfigManager")
	public OrgTreeConfigManager orgTreeConfigManager;
	
	@Resource(name = "treeBindingRuleManager")
	public TreeBindingRuleManager treeBindingRuleManager;

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
	public void onCreate$treeBindingEditWin() throws Exception {
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
		this.bean.getTreeBindingEditWin().setTitle("组织树绑定");
		orgTreeConfigManager.loadOrgTreeRootNode(bean.getTreeListbox());
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
		Listitem listitem = bean.getTreeListbox().getSelectedItem();
		if (listitem.getValue() != null){
			String[] val = listitem.getValue().toString().split("&");
			TreeBindingRule treeBindingRule = new TreeBindingRule();
			treeBindingRule.setOrgTreeId(this.currentOrgTree.getOrgTreeId());
			treeBindingRule.setRefTreeId(Long.valueOf(val[0]));
			treeBindingRule.setRefOrgRelaCd(val[1]);
			treeBindingRule.setRefTreeName(listitem.getLabel());
			/**
			 * 重复判断
			 */
			if (treeBindingRuleManager.checkTreeBindingRuleIsExist(treeBindingRule)) {
				ZkUtil.showError("存在重复配置,请确认!", "提示信息");
				return;
			}
			treeBindingRuleManager.addTreeBindingRule(treeBindingRule);
		}else{
			ZkUtil.showError("请选择组织树！", "提示信息");
		}
		onCancel();
		Events.postEvent("onOK", this.self, null);
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getTreeBindingEditWin().onClose();
	}
}
