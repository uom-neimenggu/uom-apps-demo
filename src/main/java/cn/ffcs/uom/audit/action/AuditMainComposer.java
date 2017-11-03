package cn.ffcs.uom.audit.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.audit.action.bean.AuditMainBean;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;

/**
 * 稽核查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-07-30
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class AuditMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private AuditMainBean bean = new AuditMainBean();

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
		UomZkUtil.autoFitHeight(comp);
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.bean.getStaffAuditExt().setPortletInfoProvider(this);
		this.bean.getOrgAuditExt().setPortletInfoProvider(this);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$auditMainWin() throws Exception {
		this.initPage();
		this.initTab();
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
			bean.setSelectTab(this.bean.getTabBox().getSelectedTab());
		}
		/**
		 * 当前页查询数据
		 */
		if (this.bean.getSelectTab().getId() != null
				&& this.bean.getSelectTab().getId() != null) {
			if ("staffAuditTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent("onQueryStaffAuditRequest",
						bean.getStaffAuditExt(), null);
			}
			if ("orgAuditTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent("onQueryOrgAuditRequest",
						bean.getOrgAuditExt(), null);
			}
		}

	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getStaffAuditExt().setPagePosition("auditPage");
		this.bean.getOrgAuditExt().setPagePosition("auditPage");

	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initTab() throws Exception {
		if (!PlatformUtil.checkHasPermission(this, ActionKeys.STAFF_AUDIT_TAB)) {
			this.bean.getStaffAuditTab().setVisible(false);
			this.bean.getStaffAuditTab().setSelected(false);
		}
		if (!PlatformUtil.checkHasPermission(this, ActionKeys.ORG_AUDIT_TAB)) {
			this.bean.getOrgAuditTab().setVisible(false);
		}
		if (!PlatformUtil.checkHasPermission(this, ActionKeys.STAFF_AUDIT_TAB)
				&& !PlatformUtil.checkHasPermission(this,
						ActionKeys.ORG_AUDIT_TAB)) {
			this.bean.getTempTab().setVisible(true);
			this.bean.getTempTab().setSelected(true);
			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
		} else {
			if (PlatformUtil.checkHasPermission(this,
					ActionKeys.STAFF_AUDIT_TAB)) {
				this.bean.getStaffAuditTab().setSelected(true);
			} else if (PlatformUtil.checkHasPermission(this,
					ActionKeys.ORG_AUDIT_TAB)) {
				this.bean.getOrgAuditTab().setSelected(true);
			}
		}
	}
	
}
