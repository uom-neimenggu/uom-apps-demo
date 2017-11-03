package cn.ffcs.uom.systemconfig.action.comp;

import java.util.HashMap;
import java.util.List;
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
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.businesssystem.model.SystemOrgTreeConfig;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.BusinessSystemListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.systemconfig.manager.BusinessSystemManager;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.IntfTaskInstanceManager;
import cn.ffcs.uom.webservices.model.IntfTaskInstance;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

@Controller
@Scope("prototype")
public class BusinessSystemListExt extends Div implements IdSpace {

	public BusinessSystemListExtBean bean = new BusinessSystemListExtBean();

	private String zul = "/pages/system_config/comp/business_system_list_ext.zul";

	private BusinessSystemManager businessSystemManager = (BusinessSystemManager) ApplicationContextUtil
			.getBean("businessSystemManager");

	private IntfTaskInstanceManager intfTaskInstanceManager = (IntfTaskInstanceManager) ApplicationContextUtil
			.getBean("intfTaskInstanceManager");
	/**
	 * 查询业务系统
	 */
	private BusinessSystem queryBusinessSystem;

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
	public BusinessSystemListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() throws Exception {
		this.setButtonValid(true, false, false);
		onQueryBusinessSystem();
	}

	/**
	 * 查询业务系统
	 */
	public void onQueryBusinessSystem() {
		try {
			this.bean.getBusinessSystemListPaging().setActivePage(0);
			this.onBusinessSystemListboxPaging();
			this.queryBusinessSystem = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重置查询内容
	 */
	public void onResetBusinessSystem() throws Exception {
		bean.getSystemCode().setValue(null);
		bean.getSystemName().setValue(null);
	}

	/**
	 * 选中业务系统
	 */
	public void onBusinessSystemSelect() throws Exception {
		if (this.bean.getBusinessSystemListBox().getSelectedCount() > 0) {
			businessSystem = (BusinessSystem) this.bean
					.getBusinessSystemListBox().getSelectedItem().getValue();
			if (businessSystem != null) {
				this.setButtonValid(true, true, true);
				Events.postEvent(
						SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_REQUEST,
						this, businessSystem);
			}
		}
	}

	/**
	 * 分页查询业务系统
	 */
	public void onBusinessSystemListboxPaging() throws Exception {

		queryBusinessSystem = new BusinessSystem();
		queryBusinessSystem.setSystemCode(this.bean.getSystemCode().getValue());
		queryBusinessSystem.setSystemName(this.bean.getSystemName().getValue());

		if (this.queryBusinessSystem != null) {
			PageInfo pageInfo = this.businessSystemManager
					.queryBusinessSystemPageInfo(queryBusinessSystem, this.bean
							.getBusinessSystemListPaging().getActivePage() + 1,
							this.bean.getBusinessSystemListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getBusinessSystemListBox().setModel(dataList);
			this.bean.getBusinessSystemListPaging().setTotalSize(
					pageInfo.getTotalCount());
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canAdd, boolean canEdit, boolean canDel) {
		this.bean.getAddBusinessSystemButton().setDisabled(!canAdd);
		this.bean.getEditBusinessSystemButton().setDisabled(!canEdit);
		this.bean.getDelBusinessSystemButton().setDisabled(!canDel);
	}

	/**
	 * 删除业务系统
	 */
	public void onBusinessSystemDel() throws Exception {

		if (businessSystem != null) {
			/**
			 * 系统接口信息配置
			 */
			businessSystem.setSystemIntfInfoConfigList(null);
			List<SystemIntfInfoConfig> systemIntfInfoConfigList = businessSystem
					.getSysIntfInfoConfigList();
			if (systemIntfInfoConfigList != null
					&& systemIntfInfoConfigList.size() > 0) {
				ZkUtil.showError("存在系统接口信息配置,不能删除", "提示信息");
				return;
			}

			/**
			 * 系统接口配置组织树
			 */
			businessSystem.setSystemOrgTreeConfigList(null);
			List<SystemOrgTreeConfig> systemOrgTreeConfigList = businessSystem
					.getSysOrgTreeConfigList();
			if (systemOrgTreeConfigList != null
					&& systemOrgTreeConfigList.size() > 0) {
				ZkUtil.showError("存在系统接口组织树配置,不能删除", "提示信息");
				return;
			}

			ZkUtil.showQuestion("你确定要删除该业务系统吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						businessSystemManager
								.removeBusinessSystem(businessSystem);
						PubUtil.reDisplayListbox(
								bean.getBusinessSystemListBox(),
								businessSystem, "del");
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的业务系统", "提示信息");
			return;
		}
	}

	/**
	 * 新增业务系统
	 * 
	 * @throws Exception
	 */
	public void onBusinessSystemAdd() throws Exception {
		this.openBusinessSystemEditWin("add");
	}

	/**
	 * 修改业务系统
	 * 
	 * @throws Exception
	 */
	public void onBusinessSystemEdit() throws Exception {
		this.openBusinessSystemEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openBusinessSystemEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {

		} else if ("mod".equals(type)) {
			arg.put("businessSystem", businessSystem);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/business_system_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					BusinessSystem businessSys = (BusinessSystem) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getBusinessSystemListBox(), businessSys,
								"add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getBusinessSystemListBox(), businessSys,
								"mod");
						businessSystem = businessSys;
					}
				}
			}
		});
	}

