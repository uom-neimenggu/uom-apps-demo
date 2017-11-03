package cn.ffcs.uom.restservices.action;

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
import cn.ffcs.uom.restservices.action.bean.GrpStaffMainBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;

/**
 * 渠道管理.
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 */
@Controller
@Scope("prototype")
public class GrpStaffMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private GrpStaffMainBean bean = new GrpStaffMainBean();

	/**
	 * grpStaff.
	 */
	private GrpStaff grpStaff;

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
		bean.getGrpStaffListboxExt().setPortletInfoProvider(this);
		bean.getGrpStaffChannelRelaListboxExt().setPortletInfoProvider(this);
		bean.getGrpStaffListboxExt().addForward(
				ChannelInfoConstant.ON_SELECT_GRP_STAFF, comp,
				ChannelInfoConstant.ON_SELECT_GRP_STAFF_RESPONS);
		bean.getGrpStaffListboxExt().addForward(
				ChannelInfoConstant.ON_GRP_STAFF_CLEAR_TABS, comp,
				ChannelInfoConstant.ON_GRP_STAFF_CLEAR_TABS_RESPONS);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$grpStaffMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择渠道列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onSelectGrpStaffResponse(final ForwardEvent event)
			throws Exception {
		grpStaff = (GrpStaff) event.getOrigin().getData();
		callTab();
	}

	/**
	 * tab响应
	 */
	public void callTab() {
		try {
			if (bean.getTab() == null) {
				bean.setTab(bean.getTabBox().getSelectedTab());
			}
			if (grpStaff != null) {
				String tab = this.bean.getTab().getId();
				if ("grpStaffChannelRelaTab".equals(tab)) {
					GrpStaffChannelRela grpStaffChannelRela = new GrpStaffChannelRela();
					grpStaffChannelRela.setSalesCode(grpStaff.getSalesCode());
					Events.postEvent(
							ChannelInfoConstant.ON_GRP_STAFF_CHANNEL_RELA_QUERY,
							bean.getGrpStaffChannelRelaListboxExt(),
							grpStaffChannelRela);
				}
			} else {
				bean.getGrpStaffChannelRelaListboxExt()
						.onCleanGrpStaffChannelRelaRespons(null);
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
	 * 处理点击后Tabs .
	 * 
	 * @param event
	 * @author 朱林涛
	 */
	public void onGrpStaffClearTabsRespons(final ForwardEvent event) {
		Events.postEvent(ChannelInfoConstant.ON_CLEAN_GRP_STAFF_CHANNEL_RELA,
				bean.getGrpStaffChannelRelaListboxExt(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getGrpStaffListboxExt().setPagePosition("grpStaffPage");
		this.bean.getGrpStaffChannelRelaListboxExt().setPagePosition(
				"grpStaffPage");
	}
}
