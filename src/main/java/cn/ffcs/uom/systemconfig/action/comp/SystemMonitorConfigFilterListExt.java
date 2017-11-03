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
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.SystemMonitorConfigFilterListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.manager.SystemMonitorConfigFilterManager;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;
import cn.ffcs.uom.webservices.model.SystemMonitorConfigFilter;

@Controller
@Scope("prototype")
public class SystemMonitorConfigFilterListExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	public SystemMonitorConfigFilterListExtBean bean = new SystemMonitorConfigFilterListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/system_monitor_config_filter_list_ext.zul";

	private SystemMonitorConfigFilterManager systemMonitorConfigFilterManager = (SystemMonitorConfigFilterManager) ApplicationContextUtil
			.getBean("systemMonitorConfigFilterManager");

	/**
	 * 查询时存放条件
	 */
	private SystemMonitorConfigFilter querySystemMonitorConfigFilter;

	/**
	 * 选中业务系统配置信息
	 */
	private SystemMonitorConfigFilter systemMonitorConfigFilter;
	/**
	 * 选中的业务系统
	 */
	private SystemMonitorConfig systemMonitorConfig;

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
	public SystemMonitorConfigFilterListExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
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
	public void onQuerySystemMonitorConfigFilter() throws Exception {
		this.bean.getSystemMonitorConfigFilterListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onSystemMonitorConfigFilterListboxPaging();
		querySystemMonitorConfigFilter = null;
	}

	public void onQuerySystemMonitorConfigFilterBySystemMonitorConfigId(
			Long systemMonitorConfigId) throws Exception {
		if (null == systemMonitorConfig) {
			systemMonitorConfig = new SystemMonitorConfig();
		}
		systemMonitorConfig.setSystemMonitorConfigId(systemMonitorConfigId);
		this.bean.getSystemMonitorConfigFilterListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onSystemMonitorConfigFilterListboxPaging();
		querySystemMonitorConfigFilter = null;
	}

	/**
	 * 选中业务系统组织树配置
	 */
	public void onSystemMonitorConfigFilterSelect() throws Exception {
		if (this.bean.getSystemMonitorConfigFilterListBox().getSelectedCount() > 0) {
			systemMonitorConfigFilter = (SystemMonitorConfigFilter) this.bean
					.getSystemMonitorConfigFilterListBox().getSelectedItem()
					.getValue();
			if (systemMonitorConfigFilter != null) {
				this.setButtonValid(true, true, true);
			}
		}
	}

	/**
	 * 分页查询
	 */
	public void onSystemMonitorConfigFilterListboxPaging() throws Exception {
		if (systemMonitorConfig != null
				&& systemMonitorConfig.getSystemCode() != null) {
			querySystemMonitorConfigFilter = new SystemMonitorConfigFilter();
			querySystemMonitorConfigFilter
					.setSystemMonitorConfigId(systemMonitorConfig
							.getSystemMonitorConfigId());
			PageInfo pageInfo = this.systemMonitorConfigFilterManager
					.querySystemMonitorConfigFilterPageInfo(
							querySystemMonitorConfigFilter, this.bean
									.getSystemMonitorConfigFilterListPaging()
									.getActivePage() + 1, this.bean
									.getSystemMonitorConfigFilterListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemMonitorConfigFilterListBox().setModel(dataList);
			this.bean.getSystemMonitorConfigFilterListPaging().setTotalSize(
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
		this.bean.getAddSystemMonitorConfigFilterButton().setDisabled(!canAdd);
		this.bean.getEditSystemMonitorConfigFilterButton()
				.setDisabled(!canEdit);
		this.bean.getDelSystemMonitorConfigFilterButton().setDisabled(!canDel);
	}

	/**
	 * TAB页面响应
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSystemMonitorConfigSelectCallQueryRequest(
			SystemMonitorConfig smc) throws Exception {
		if (smc != null) {
			systemMonitorConfig = smc;
			this.onQuerySystemMonitorConfigFilter();
		}
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemMonitorConfigFilterDel() throws Exception {
		if (systemMonitorConfigFilter != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						systemMonitorConfigFilterManager
								.removeSystemMonitorConfigFilter(systemMonitorConfigFilter);
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigFilterListBox(),
								systemMonitorConfigFilter, "del");
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
	public void onSystemMonitorConfigFilterAdd() throws Exception {
		if (systemMonitorConfig != null
				&& systemMonitorConfig.getSystemMonitorConfigId() != null) {
			this.openSystemMonitorConfigFilterEditWin("add");
		}
	}

	/**
	 * 修改业务系统
	 * 
	 * @throws Exception
	 */
	public void onSystemMonitorConfigFilterEdit() throws Exception {
		this.openSystemMonitorConfigFilterEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openSystemMonitorConfigFilterEditWin(String type)
			throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {
			arg.put("systemMonitorConfig", systemMonitorConfig);
		} else if ("mod".equals(type)) {
			arg.put("systemMonitorConfig", systemMonitorConfig);
			arg.put("systemMonitorConfigFilter", systemMonitorConfigFilter);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/system_monitor_config_filter_edit.zul",
				this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					SystemMonitorConfigFilter systemIntfInfoConfigNew = (SystemMonitorConfigFilter) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigFilterListBox(),
								systemIntfInfoConfigNew, "add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemMonitorConfigFilterListBox(),
								systemIntfInfoConfigNew, "mod");
						systemMonitorConfigFilter = systemIntfInfoConfigNew;
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
					ActionKeys.INTERFACE_CONFIG_MONITOR_FILTER_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_MONITOR_FILTER_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_MONITOR_FILTER_DEL)) {
				canDel = true;
			}
		}

		this.bean.getAddSystemMonitorConfigFilterButton().setVisible(canAdd);
		this.bean.getEditSystemMonitorConfigFilterButton().setVisible(canEdit);
		this.bean.getDelSystemMonitorConfigFilterButton().setVisible(canDel);
	}
}
