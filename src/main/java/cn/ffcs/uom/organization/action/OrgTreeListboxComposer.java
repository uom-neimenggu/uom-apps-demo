package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.OrgTreeListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class OrgTreeListboxComposer extends Div implements IdSpace {

	public OrgTreeListExtBean bean = new OrgTreeListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/organization/comp/org_tree_listbox.zul";

	private OrgTreeManager orgTreeManager = (OrgTreeManager) ApplicationContextUtil
			.getBean("orgTreeManager");

	/**
	 * 查询时存放条件使用
	 */
	private OrgTree queryOrgTree = new OrgTree();

	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;

	/**
	 * 构造方法
	 */
	public OrgTreeListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		onOrgTreeListboxPaging();
	}

	/**
	 * 选中组织树
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrgTreeSelect() throws Exception {
		if (this.bean.getOrgTreeListBox().getSelectedCount() > 0) {
			orgTree = (OrgTree) this.bean.getOrgTreeListBox().getSelectedItem()
					.getValue();
			if (orgTree != null) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_REQUEST, this,orgTree);
			}
		}
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onOrgTreeListboxPaging() throws Exception {
		PageInfo pageInfo = this.orgTreeManager.queryOrgTreePageInfoByOrgTree(
				queryOrgTree,
				this.bean.getOrgTreeListPaging().getActivePage() + 1, this.bean
						.getOrgTreeListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getOrgTreeListBox().setModel(dataList);
		this.bean.getOrgTreeListPaging().setTotalSize(pageInfo.getTotalCount());
	}

}
