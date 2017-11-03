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
import org.zkoss.zk.ui.SuspendNotAllowedException;
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
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.businesssystem.manager.SystemOrgTreeConfigManager;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.businesssystem.model.SystemOrgTreeConfig;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.systemconfig.action.bean.comp.SystemOrgTreeConfigListExtBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class SystemOrgTreeConfigListExt extends Div implements IdSpace {

	public SystemOrgTreeConfigListExtBean bean = new SystemOrgTreeConfigListExtBean();

	/**
	 * 页面
	 */
	private String zul = "/pages/system_config/comp/system_org_tree_config_list_ext.zul";

	private SystemOrgTreeConfigManager systemOrgTreeConfigManager = (SystemOrgTreeConfigManager) ApplicationContextUtil
			.getBean("systemOrgTreeConfigManagerImpl");

	/**
	 * 查询时存放条件
	 */
	private SystemOrgTreeConfig querySystemOrgTreeConfig;

	/**
	 * 选中系统组织树配置
	 */
	private SystemOrgTreeConfig systemOrgTreeConfig;
	/**
	 * 选中的业务系统
	 */
	private BusinessSystem businessSystem;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 构造方法
	 */
	public SystemOrgTreeConfigListExt() {
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
	public void onQuerySystemOrgTreeConfig() throws Exception {
		this.bean.getSystemOrgTreeConfigListPaging().setActivePage(0);
		this.setButtonValid(true, false, false);
		this.onSystemOrgTreeConfigListboxPaging();
		querySystemOrgTreeConfig = null;
	}

	/**
	 * 选中业务系统组织树配置
	 */
	public void onSystemOrgTreeConfigSelect() throws Exception {
		if (this.bean.getSystemOrgTreeConfigListBox().getSelectedCount() > 0) {
			systemOrgTreeConfig = (SystemOrgTreeConfig) this.bean
					.getSystemOrgTreeConfigListBox().getSelectedItem()
					.getValue();
			if (systemOrgTreeConfig != null) {
				this.setButtonValid(true, true, true);
			}
		}
	}

	/**
	 * 分页查询
	 */
	public void onSystemOrgTreeConfigListboxPaging() throws Exception {
		if (businessSystem != null
				&& businessSystem.getBusinessSystemId() != null) {
			querySystemOrgTreeConfig = new SystemOrgTreeConfig();
			querySystemOrgTreeConfig.setBusinessSystemId(businessSystem
					.getBusinessSystemId());

			PageInfo pageInfo = this.systemOrgTreeConfigManager
					.querySystemOrgTreeConfigPageInfo(querySystemOrgTreeConfig,
							this.bean.getSystemOrgTreeConfigListPaging()
									.getActivePage() + 1, this.bean
									.getSystemOrgTreeConfigListPaging()
									.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getSystemOrgTreeConfigListBox().setModel(dataList);
			this.bean.getSystemOrgTreeConfigListPaging().setTotalSize(
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
		this.bean.getAddSystemOrgTreeConfigButton().setDisabled(!canAdd);
		this.bean.getEditSystemOrgTreeConfigButton().setDisabled(!canEdit);
		this.bean.getDelSystemOrgTreeConfigButton().setDisabled(!canDel);
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
			this.onQuerySystemOrgTreeConfig();
		}
	}

	/**
	 * 删除业务系统组织树配置
	 */
	public void onSystemOrgTreeConfigDel() throws Exception {

		if (systemOrgTreeConfig != null) {
			ZkUtil.showQuestion("你确定要删除该配置吗?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						systemOrgTreeConfigManager
								.removeSystemOrgTreeConfig(systemOrgTreeConfig);
						PubUtil.reDisplayListbox(
								bean.getSystemOrgTreeConfigListBox(),
								systemOrgTreeConfig, "del");
						systemOrgTreeConfig = null;
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的配置", "提示信息");
			return;
		}
	}

	/**
	 * 新增系统接口组织树配置 .
	 * 
	 * @author 朱林涛
	 */
	public void onSystemOrgTreeConfigAdd() throws PortalException,
			SystemException {
		openSystemOrgTreeConfigEditWin(BaseUnitConstants.ENTT_STATE_ADD);
	}

	/**
	 * 修改系统接口组织树配置.
	 * 
	 * @author 朱林涛
	 */
	public void onSystemOrgTreeConfigEdit() throws PortalException,
			SystemException {
		openSystemOrgTreeConfigEditWin(BaseUnitConstants.ENTT_STATE_MOD);
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	public void openSystemOrgTreeConfigEditWin(String opType) {
		try {
			if (businessSystem != null
					&& businessSystem.getBusinessSystemId() != null) {

				if (BaseUnitConstants.ENTT_STATE_ADD.equals(opType)) {
					SystemOrgTreeConfig sysOrgTreeConfig = new SystemOrgTreeConfig();
					sysOrgTreeConfig.setBusinessSystemId(businessSystem
							.getBusinessSystemId());

					List<SystemOrgTreeConfig> list = systemOrgTreeConfigManager
							.querySystemOrgTreeConfigList(sysOrgTreeConfig);

					if (list != null && list.size() > 0) {
						ZkUtil.showError("该业务系统组织树已经配置，请重新选择！", "提示信息");
						return;
					}
				}

				Map arg = new HashMap();
				arg.put("opType", opType);
				arg.put("businessSystem", businessSystem);
				arg.put("systemOrgTreeConfig", systemOrgTreeConfig);
				Window win = (Window) Executions.createComponents(
						"/pages/system_config/system_org_tree_config_edit.zul",
						this, arg);
				win.doModal();
				win.addEventListener(Events.ON_OK, new EventListener() {
					@Override
					public void onEvent(Event event) throws Exception {
						onQuerySystemOrgTreeConfig();
						if (event.getData() != null) {

						}
					}
				});
			}
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception ec) {
			ec.printStackTrace();
		}
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
					ActionKeys.INTERFACE_CONFIG_ORG_TREE_ADD)) {
				canAdd = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_ORG_TREE_EDIT)) {
				canEdit = true;
			}

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.INTERFACE_CONFIG_ORG_TREE_DEL)) {
				canDel = true;
			}

		}

		this.bean.getAddSystemOrgTreeConfigButton().setVisible(canAdd);
		this.bean.getEditSystemOrgTreeConfigButton().setVisible(canEdit);
		this.bean.getDelSystemOrgTreeConfigButton().setVisible(canDel);
	}

}
