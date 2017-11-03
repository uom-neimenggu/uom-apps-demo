package cn.ffcs.uom.bpm.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.bpm.bean.PrincipalConfigMainBean;
import cn.ffcs.uom.bpm.constants.BmpConstants;
import cn.ffcs.uom.bpm.manager.BmpSystemConfigManager;
import cn.ffcs.uom.bpm.model.QaUnPrincipal;
import cn.ffcs.uom.common.util.UomZkUtil;

@Controller
@Scope("prototype")
public class PrincipalConfigMainComposer extends BasePortletComposer {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 3414801544961910332L;
    /**
     * 页面bean
     */
    private PrincipalConfigMainBean bean = new PrincipalConfigMainBean();
    
    /**
     * 选中的
     */
    private QaUnPrincipal principal;
    @Autowired
    private BmpSystemConfigManager bmpSystemConfigManager;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        UomZkUtil.autoFitHeight(comp);
        Components.wireVariables(comp, bean);
        this.bean.getPrincipalListExt().addForward(BmpConstants.ON_PRINCIPAL_SELECT, this.self,
            "onSelectPrincipalResponse");
    }
    
    /**
     * 界面初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$principalConfigMainWin() throws Exception {

    }
    
    public void onSelectPrincipalResponse(final ForwardEvent event){
        try {
        	principal = (QaUnPrincipal) event.getOrigin().getData();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
