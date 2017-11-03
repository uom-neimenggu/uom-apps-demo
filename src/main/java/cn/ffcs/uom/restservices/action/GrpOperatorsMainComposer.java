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
import cn.ffcs.uom.restservices.action.bean.GrpOperatorsMainBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.model.GrpOperators;

/**
 * 经营主体管理.
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 */
@Controller
@Scope("prototype")
public class GrpOperatorsMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private GrpOperatorsMainBean bean = new GrpOperatorsMainBean();

	/**
	 * grpOperators.
	 */
	private GrpOperators grpOperators;

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
		bean.getGrpOperatorsListboxExt().setPortletInfoProvider(this);
		bean.getGrpChannelOperatorsRelaListboxExt()
				.setPortletInfoProvider(this);
		bean.getGrpOperatorsListboxExt().addForward(
				ChannelInfoConstant.ON_GRP_OPERATORS_SELECT, comp,
				ChannelInfoConstant.ON_SELECT_GRP_OPERATORS_RESPONS);
		bean.getGrpOperatorsListboxExt().addForward(
				ChannelInfoConstant.ON_GRP_OPERATORS_CLEAR_TABS, comp,
				ChannelInfoConstant.ON_GRP_OPERATORS_CLEAR_TABS_RESPONS);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$grpOperatorsMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择经营主体列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author 朱林涛
	 */
	public void onSelectGrpOperatorsResponse(final ForwardEvent event)
			throws Exception {
		grpOperators = (GrpOperators) event.getOrigin().getData();
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
			if (grpOperators != null) {
				String tab = this.bean.getTab().getId();
				if ("grpChannelOperatorsRelaTab".equals(tab)) {
					Events.postEvent(
							ChannelInfoConstant.ON_GRP_CHANNEL_OPERATORS_RELA_QUERY,
							bean.getGrpChannelOperatorsRelaListboxExt(),
							grpOperators);
				}
			} else {
				bean.getGrpChannelOperatorsRelaListboxExt()
						.onCleanGrpChannelOperatorsRelaRespons(null);
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
	public void onGrpOperatorsClearTabsRespons(final ForwardEvent event) {
		Events.postEvent(
				ChannelInfoConstant.ON_CLEAN_GRP_CHANNEL_OPERATORS_RELA,
				bean.getGrpChannelOperatorsRelaListboxExt(), null);
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getGrpOperatorsListboxExt().setPagePosition(
				"grpOperatorsPage");
		this.bean.getGrpChannelOperatorsRelaListboxExt().setPagePosition(
				"grpOperatorsPage");
	}
}
