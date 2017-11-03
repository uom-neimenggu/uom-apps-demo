package cn.ffcs.uom.report.action;

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
import cn.ffcs.uom.report.bean.RealnameDataReportMainBean;

/**
 * 实名制数据统计报表
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-06-14
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class RealnameDataReportMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private RealnameDataReportMainBean bean = new RealnameDataReportMainBean();

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
		this.bean.getRealnameDataReportExt().setPortletInfoProvider(this);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$realnameDataReportMainWin() throws Exception {
		this.initPage();
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getRealnameDataReportExt().setPagePosition("realnameReportPage");
	}
}
