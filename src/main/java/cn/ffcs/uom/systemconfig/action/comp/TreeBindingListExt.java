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
import cn.ffcs.uom.orgTreeCalc.manager.TreeBindingRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.TreeBindingListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class TreeBindingListExt extends Div implements IdSpace {

	public TreeBindingListExtBean bean = new TreeBindingListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/tree_binding_list_ext.zul";
	/**
	 * 选中的
	 */
	private TreeBindingRule treeBindingRule;
	/**
	 * 查询条件
	 */
	private TreeBindingRule queryTreeBinding;
	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	private TreeBindingRuleManager treeBindingRuleManager = (TreeBindingRuleManager) ApplicationContextUtil.getBean("treeBindingRuleManager");

	/**
	 * 构造方法
	 */
	public TreeBindingListExt() {
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
	public void onQueryTreeBinding() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			queryTreeBinding = new TreeBindingRule();
			queryTreeBinding.setOrgTreeId(orgTree.getOrgTreeId());
			this.setButtonValid(true, false);
			this.onTreeBindingListboxPaging();
		}
	}

	/**
	 * 选中
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onTreeBindingSelect() throws Exception {
		if (this.bean.getTreeBindingListBox().getSelectedCount() > 0) {
			treeBindingRule = (TreeBindingRule) this.bean.getTreeBindingListBox().getSelectedItem().getValue();
			if (treeBindingRule != null) {
				this.setButtonValid(true, true);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onTreeBindingListboxPaging() throws Exception {
		PageInfo pageInfo = this.treeBindingRuleManager.queryTreeBindingRulePageInfo(queryTreeBinding, this.bean.getTreeBindingListPaging().getActivePage() + 1, this.bean.getTreeBindingListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),true);
		this.bean.getTreeBindingListBox().setModel(dataList);
		this.bean.getTreeBindingListPaging().setTotalSize(pageInfo.getTotalCount());
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canDel) {
		this.bean.getAddTreeBindingButton().setDisabled(!canAdd);
		this.bean.getDelTreeBindingButton().setDisabled(!canDel);
	}

	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrgTreeSelectCallQueryResponse(ForwardEvent event)
			throws Exception {
		if (event != null && event.getOrigin() != null && event.getOrigin().getData() != null) {
			orgTree = (OrgTree) event.getOrigin().getData();
			this.onQueryTreeBinding();
		}
	}

	/**
	 * 清空列表
	 */
	public void clearListBox() {
		ListboxUtils.clearListbox(this.bean.getTreeBindingListBox());
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void onTreeBindingDel() throws Exception {
		if (treeBindingRule != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						treeBindingRuleManager.removeTreeBindingRule(treeBindingRule);
						PubUtil.reDisplayListbox(bean.getTreeBindingListBox(),treeBindingRule, "del");
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
	public void onTreeBindingAdd() throws Exception {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			Map arg = new HashMap();
			arg.put("orgTree", orgTree);
			Window win = (Window) Executions.createComponents("/pages/system_config/tree_binding_edit.zul", this, arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					onQueryTreeBinding();
					if (event.getData() != null) {
						
					}
				}
			});
		}
	}
}
