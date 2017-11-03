package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.action.bean.PiFinanceOrgMainBean;

/**
 * PI财务组织查询列表
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-06-28
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class PiFinanceOrgMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3475413632393655450L;
	/**
	 * bean.
	 */
	private PiFinanceOrgMainBean bean = new PiFinanceOrgMainBean();

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		UomZkUtil.autoFitHeight(comp);
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 点击tab
	 * 
	 * @throws Exception
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
	 * 响应tab事件
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabbox().getSelectedTab());
		}
	}
}
