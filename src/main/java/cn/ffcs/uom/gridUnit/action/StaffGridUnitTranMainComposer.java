package cn.ffcs.uom.gridUnit.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.gridUnit.action.bean.StaffGridUnitTranMainBean;

/**
 * 网格管理实体Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-22
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffGridUnitTranMainComposer extends BasePortletComposer
		implements IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private StaffGridUnitTranMainBean bean = new StaffGridUnitTranMainBean();

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
		bean.getStaffGridUnitTranListboxExt().setPortletInfoProvider(this);
		this.bean.getStaffGridUnitTranListboxExt().setVariablePagePosition(
				"staffGridUnitTranPage");
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffGridUnitTranMainWin() {
		try {
			initPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getStaffGridUnitTranListboxExt().setPagePosition(
				"staffGridUnitTranPage");
	}
}
