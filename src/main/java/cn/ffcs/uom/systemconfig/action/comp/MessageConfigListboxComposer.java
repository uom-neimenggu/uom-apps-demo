package cn.ffcs.uom.systemconfig.action.comp;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.MessageConfigListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemConfigUser;

@Controller
@Scope("prototype")
public class MessageConfigListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageConfigListExtBean bean = new MessageConfigListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/message_config_listbox.zul";

	private MessageConfigManager messageConfigManager = (MessageConfigManager) ApplicationContextUtil
			.getBean("messageConfigManager");

	/**
	 * 查询时存放条件
	 */
	private SystemConfigUser querySystemMessageConfig;

	/**
	 * 选中业务系统配置信息
	 */
	private SystemConfigUser systemMessageConfig;

	/**
	 * 操作类型
	 */
	private String opType;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 构造方法
	 */
	public MessageConfigListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
				this,
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_RESPONSE);
		
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onReset() throws Exception {
		cn.ffcs.uom.common.util.PubUtil.fillBeanFromPo(new SystemConfigUser(), this.bean);
	}

	/**
	 * 查询用户列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onQuery() throws Exception {
		this.bean.getSystemMessageConfigListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onMessageConfigListboxPaging();
		querySystemMessageConfig = null;
		
		/**
		 * 抛出系统配置人员组件刷新事件
		 */
		Events.postEvent(SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, this, null);
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(false, false, false);
	}

	/**
	 * 利用查询条件进行查询
	 */
	public void onQuerySystemMessageConfig() throws Exception {
		this.bean.getSystemMessageConfigListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onMessageConfigListboxPaging();
		querySystemMessageConfig = null;
	}

	/**
	 * 选中系统用户
	 */
	public void onSystemMessageConfigSelect() throws Exception {
		if (this.bean.getSystemMessageConfigListBox().getSelectedCount() > 0) {
			systemMessageConfig = (SystemConfigUser) this.bean
					.getSystemMessageConfigListBox().getSelectedItem()
					.getValue();
			if (systemMessageConfig != null) {
				this.setButtonValid(true, true, true);
				
				/**
				 * 抛出人员选择事件
				 */
				Events.postEvent(SystemConfigConstant.ON_SELECT_SYSTEM_CONFIG_USER, this,
						systemMessageConfig);
			}
		} else {
			/**
			 * 失去选中，抛出系统配置人员组件刷新事件
			 */
			Events.postEvent(SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, this, null);
		}
	}

	/**
	 * 分页查询
	 */
	public void onMessageConfigListboxPaging() throws Exception {
		querySystemMessageConfig = new SystemConfigUser();
		PubUtil.fillPoFromBean(bean, querySystemMessageConfig);

		PageInfo pageInfo = this.messageConfigManager
				.queryMessageConfigPageInfo(querySystemMessageConfig,
						this.bean.getSystemMessageConfigListPaging()
						.getActivePage() + 1, this.bean
						.getSystemMessageConfigListPaging()
						.getPageSize());
		ListModel dataList = new BindingListModelList(
				pageInfo.getDataList(), true);
		this.bean.getSystemMessageConfigListBox().setModel(dataList);
		this.bean.getSystemMessageConfigListPaging().setTotalSize(
				pageInfo.getTotalCount());
		
		/**
		 * 抛出系统配置人员组件刷新事件
		 */
		Events.postEvent(SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, this, null);
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canEdit, boolean canDel) {
		this.bean.getAddSystemMessageConfigButton().setDisabled(!canAdd);
		this.bean.getEditSystemMessageConfigButton().setDisabled(!canEdit);
		this.bean.getDelSystemMessageConfigButton().setDisabled(!canDel);
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemMessageConfigDel() throws Exception {

		if (systemMessageConfig != null) {
			ZkUtil.showQuestion("你确定要删除该人员吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						messageConfigManager
						.removeMessageConfig(systemMessageConfig);
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemMessageConfig, "del");
					}
				}
			});
			
			/**
			 * 抛出系统配置人员组件刷新事件
			 */
			Events.postEvent(SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, this, null);
		} else {
			ZkUtil.showError("请选择你要删除的配置", "提示信息");
			return;
		}
	}

	/**
	 * 新增业务系统
	 * 
	 * @throws Exception
	 */
	public void onMessageConfigAdd() throws Exception {
		this.openSystemMessageConfigEditWin("add");
	}

	/**
	 * 修改业务系统
	 * 
	 * @throws Exception
	 */
	public void onSystemMessageConfigEdit() throws Exception {

		this.openSystemMessageConfigEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openSystemMessageConfigEditWin(String type) throws Exception {
		Map<String, Object> arg = new HashMap<String, Object>();
		this.opType = type;
		arg.put("opType", opType);

		if ("mod".equals(type)) {
			arg.put("systemMessageConfig", systemMessageConfig);
		}

		Window win = (Window) Executions.createComponents(
				"/pages/system_config/message_config_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					SystemConfigUser systemIntfInfoConfigNew = (SystemConfigUser) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemIntfInfoConfigNew, "add");
						onSystemMessageConfigSelect();
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemIntfInfoConfigNew, "mod");
						systemMessageConfig = systemIntfInfoConfigNew;
						onSystemMessageConfigSelect();
					}
					
				}
			}
		});
		
		
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
		} else if ("messageConfigPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MESSAGE_CONFIG_USER_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MESSAGE_CONFIG_USER_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MESSAGE_CONFIG_USER_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddSystemMessageConfigButton().setVisible(canAdd);
		this.bean.getEditSystemMessageConfigButton().setVisible(canEdit);
		this.bean.getDelSystemMessageConfigButton().setVisible(canDel);
	}
}
