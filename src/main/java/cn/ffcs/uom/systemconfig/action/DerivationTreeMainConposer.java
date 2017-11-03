package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.DerivationTreeMainBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class DerivationTreeMainConposer extends BasePortletComposer {

	private DerivationTreeMainBean bean = new DerivationTreeMainBean();

	/**
	 * 选中的组织树信息
	 */
	private OrgTree orgTree;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getOrgTreeListExt().addForward(
				SystemConfigConstant.ON_ORG_TREE_SELECT_REQUEST, comp,
				SystemConfigConstant.ON_ORG_TREE_SELECT_RESOPONSE);
		this.bean.getOrgTreeListExt().addForward(
				SystemConfigConstant.ON_DEL_ORG_TREE_REQUEST, comp,
				SystemConfigConstant.ON_DEL_ORG_TREE_RESPONSE);
	}

	/**
	 * 创建
	 * 
	 * @throws Exception
	 */
	public void onCreate$orgTreeMainWin() throws Exception {

	}

	/**
	 * 选中组织树
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onOrgTreeSelectResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			OrgTree orgTree = (OrgTree) event.getOrigin().getData();
			if (orgTree != null) {
				this.orgTree = orgTree;
				this.callTab();
			}
		}
	}

	/**
	 * 点击删除组织树信息
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onDelOrgTreeResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			OrgTree orgTree = (OrgTree) event.getOrigin().getData();
			if (orgTree != null) {
				this.bean.getTreeBindingListExt().clearListBox();
				this.bean.getTreeOrgOrgRelaListExt().clearListBox();
				this.bean.getTreeOrgOrgTypeListExt().clearListBox();
				this.bean.getTreeStaffOrgRelaListExt().clearListBox();
				this.bean.getTreeStaffOrgTypeListExt().clearListBox();
				this.bean.getTreeStaffStaffTypeListExt().clearListBox();
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabbox().getSelectedTab());
		}
		if (orgTree != null) {
			if ("treeBindingTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeBindingListExt(), orgTree);
			} else if ("treeOrgOrgRelaTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeOrgOrgRelaListExt(), orgTree);
			} else if ("treeOrgOrgTypeTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeOrgOrgTypeListExt(), orgTree);
			} else if ("treeStaffOrgTypeTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeStaffOrgTypeListExt(), orgTree);
			} else if ("treeStaffStaffTypeTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeStaffStaffTypeListExt(),orgTree);
			} else if ("treeStaffOrgRelaTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(SystemConfigConstant.ON_ORG_TREE_SELECT_CALL_QUERY_REQUEST,this.bean.getTreeStaffOrgRelaListExt(), orgTree);
			}
		}
	}

	/**
	 * 点击tab
	 * 
	 * @throws Exception
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setSelectTab(clickTab);
				callTab();
			}
		}
	}

}