	/**
	 * 阀值开关
	 * 
	 */
	public void onBusinessSystemThreshold() throws Exception {
		this.openBusinessSystemSwitchEditWin("threshold");
	}

	/**
	 * FTP通知开关
	 * 
	 */
	public void onBusinessSystemFtpNotice() throws Exception {
		this.openBusinessSystemSwitchEditWin("ftpNotice");
	}

	/**
	 * FTP数据补发
	 * 
	 */
	public void onBusinessSystemFtpReplacement() throws Exception {

		IntfTaskInstance queryIntfTaskInstance = new IntfTaskInstance();
		queryIntfTaskInstance.setInvokeResule(WsConstants.TASK_FAILED);
		queryIntfTaskInstance.setInvokeTimes(WsConstants.LIMIT_TIMES_FTP);

		List<IntfTaskInstance> intfTaskInstanceList = intfTaskInstanceManager
				.queryIntfTaskInstanceList(queryIntfTaskInstance);

		if (intfTaskInstanceList != null && intfTaskInstanceList.size() > 0) {
			for (IntfTaskInstance intfTaskInstance : intfTaskInstanceList) {
				intfTaskInstance.setInvokeTimes(WsConstants.TASK_FAILED);
				intfTaskInstance.updateOnly();
			}
		}

		ZkUtil.showInformation("FTP数据补发成功！", "提示信息");

	}

	/**
	 * 短信通知开关
	 * 
	 */
	public void onBusinessSystemSmsNotice() throws Exception {
		this.openBusinessSystemSwitchEditWin("smsNotice");
	}

	/**
	 * 邮件发送开关
	 * 
	 */
	public void onBusinessSystemMailSend() throws Exception {
		this.openBusinessSystemSwitchEditWin("mailSend");
	}

	/**
	 * FTP定时开关
	 * 
	 */
	public void onBusinessSystemFtpTimer() throws Exception {
		this.openBusinessSystemSwitchEditWin("ftpTimer");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openBusinessSystemSwitchEditWin(String opType)
			throws Exception {
		Map arg = new HashMap();
		this.opType = opType;
		arg.put("opType", opType);
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

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;
		boolean canThreshold = false;
		boolean canFtpNotice = false;
		boolean canFtpReplacement = false;
		boolean canSmsNotice = false;
		boolean canMailSend = false;
		boolean canFtpTimer = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canEdit = true;
			canDel = true;
			canThreshold = true;
			canFtpNotice = true;
			canFtpReplacement = true;
			canSmsNotice = true;
			canMailSend = true;
			canFtpTimer = true;
		} else if ("interfaceConfigPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_DEL)) {
				canDel = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_THRESHOLD)) {
				canThreshold = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_FTP_NOTICE)) {
				canFtpNotice = true;
			}

			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_FTP_REPLACEMENT)) {
				canFtpReplacement = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_SMS_NOTICE)) {
				canSmsNotice = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_MAIL_SEND)) {
				canMailSend = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_BUSINESS_SYSTEM_FTP_TIMER)) {
				canFtpTimer = true;
			}

		}

		this.bean.getAddBusinessSystemButton().setVisible(canAdd);
		this.bean.getEditBusinessSystemButton().setVisible(canEdit);
		this.bean.getDelBusinessSystemButton().setVisible(canDel);
		this.bean.getThresholdSwitchButton().setVisible(canThreshold);
		this.bean.getFtpNoticeSwitchButton().setVisible(canFtpNotice);
		this.bean.getFtpReplacementButton().setVisible(canFtpReplacement);
		this.bean.getSmsNoticeSwitchButton().setVisible(canSmsNotice);
		this.bean.getMaiilSendSwitchButton().setVisible(canMailSend);
		this.bean.getFtpTimerSwitchButton().setVisible(canFtpTimer);
	}

}
