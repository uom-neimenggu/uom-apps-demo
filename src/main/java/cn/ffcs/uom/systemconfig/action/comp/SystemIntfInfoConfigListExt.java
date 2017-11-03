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
import cn.ffcs.uom.systemconfig.action.bean.comp.SystemIntfInfoConfigListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.manager.SystemIntfInfoConfigManager;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

@Controller
@Scope("prototype")
public class SystemIntfInfoConfigListExt extends Div implements IdSpace {

	public SystemIntfInfoConfigListExtBean bean = new SystemIntfInfoConfigListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/system_intf_info_config_list_ext.zul";

	private SystemIntfInfoConfigManager systemIntfInfoConfigManager = (SystemIntfInfoConfigManager) ApplicationContextUtil
			.getBean("systemIntfInfoConfigManager");

	/**
	 * 查询时存放条件
	 */
	private SystemIntfInfoConfig querySystemIntfInfoConfig;

	/**
	 * 选中业务系统配置信息
	 */
	private SystemIntfInfoConfig systemIntfInfoConfig;
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
	public SystemIntfInfoConfigListExt() {
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
	public void onQuerySystemIntfInfoConfig() throws Exception {
		this.bean.getSystemIntfInfoConfigListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onSystemIntfInfoConfigListboxPaging();
		querySystemIntfInfoConfig = null;
	}

	/**
	 * 选中业务系统组织树配置
	 */
	public void onSystemIntfInfoConfigSelect() throws Exception {
		if (this.bean.getSystemIntfInfoConfigListBox().getSelectedCount() > 0) {
			systemIntfInfoConfig = (SystemIntfInfoConfig) this.bean
					.getSystemIntfInfoConfigListBox().getSelectedItem()
					.getValue();
			if (systemIntfInfoConfig != null) {
				this.setButtonValid(true, true, true);
			}
		}
	}

	/**
	 * 分页查询
	 */
	public void onSystemIntfInfoConfigListboxPaging() throws Exception {
		if (businessSystem != null && businessSystem.getSystemCode() != null) {
			querySystemIntfInfoConfig = new SystemIntfInfoConfig();
			querySystemIntfInfoConfig.setSystemCode(businessSystem
					.getSystemCode());

			PageInfo pageInfo = this.systemIntfInfoConfigManager
					.querySystemIntfInfoConfigPageInfo(
							querySystemIntfInfoConfig, this.bean
									.getSystemIntfInfoConfigListPaging()
									.getActivePage() + 1, this.bean
									.getSystemIntfInfoConfigListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemIntfInfoConfigListBox().setModel(dataList);
			this.bean.getSystemIntfInfoConfigListPaging().setTotalSize(
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
		this.bean.getAddSystemIntfInfoConfigButton().setDisabled(!canAdd);
		this.bean.getEditSystemIntfInfoConfigButton().setDisabled(!canEdit);
		this.bean.getDelSystemIntfInfoConfigButton().setDisabled(!canDel);
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
			this.onQuerySystemIntfInfoConfig();
		}
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemIntfInfoConfigDel() throws Exception {

		if (systemIntfInfoConfig != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						systemIntfInfoConfigManager
								.removeSystemIntfInfoConfig(systemIntfInfoConfig);
						PubUtil.reDisplayListbox(
								bean.getSystemIntfInfoConfigListBox(),
								systemIntfInfoConfig, "del");
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
	public void onSystemIntfInfoConfigAdd() throws Exception {

		if (businessSystem != null
				&& !StrUtil.isEmpty(businessSystem.getSystemCode())) {

			SystemIntfInfoConfig sysIntfInfoConfig = this.systemIntfInfoConfigManager
					.querySystemIntfInfoConfigBySystemCode(businessSystem
							.getSystemCode());

			if (sysIntfInfoConfig != null) {
				ZkUtil.showError(businessSystem.getSystemName() + "业务系统信息已经配置",
						"提示信息");
				return;
			}

			this.openSystemIntfInfoConfigEditWin("add");
		}
	}

	/**
	 * 修改业务系统
	 * 
	 * @throws Exception
	 */
	public void onSystemIntfInfoConfigEdit() throws Exception {

		this.openSystemIntfInfoConfigEditWin("mod");
	}

	/**
	 * 打开页面
	 * 
	 * @param string
	 */
	private void openSystemIntfInfoConfigEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if ("add".equals(type)) {
			arg.put("businessSystem", businessSystem);
		} else if ("mod".equals(type)) {
			arg.put("systemIntfInfoConfig", systemIntfInfoConfig);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/system_config/system_intf_info_config_edit.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					SystemIntfInfoConfig systemIntfInfoConfigNew = (SystemIntfInfoConfig) event
							.getData();
					if ("add".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemIntfInfoConfigListBox(),
								systemIntfInfoConfigNew, "add");
					} else if ("mod".equals(opType)) {
						PubUtil.reDisplayListbox(
								bean.getSystemIntfInfoConfigListBox(),
								systemIntfInfoConfigNew, "mod");
						systemIntfInfoConfig = systemIntfInfoConfigNew;
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
					ActionKeys.INTERFACE_CONFIG_INFO_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_INFO_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_INFO_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddSystemIntfInfoConfigButton().setVisible(canAdd);
		this.bean.getEditSystemIntfInfoConfigButton().setVisible(canEdit);
		this.bean.getDelSystemIntfInfoConfigButton().setVisible(canDel);
	}
}
