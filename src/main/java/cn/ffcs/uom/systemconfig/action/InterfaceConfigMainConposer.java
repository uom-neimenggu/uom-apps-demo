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
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.systemconfig.action.bean.InterfaceConfigMainBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;

@Controller
@Scope("prototype")
public class InterfaceConfigMainConposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private InterfaceConfigMainBean bean = new InterfaceConfigMainBean();

	/**
	 * 选中业务系统
	 */
	private BusinessSystem businessSystem;

	@Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		// TODO Auto-generated method stub
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getBusinessSystemListExt().setPortletInfoProvider(this);
		this.bean.getSystemIntfInfoConfigListExt().setPortletInfoProvider(this);
		this.bean.getSystemOrgTreeConfigListExt().setPortletInfoProvider(this);
		this.bean.getSystemMessageConfigListExt().setPortletInfoProvider(this);
		this.bean.getSystemMonitorConfigListExt().setPortletInfoProvider(this);
		this.bean.getSystemMonitorConfigListExt().getBean()
				.getSystemMonitorConfigFilterListExt()
				.setPortletInfoProvider(this);
		this.bean.getBusinessSystemListExt().addForward(
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_REQUEST, comp,
				SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_RESOPONSE);
	}

	public void onCreate$interfaceConfigMainWin() throws Exception {
		initPage();
	}

	/**
	 * 选中业务系统响应事件
	 */
	public void onBusinessSystemSelectResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			BusinessSystem businessSystem = (BusinessSystem) event.getOrigin()
					.getData();
			if (businessSystem != null) {
				this.businessSystem = businessSystem;
				this.callTab();
			}
		}
	}

	/**
	 * 选中业务系统触发查询事件
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabbox().getSelectedTab());
		}
		if (businessSystem != null) {
			if ("systemIntfInfoConfigTab".equals(this.bean.getSelectTab()
					.getId())) {
				Events.postEvent(
						SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
						this.bean.getSystemIntfInfoConfigListExt(),
						businessSystem);
			} else if ("systemOrgTreeConfigTab".equals(this.bean.getSelectTab()
					.getId())) {
				Events.postEvent(
						SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
						this.bean.getSystemOrgTreeConfigListExt(),
						businessSystem);
			} else if ("systemMessageConfigTab".equals(this.bean.getSelectTab()
					.getId())) {
				Events.postEvent(
						SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
						this.bean.getSystemMessageConfigListExt(),
						businessSystem);
			} else if ("systemMonitorConfigTab".equals(this.bean.getSelectTab()
					.getId())) {
				Events.postEvent(
						SystemConfigConstant.ON_BUSINESS_SYSTEM_SELECT_CALL_QUERY_REQUEST,
						this.bean.getSystemMonitorConfigListExt(),
						businessSystem);
			}
		}
	}

	/**
	 * 点击TAB页时触发查询事件
	 * 
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

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getBusinessSystemListExt().setPagePosition(
				"interfaceConfigPage");
		this.bean.getSystemIntfInfoConfigListExt().setPagePosition(
				"interfaceConfigPage");
		this.bean.getSystemOrgTreeConfigListExt().setPagePosition(
				"interfaceConfigPage");
		this.bean.getSystemMessageConfigListExt().setPagePosition(
				"interfaceConfigPage");
		this.bean.getSystemMonitorConfigListExt().setPagePosition(
				"interfaceConfigPage");
		this.bean.getSystemMonitorConfigListExt().getBean()
				.getSystemMonitorConfigFilterListExt()
				.setPagePosition("interfaceConfigPage");
	}

}
