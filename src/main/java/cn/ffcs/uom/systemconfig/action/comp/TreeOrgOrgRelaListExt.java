package cn.ffcs.uom.systemconfig.action.comp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaTypeRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.TreeOrgOrgRelaListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class TreeOrgOrgRelaListExt extends Div implements IdSpace {

	public TreeOrgOrgRelaListExtBean bean = new TreeOrgOrgRelaListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/tree_org_or_rule_list_ext.zul";
	/**
	 * 选中的
	 */
	private TreeOrgRelaTypeRule treeOrgRelaTypeRule;
	/**
	 * 查询条件
	 */
	private TreeOrgRelaTypeRule queryTreeOrgOrRule;
	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	private TreeOrgRelaTypeRuleManager treeOrgRelaTypeRuleManager = (TreeOrgRelaTypeRuleManager) ApplicationContextUtil
			.getBean("treeOrgRelaTypeRuleManager");

	/**
	 * 构造方法
	 */
	public TreeOrgOrgRelaListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(
				SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,
				this,
				SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_RESPONSE);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false);
	}

	/**
	 * 查询
	 * 
	 * @throws Exception
	 */
	public void onQueryTreeOrgOrgRela() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			queryTreeOrgOrRule = new TreeOrgRelaTypeRule();
			queryTreeOrgOrRule.setOrgTreeId(orgTree.getOrgTreeId());
			this.setButtonValid(true, false);
			this.onTreeOrgOrRuleListboxPaging();
		}
	}

	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onTreeOrgOrRuleSelect() throws Exception {
		if (this.bean.getTreeOrgOrRuleListBox().getSelectedCount() > 0) {
			treeOrgRelaTypeRule = (TreeOrgRelaTypeRule) this.bean
					.getTreeOrgOrRuleListBox().getSelectedItem().getValue();
			if (treeOrgRelaTypeRule != null) {
				this.setButtonValid(true, true);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onTreeOrgOrRuleListboxPaging() throws Exception {
		PageInfo pageInfo = this.treeOrgRelaTypeRuleManager
				.queryTreeOrgOrgRelaRulePageInfo(queryTreeOrgOrRule, this.bean
						.getTreeOrgOrRuleListPaging().getActivePage() + 1,
						this.bean.getTreeOrgOrRuleListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getTreeOrgOrRuleListBox().setModel(dataList);
		this.bean.getTreeOrgOrRuleListPaging().setTotalSize(
				pageInfo.getTotalCount());
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canDel) {
		this.bean.getAddTreeOrgOrRuleButton().setDisabled(!canAdd);
		this.bean.getDelTreeOrgOrRuleButton().setDisabled(!canDel);
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrgTreeSelectCallQueryResponse(ForwardEvent event)
			throws Exception {
		if (event != null && event.getOrigin() != null
				&& event.getOrigin().getData() != null) {
			orgTree = (OrgTree) event.getOrigin().getData();
			this.onQueryTreeOrgOrgRela();
		}
	}

	/**
	 * 清空列表
	 */
	public void clearListBox() {
		ListboxUtils.clearListbox(this.bean.getTreeOrgOrRuleListBox());
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onTreeOrgOrRuleDel() throws Exception {
		if (treeOrgRelaTypeRule != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						treeOrgRelaTypeRuleManager
								.removeTreeOrgRelaTypeRule(treeOrgRelaTypeRule);
						PubUtil.reDisplayListbox(
								bean.getTreeOrgOrRuleListBox(),
								treeOrgRelaTypeRule, "del");
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的配置", "提示信息");
			return;
		}
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void onTreeOrgOrRuleAdd() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			Map arg = new HashMap();
			arg.put("orgTree", orgTree);
			Window win = (Window) Executions.createComponents(
					"/pages/system_config/tree_org_or_edit.zul", this, arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQueryTreeOrgOrgRela();
					if (event.getData() != null) {
						
					}
				}
			});
		}
	}
}
