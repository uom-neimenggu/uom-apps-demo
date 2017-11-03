package cn.ffcs.uom.group.action;

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
import cn.ffcs.uom.group.action.bean.GroupUomMainBean;

/**
 * 集团数据与主数据关系管理.
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-02-03
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class GroupUomMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private GroupUomMainBean bean = new GroupUomMainBean();

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
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getGroupUomOrgListboxExt().setPortletInfoProvider(this);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$groupUomMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * tab响应
	 * 
	 * @throws Exception
	 * 
	 */
	public void callTab() {
		try {

			if (bean.getTab() == null) {
				bean.setTab(bean.getTabBox().getSelectedTab());
			}

			String tab = this.bean.getTab().getId();
			if ("groupUomOrgTab".equals(tab)) {
				// Events.postEvent(SffOrPtyCtants.ON_STAFF_Org_QUERY,
				// this.bean.getGroupUomOrgListboxExt(), staff);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClickTab(ForwardEvent event) {
		try {
			Event origin = event.getOrigin();
			if (origin != null) {
				Component comp = origin.getTarget();
				if (comp != null && comp instanceof Tab) {
					bean.setTab((Tab) comp);
					callTab();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getGroupUomOrgListboxExt().setPagePosition("groupUomPage");
	}
}
