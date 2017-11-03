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
import cn.ffcs.uom.systemconfig.action.bean.comp.SystemMonitorConfigListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;

@Controller
@Scope("prototype")
public class SystemMonitorConfigListExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	@Getter
	public SystemMonitorConfigListExtBean bean = new SystemMonitorConfigListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/system_monitor_config_list_ext.zul";

	private SystemMonitorConfigManager systemMonitorConfigManager = (SystemMonitorConfigManager) ApplicationContextUtil
			.getBean("systemMonitorConfigManager");

	/**
	 * 查询时存放条件
	 */
	private SystemMonitorConfig querySystemMonitorConfig;

	/**
	 * 选中业务系统配置信息
	 */
	private SystemMonitorConfig systemMonitorConfig;
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
	public SystemMonitorConfigListExt() {
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
	public void onQuerySystemMonitorConfig() throws Exception {
		this.bean.getSystemMonitorConfigListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onSystemMonitorConfigListboxPaging();
		querySystemMonitorConfig = null;
	}

	/**
	 * 选中业务系统组织树配置
	 */
	public void onSystemMonitorConfigSelect() throws Exception {
		if (this.bean.getSystemMonitorConfigListBox().getSelectedCount() > 0) {
			systemMonitorConfig = (SystemMonitorConfig) this.bean
					.getSystemMonitorConfigListBox().getSelectedItem()
					.getValue();
			if (systemMonitorConfig != null) {
				this.setButtonValid(true, true, true);
				this.bean.getSystemMonitorConfigFilterListExt()
						.onSystemMonitorConfigSelectCallQueryRequest(
								systemMonitorConfig);
			}
		}
	}

	/**
	 * 分页查询
	 */
	public void onSystemMonitorConfigListboxPaging() throws Exception {
		if (businessSystem != null && businessSystem.getSystemCode() != null) {
			querySystemMonitorConfig = new SystemMonitorConfig();
			querySystemMonitorConfig.setSystemCode(businessSystem
					.getSystemCode());
			PageInfo pageInfo = this.systemMonitorConfigManager
					.querySystemMonitorConfigPageInfo(querySystemMonitorConfig,
							this.bean.getSystemMonitorConfigListPaging()
									.getActivePage() + 1, this.bean
									.getSystemMonitorConfigListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemMonitorConfigListBox().setModel(dataList);
			this.bean.getSystemMonitorConfigListPaging().setTotalSize(
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
		this.bean.getAddSystemMonitorConfigButton().setDisabled(!canAdd);
		this.bean.getEditSystemMonitorConfigButton().setDisabled(!canEdit);
		this.bean.getDelSystemMonitorConfigButton().setDisabled(!canDel);
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
			this.onQuerySystemMonitorConfig();
			this.bean
					.getSystemMonitorConfigFilterListExt()
					.onQuerySystemMonitorConfigFilterBySystemMonitorConfigId(0L);
		}
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemMonitorConfigDel() throws Exception {
		if (systemMonitorConfig != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						systemMonitorConfigManager
								.removeSystemMonitorConfig(systemMonitorConfig);
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigListBox(),
								systemMonitorConfig, "del");
					}
				}
			});
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
	public void onSystemMonitorConfigAdd() throws Exception {
		if (businessSystem != null
				&& !StrUtil.isEmpty(businessSystem.getSystemCode())) {
			this.openSystemMonitorConfigEditWin("add");
		}
	}

	/**
	 * 修改业务系统
	 * 
	 * @throws Exception
	 */
	public void onSystemMonitorConfigEdit() throws Exception {
		this.openSystemMonitorConfigEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openSystemMonitorConfigEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {
			arg.put("businessSystem", businessSystem);
		} else if ("mod".equals(type)) {
			arg.put("systemMonitorConfig", systemMonitorConfig);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/system_monitor_config_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					SystemMonitorConfig systemIntfInfoConfigNew = (SystemMonitorConfig) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigListBox(),
								systemIntfInfoConfigNew, "add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigListBox(),
								systemIntfInfoConfigNew, "mod");
						systemMonitorConfig = systemIntfInfoConfigNew;
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
					ActionKeys.INTERFACE_CONFIG_MONITOR_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_MONITOR_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_MONITOR_DEL)) {
				canDel = true;
			}
		}

		this.bean.getAddSystemMonitorConfigButton().setVisible(canAdd);
		this.bean.getEditSystemMonitorConfigButton().setVisible(canEdit);
		this.bean.getDelSystemMonitorConfigButton().setVisible(canDel);
	}
}
