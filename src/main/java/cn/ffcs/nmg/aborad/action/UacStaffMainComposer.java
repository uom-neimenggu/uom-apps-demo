package cn.ffcs.nmg.aborad.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uac.staff.action.bean.UacStaffMainBean;
import cn.ffcs.uom.common.util.IPortletInfoProvider;

/**
 * Title: UAC-STAFF INFO MANAGE  
 * Description: CRUD UAC-STAFF Relational INFO
 * Copyright: FFCS© 2017
 * Company:FFCS
 * @author Luis.Zhang
 * @version 0.0.1 date:2017-09-06
 * @since JDK1.6
 */
@Controller
@Scope("prototype")
public class UacStaffMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = -6213315628515060793L;

	private UacStaffMainBean bean = new UacStaffMainBean();

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
		Components.wireVariables(comp, bean);
		bean.getUacStaffListboxExt().setPortletInfoProvider(this);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$uacStaffMainWin() {
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
		this.bean.getUacStaffListboxExt().setPagePosition("uacStaffPage");
	}
}
