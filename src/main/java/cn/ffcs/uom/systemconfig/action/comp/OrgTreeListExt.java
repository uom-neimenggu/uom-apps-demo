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
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.ServerConfigUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.systemconfig.action.bean.comp.OrgTreeListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class OrgTreeListExt extends Div implements IdSpace {

	public OrgTreeListExtBean bean = new OrgTreeListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/org_tree_list_ext.zul";

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
	public OrgTreeListExt() {
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
		this.setButtonValid(true, false, false, false, false);
		this.setButtonVisible();
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
				this.setButtonValid(true, true, true, true, true);
				Events.postEvent(
						SystemConfigConstant.ON_ORG_TREE_SELECT_REQUEST, this,
						orgTree);
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

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canEdit, boolean canDel,
			boolean canFtpIncreaseTimer, boolean canFtpAllTimer) {
		this.bean.getAddOrgTreeButton().setDisabled(!canAdd);
		this.bean.getEditOrgTreeButton().setDisabled(!canEdit);
		this.bean.getDelOrgTreeButton().setDisabled(!canDel);
		this.bean.getFtpIncreaseTimerSwitchButton().setDisabled(
				!canFtpIncreaseTimer);
		this.bean.getFtpAllTimerSwitchButton().setDisabled(!canFtpAllTimer);
	}

	/**
	 * 设置按钮显示
	 */
	public void setButtonVisible() {
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;
		boolean canFtpIncreaseTimer = false;
		boolean canFtpAllTimer = false;

		// 当前server下的COMPONENT_SHOW用户变量未配置或者不为true
		if (ServerConfigUtil.isComponentShow()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
			canFtpIncreaseTimer = true;
			canFtpAllTimer = true;
		}

		this.bean.getAddOrgTreeButton().setVisible(canAdd);
		this.bean.getEditOrgTreeButton().setVisible(canEdit);
		this.bean.getDelOrgTreeButton().setVisible(canDel);
		this.bean.getFtpIncreaseTimerSwitchButton().setVisible(
				canFtpIncreaseTimer);
		this.bean.getFtpAllTimerSwitchButton().setVisible(canFtpAllTimer);
	}

	/**
	 * 组织信息删除
	 * 
	 * @throws Exception
	 */
	public void onOrgTreeDel() throws Exception {
		if (orgTree != null) {
			ZkUtil.showQuestion("删除组织信息将级联删除该树的推倒配置,你确定要删除该组织树吗?", "提示信息",
					new EventListener() {
						@Override
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								orgTreeManager.removeOrgTree(orgTree);
								PubUtil.reDisplayListbox(
										bean.getOrgTreeListBox(), orgTree,
										"del");
								Events.postEvent(
										SystemConfigConstant.ON_DEL_ORG_TREE_REQUEST,
										OrgTreeListExt.this, orgTree);
							}
						}
					});
		} else {
			ZkUtil.showError("请选择你要删除组织树", "提示信息");
			return;
		}
	}

	/**
	 * 组织信息新增
	 * 
	 * @throws Exception
	 */
	public void onOrgTreeAdd() throws Exception {
		this.openOrgTreeEditWin("add");
	}

	/**
	 * 组织信息修改
	 * 
	 * @throws Exception
	 */
	public void onOrgTreeEdit() throws Exception {
		this.openOrgTreeEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openOrgTreeEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {

		} else if ("mod".equals(type)) {
			arg.put("orgTree", orgTree);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/org_tree_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					OrgTree ot = (OrgTree) event.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getOrgTreeListBox(), ot,
								"add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(bean.getOrgTreeListBox(), ot,
								"mod");
						orgTree = ot;
					}
				}
			}
		});
	}

	/**
	 * FTP增量定时开关
	 * 
	 */
	public void onOrgTreeFtpIncreaseTimer() throws Exception {
		this.openOrgTreeSwitchEditWin("ftpIncreaseTimer");
	}

	/**
	 * FTP全量定时开关
	 * 
	 */
	public void onOrgTreeFtpAllTimer() throws Exception {
		this.openOrgTreeSwitchEditWin("ftpAllTimer");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openOrgTreeSwitchEditWin(String opType) throws Exception {
		Map arg = new HashMap();
		this.opType = opType;
		arg.put("opType", opType);
		arg.put("orgTree", orgTree);
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/business_system_switch_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					ZkUtil.showInformation("操作成功！", "提示信息");
				} else {
					ZkUtil.showError("操作失败！", "提示信息");
				}
			}
		});
	}

}
