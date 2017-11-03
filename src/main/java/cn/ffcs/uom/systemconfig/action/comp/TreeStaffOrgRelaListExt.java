package cn.ffcs.uom.systemconfig.action.comp;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

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
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOrtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.TreeStaffOrgRelaListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class TreeStaffOrgRelaListExt extends Div implements IdSpace {

	public TreeStaffOrgRelaListExtBean bean = new TreeStaffOrgRelaListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/tree_staff_or_rule_list_ext.zul";

	private TreeStaffOrtRuleManager treeStaffOrRuleManager = (TreeStaffOrtRuleManager) ApplicationContextUtil
			.getBean("treeStaffOrtRuleManager");

	/**
	 * 查询时存放条件使用
	 */
	private TreeStaffOrtRule queryTreeStaffOrRule;

	/**
	 * 选中
	 */
	private TreeStaffOrtRule treeStaffOrRule;
	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	/**
	 * 构造方法
	 */
	public TreeStaffOrgRelaListExt() {
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
	public void onQueryTreeStaffOrgRela() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			queryTreeStaffOrRule = new TreeStaffOrtRule();
			queryTreeStaffOrRule.setOrgTreeId(orgTree.getOrgTreeId());
			this.setButtonValid(true, false);
			this.onTreeStaffOrRuleListboxPaging();
		}
	}

	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onTreeStaffOrRuleSelect() throws Exception {
		if (this.bean.getTreeStaffOrRuleListBox().getSelectedCount() > 0) {
			treeStaffOrRule = (TreeStaffOrtRule) this.bean
					.getTreeStaffOrRuleListBox().getSelectedItem().getValue();
			if (treeStaffOrRule != null) {
				this.setButtonValid(true, true);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onTreeStaffOrRuleListboxPaging() throws Exception {
		PageInfo pageInfo = this.treeStaffOrRuleManager
				.queryTreeStaffOrRulePageInfo(queryTreeStaffOrRule, this.bean
						.getTreeStaffOrRuleListPaging().getActivePage() + 1,
						this.bean.getTreeStaffOrRuleListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getTreeStaffOrRuleListBox().setModel(dataList);
		this.bean.getTreeStaffOrRuleListPaging().setTotalSize(
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
		this.bean.getAddTreeStaffOrRuleButton().setDisabled(!canAdd);
		this.bean.getDelTreeStaffOrRuleButton().setDisabled(!canDel);
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
			this.onQueryTreeStaffOrgRela();
		}
	}

	/**
	 * 清空列表
	 */
	public void clearListBox() {
		ListboxUtils.clearListbox(this.bean.getTreeStaffOrRuleListBox());
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onTreeStaffOrRuleDel() throws Exception {
		if (treeStaffOrRule != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						treeStaffOrRuleManager
								.removeTreeStaffOrRule(treeStaffOrRule);
						PubUtil.reDisplayListbox(bean
								.getTreeStaffOrRuleListBox(), treeStaffOrRule,
								"del");
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
	public void onTreeStaffOrRuleAdd() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			Map arg = new HashMap();
			arg.put("orgTree", orgTree);
			Window win = (Window) Executions.createComponents(
					"/pages/system_config/tree_staff_or_edit.zul", this, arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQueryTreeStaffOrgRela();
					if (event.getData() != null) {

					}
				}
			});
		}
	}

}
