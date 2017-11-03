package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.action.bean.UnitedDirectoryMainBean;

/**
 * 组织管理.
 * 
 * @author xuxs
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class UnitedDirectoryMainComposer extends BasePortletComposer implements IPortletInfoProvider{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7031627370196867516L;
	
	/**
	 * bean.
	 */
	private UnitedDirectoryMainBean bean = new UnitedDirectoryMainBean();

	
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
        super.doAfterCompose(comp);
        UomZkUtil.autoFitHeight(comp);
        Components.wireVariables(comp, bean);
    }
}
