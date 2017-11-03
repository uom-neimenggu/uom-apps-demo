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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.SystemMessageConfigListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.SysBusiUserManager;
import cn.ffcs.uom.webservices.manager.MessageConfigManager;
import cn.ffcs.uom.webservices.manager.SystemMessageConfigManager;
import cn.ffcs.uom.webservices.model.SystemBusiUser;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

@Controller
@Scope("prototype")
public class SystemMessageConfigListExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SystemMessageConfigListExtBean bean = new SystemMessageConfigListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/system_message_config_list_ext.zul";

	private SystemMessageConfigManager systemMessageConfigManager = (SystemMessageConfigManager) ApplicationContextUtil
			.getBean("systemMessageConfigManager");
	
	private MessageConfigManager messageConfigManager = (MessageConfigManager) ApplicationContextUtil
			.getBean("messageConfigManager");
	
	private SysBusiUserManager sysBusiUserManager = (SysBusiUserManager) ApplicationContextUtil
			.getBean("sysBusiUserManager");

	/**
	 * 查询时存放条件
	 */
	private SystemMessageConfig querySystemMessageConfig;

	/**
	 * 选中人员信息
	 */
	private SystemConfigUser systemConfigUser;
	/**
	 * 选中的业务系统
	 */
	private BusinessSystem businessSystem;

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
	public SystemMessageConfigListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
				this,
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_RESPONSE);
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
		this.onSystemConfigUserListboxPaging();
		querySystemMessageConfig = null;
	}

	/**
	 * 选中业务系统组织树配置
	 */
	public void onSystemMessageConfigSelect() throws Exception {
		if (this.bean.getSystemMessageConfigListBox().getSelectedCount() > 0) {
			systemConfigUser = (SystemConfigUser) this.bean
					.getSystemMessageConfigListBox().getSelectedItem()
					.getValue();
			if (systemConfigUser != null) {
				this.setButtonValid(true, true, true);
			}
		}
	}

	/**
	 * 分页查询
	 */
	public void onSystemMessageConfigListboxPaging() throws Exception {
		if (businessSystem != null && businessSystem.getSystemCode() != null) {
			querySystemMessageConfig = new SystemMessageConfig();
			querySystemMessageConfig.setSystemCode(businessSystem
					.getSystemCode());

			PageInfo pageInfo = this.systemMessageConfigManager
					.querySystemMessageConfigPageInfo(querySystemMessageConfig,
							this.bean.getSystemMessageConfigListPaging()
									.getActivePage() + 1, this.bean
									.getSystemMessageConfigListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemMessageConfigListBox().setModel(dataList);
			this.bean.getSystemMessageConfigListPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}
	
	/**
	 * 分页查询SystemConfigUser
	 */
	public void onSystemConfigUserListboxPaging() throws Exception {
		if (businessSystem != null && businessSystem.getSystemCode() != null) {
			PageInfo pageInfo = this.messageConfigManager.queryMessageConfigPageInfo(businessSystem,
							this.bean.getSystemMessageConfigListPaging()
									.getActivePage() + 1, this.bean
									.getSystemMessageConfigListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemMessageConfigListBox().setModel(dataList);
			this.bean.getSystemMessageConfigListPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
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
	 * TAB页面响应
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onBusinessSystemSelectCallQueryResponse(ForwardEvent event)
			throws Exception {
		if (event != null && event.getOrigin() != null
				&& event.getOrigin().getData() != null) {
			businessSystem = (BusinessSystem) event.getOrigin().getData();
			this.onQuerySystemMessageConfig();
		}
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemMessageConfigDel() throws Exception {

		if (systemConfigUser != null) {
			ZkUtil.showQuestion("你确定要删除该用户配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						SystemBusiUser querySysBusiUser = new SystemBusiUser();
						querySysBusiUser.setSystemConfigUserId(systemConfigUser.getId());
						sysBusiUserManager.removeBusinessSystem(businessSystem, querySysBusiUser);
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemConfigUser, "del");
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的用户配置", "提示信息");
			return;
		}
	}

	/**
	 * 新增业务系统
	 * 
	 * @throws Exception
	 */
	public void onSystemMessageConfigAdd() throws Exception {

		if (businessSystem != null
				&& !StrUtil.isEmpty(businessSystem.getSystemCode())) {
			this.openSystemMessageConfigEditWin("add");
		}
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
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {
			arg.put("businessSystem", businessSystem);
		} else if ("mod".equals(type)) {
			arg.put("systemMessageConfig", systemConfigUser);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/system_message_config_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					SystemConfigUser systemConfigUserNew = (SystemConfigUser) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemConfigUserNew, "add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMessageConfigListBox(),
								systemConfigUserNew, "mod");
						systemConfigUser = systemConfigUserNew;
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
		} else if ("interfaceConfigPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_SMS_NOTICE_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_SMS_NOTICE_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_SMS_NOTICE_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddSystemMessageConfigButton().setVisible(canAdd);
		//this.bean.getEditSystemMessageConfigButton().setVisible(canEdit);
		this.bean.getDelSystemMessageConfigButton().setVisible(canDel);
	}
}
