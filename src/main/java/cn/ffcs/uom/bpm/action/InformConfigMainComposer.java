package cn.ffcs.uom.bpm.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.bpm.bean.InformConfigMainBean;
import cn.ffcs.uom.bpm.constants.BmpConstants;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.common.util.UomZkUtil;

@Controller
@Scope("prototype")
public class InformConfigMainComposer extends BasePortletComposer {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 3414801544961910332L;
    /**
     * 页面bean
     */
    private InformConfigMainBean bean = new InformConfigMainBean();
    
    /**
     * 选中的
     */
    private QaInformMethod qaInformMethod;
    @Autowired
    private QaInformMethodManager qaInformMethodManager;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        UomZkUtil.autoFitHeight(comp);
        Components.wireVariables(comp, bean);
        this.bean.getInformMethodListExt().addForward(BmpConstants.ON_INFORM_METHOD_SELECT, this.self,
            "onSelectInformMethodResponse");
    }
    
    /**
     * 界面初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$informConfigMainWin() throws Exception {
        this.bean.getInformTemplate().setReadonly(true);
    }
    
    public void onSelectInformMethodResponse(final ForwardEvent event){
        try {
            qaInformMethod = (QaInformMethod) event.getOrigin().getData();
            if (qaInformMethod != null) {
                this.bean.getInformTemplate().setValue(qaInformMethod.getInformTemplate());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void onEditInformTemplate(){
        this.bean.getInformTemplate().setReadonly(false);
    }
    
    public void onSaveInformTemplate(){
        qaInformMethod.setInformTemplate(this.bean.getInformTemplate().getValue());
        qaInformMethodManager.saveInformMethod(qaInformMethod);
        this.bean.getInformTemplate().setReadonly(true);
    }
    
}
