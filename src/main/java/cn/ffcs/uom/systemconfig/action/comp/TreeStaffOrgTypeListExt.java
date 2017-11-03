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
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.TreeStaffOrgTypeListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class TreeStaffOrgTypeListExt extends Div implements IdSpace {

	public TreeStaffOrgTypeListExtBean bean = new TreeStaffOrgTypeListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/tree_staff_ot_rule_list_ext.zul";

	private TreeStaffOtRuleManager treeStaffOtRuleManager = (TreeStaffOtRuleManager) ApplicationContextUtil
			.getBean("treeStaffOtRuleManager");

	/**
	 * 查询时存放条件使用
	 */
	private TreeStaffOtRule queryTreeStaffOtRule;

	/**
	 * 选中
	 */
	private TreeStaffOtRule treeStaffOtRule;
	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	/**
	 * 构造方法
	 */
	public TreeStaffOrgTypeListExt() {
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
	public void onQueryTreeStaffOrgType() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			queryTreeStaffOtRule = new TreeStaffOtRule();
			queryTreeStaffOtRule.setOrgTreeId(orgTree.getOrgTreeId());
			this.setButtonValid(true, false);
			this.onTreeStaffOtRuleListboxPaging();
		}
	}

	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onTreeStaffOtRuleSelect() throws Exception {
		if (this.bean.getTreeStaffOtRuleListBox().getSelectedCount() > 0) {
			treeStaffOtRule = (TreeStaffOtRule) this.bean
					.getTreeStaffOtRuleListBox().getSelectedItem().getValue();
			if (treeStaffOtRule != null) {
				this.setButtonValid(true, true);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onTreeStaffOtRuleListboxPaging() throws Exception {
		PageInfo pageInfo = this.treeStaffOtRuleManager
				.queryTreeStaffOtRulePageInfo(queryTreeStaffOtRule, this.bean
						.getTreeStaffOtRuleListPaging().getActivePage() + 1,
						this.bean.getTreeStaffOtRuleListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getTreeStaffOtRuleListBox().setModel(dataList);
		this.bean.getTreeStaffOtRuleListPaging().setTotalSize(
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
		this.bean.getAddTreeStaffOtRuleButton().setDisabled(!canAdd);
		this.bean.getDelTreeStaffOtRuleButton().setDisabled(!canDel);
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
			this.onQueryTreeStaffOrgType();
		}
	}

	/**
	 * 清空列表
	 */
	public void clearListBox() {
		ListboxUtils.clearListbox(this.bean.getTreeStaffOtRuleListBox());
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onTreeStaffOtRuleDel() throws Exception {
		if (treeStaffOtRule != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						treeStaffOtRuleManager
								.removeTreeStaffOtRule(treeStaffOtRule);
						PubUtil.reDisplayListbox(bean
								.getTreeStaffOtRuleListBox(), treeStaffOtRule,
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
	public void onTreeStaffOtRuleAdd() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			Map arg = new HashMap();
			arg.put("orgTree", orgTree);
			Window win = (Window) Executions.createComponents(
					"/pages/system_config/tree_staff_ot_edit.zul", this, arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQueryTreeStaffOrgType();
					if (event.getData() != null) {

					}
				}
			});
		}
	}
}
