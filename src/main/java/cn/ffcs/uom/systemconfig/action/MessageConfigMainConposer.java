package cn.ffcs.uom.systemconfig.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Listbox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.systemconfig.action.bean.MessageConfigMainBean;
import cn.ffcs.uom.systemconfig.constants.SystemConfigConstant;
import cn.ffcs.uom.webservices.model.SystemBusiUser;
import cn.ffcs.uom.webservices.model.SystemConfigUser;

@Controller
@Scope("prototype")
public class MessageConfigMainConposer extends BasePortletComposer implements
		IPortletInfoProvider {
	
	private static final long serialVersionUID = 1L;

	private MessageConfigMainBean bean = new MessageConfigMainBean();
	
	/**
	 * 选中的短信配置人员
	 */
	private SystemConfigUser systemConfigUser;

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
		this.bean.getMessageConfigListbox().setPortletInfoProvider(this);
		this.bean.getSysBusiUserListbox().setPortletInfoProvider(this);
	
		this.bean.getMessageConfigListbox().addForward(
				SystemConfigConstant.ON_SELECT_SYSTEM_CONFIG_USER, comp,
				"onSelectSystemConfigUserResponse");
		this.bean.getMessageConfigListbox().addForward(
				SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER, comp,
				"onRefreshSystemConfigUserResponse");
	}

	public void onCreate$messageConfigMainWin() throws Exception {
		initPage();
	}
	
	/**
	 * 配置人员选中事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectSystemConfigUserResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			SystemConfigUser systemConfigUser = (SystemConfigUser) event.getOrigin()
					.getData();
			if (systemConfigUser != null && systemConfigUser.getSystemConfigUserId() != null) {
				SystemBusiUser sysBusiUser = new SystemBusiUser();
				sysBusiUser.setSystemConfigUserId(systemConfigUser.getSystemConfigUserId());
				Events.postEvent(
						SystemConfigConstant.ON_SYS_BUSI_USER_QUERY,
						bean.getSysBusiUserListbox(), sysBusiUser);
			}
		}
	}
	
	public void onRefreshSystemConfigUserResponse(ForwardEvent event) throws Exception {
		Events.postEvent(
				SystemConfigConstant.ON_REFRESH_SYSTEM_CONFIG_USER,
				bean.getSysBusiUserListbox(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getSysBusiUserListbox().setPagePosition(
				"messageConfigPage");
		this.bean.getMessageConfigListbox().setPagePosition(
				"messageConfigPage");
	}

}
