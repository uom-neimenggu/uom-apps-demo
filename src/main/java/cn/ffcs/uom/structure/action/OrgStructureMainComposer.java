package cn.ffcs.uom.structure.action;

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
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.structure.action.bean.OrgStructureMainBean;

/**
 * 组织结构查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-03-31
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class OrgStructureMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrgStructureMainBean bean = new OrgStructureMainBean();

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
		this.bean.getPoliticalStructureExt().setPortletInfoProvider(this);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$orgStructureMainWin() throws Exception {
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
			if ("politicalTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent("onQueryOrgStructureRequest",
						bean.getPoliticalStructureExt(), null);
			}
		}

	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getPoliticalStructureExt()
				.setPagePosition("orgStructurePage");

	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initTab() throws Exception {
		if (!PlatformUtil.checkHasPermission(this,
				ActionKeys.POLITICAL_STRUCTURE_TAB)) {
			this.bean.getPoliticalTab().setVisible(false);
			this.bean.getPoliticalTab().setSelected(false);
		}
		if (!PlatformUtil.checkHasPermission(this,
				ActionKeys.AGENT_STRUCTURE_TAB)) {
			this.bean.getAgentTab().setVisible(false);
		}
		if (!PlatformUtil.checkHasPermission(this,
				ActionKeys.SUPPLIER_STRUCTURE_TAB)) {
			this.bean.getSupplierTab().setVisible(false);
		}
		if (!PlatformUtil
				.checkHasPermission(this, ActionKeys.OSS_STRUCTURE_TAB)) {
			this.bean.getOssTab().setVisible(false);
		}
		if (!PlatformUtil.checkHasPermission(this,
				ActionKeys.POLITICAL_STRUCTURE_TAB)
				&& !PlatformUtil.checkHasPermission(this,
						ActionKeys.AGENT_STRUCTURE_TAB)
				&& !PlatformUtil.checkHasPermission(this,
						ActionKeys.SUPPLIER_STRUCTURE_TAB)
				&& !PlatformUtil.checkHasPermission(this,
						ActionKeys.OSS_STRUCTURE_TAB)) {
			this.bean.getTempTab().setVisible(true);
			this.bean.getTempTab().setSelected(true);
			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
		} else {
			if (PlatformUtil.checkHasPermission(this,
					ActionKeys.POLITICAL_STRUCTURE_TAB)) {
				this.bean.getPoliticalTab().setSelected(true);
			} else if (PlatformUtil.checkHasPermission(this,
					ActionKeys.AGENT_STRUCTURE_TAB)) {
				this.bean.getAgentTab().setSelected(true);
			} else if (PlatformUtil.checkHasPermission(this,
					ActionKeys.SUPPLIER_STRUCTURE_TAB)) {
				this.bean.getSupplierTab().setSelected(true);
			} else if (PlatformUtil.checkHasPermission(this,
					ActionKeys.OSS_STRUCTURE_TAB)) {
				this.bean.getOssTab().setSelected(true);
			}
		}
	}

}
