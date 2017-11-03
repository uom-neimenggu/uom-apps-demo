package cn.ffcs.uom.group.action;

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
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.group.action.bean.GroupGuidMainBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 集团数据管理Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-08-18
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class GroupGuidMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private GroupGuidMainBean bean = new GroupGuidMainBean();

	/**
	 * staff.
	 */
	private Staff staff;

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
		bean.getStaffListboxExt().setPortletInfoProvider(this);
		bean.getGroupListboxExt().setPortletInfoProvider(this);
		bean.getStaffListboxExt().addForward(SffOrPtyCtants.ON_STAFF_SELECT,
				comp, "onSelectStaffResponse");
		bean.getStaffListboxExt().setStaffWindowDivVisible(false);
		bean.getGroupListboxExt().setVariablePagePosition("groupGuidPage");
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$groupGuidMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择员工列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onSelectStaffResponse(final ForwardEvent event)
			throws Exception {
		staff = (Staff) event.getOrigin().getData();
		callTab();
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
			if (staff != null) {
				String tab = this.bean.getTab().getId();
				if ("groupTab".equals(tab)) {
					Events.postEvent(SffOrPtyCtants.ON_STAFF_GROUP_QUERY,
							this.bean.getGroupListboxExt(), staff);
				}
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
	@SuppressWarnings("unused")
	private void initPage() throws Exception {
		this.bean.getStaffListboxExt().setPagePosition("groupGuidPage");
		this.bean.getGroupListboxExt().setPagePosition("groupGuidPage");
	}
}
