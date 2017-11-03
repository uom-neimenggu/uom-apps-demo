package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.organization.action.bean.ChannelPackareaApproveMainBean;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年9月19日
 * @功能说明：网点包区关系
 */
@Controller
@Scope("prototype")
public class ChannelPackareaApproveMainComposer extends BasePortletComposer
    implements IPortletInfoProvider {
    
    /**
     * .
     */
    private static final long serialVersionUID = -2352432565804164949L;
    
    private ChannelPackareaApproveMainBean bean = new ChannelPackareaApproveMainBean();
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
//        UomZkUtil.autoFitHeight(comp);
        super.doAfterCompose(comp);
        //吧页面相应的值加载到bean中
        Components.wireVariables(comp, bean);
        bean.getChannelPackareaRelationListbox().setPortletInfoProvider(this);
    }
    
    /**
     * 界面初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$channelPackareaApproveMainWin() {
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
        this.bean.getChannelPackareaRelationListbox().setPagePosition("channelPackareaApprovePage");
    }
    
    
    
    @Override
    public ThemeDisplay getThemeDisplay() {
        return super.getThemeDisplay();
    }
    
    @Override
    public String getPortletId() {
        return super.getPortletId();
    }
}
