package cn.ffcs.uom.organization.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.organization.action.ChannelPackareaRelationListboxComposer;
import cn.ffcs.uom.organization.constants.ChannelPackareaRelationConstant;
import cn.ffcs.uom.organization.constants.OrganizationConstant;

@Controller
@Scope("prototype")
public class ChannelPackareaRelationBandboxExt extends Bandbox implements IdSpace {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    //channelPackareaRelationBandboxExt
    private final String zul = "/pages/organization/comp/channel_packarea_relation_bandbox_ext.zul";
    
    /**
     * 网点包区列表
     */
    private ChannelPackareaRelationListboxComposer channelPackareaRelationListbox;
    /**
     * 对应流程的id号
     */
    private Long processId;
    
    
    public ChannelPackareaRelationBandboxExt()
    {
        Executions.createComponents(this.zul, this, null);
        // 2. Wire variables (optional)
        Components.wireVariables(this, this);
        // 3. Wire event listeners (optional)
        Components.addForwards(this, this, '$');
    }
    
    public void onCreate()
    {
        if(processId != null)
        {
            Events.postEvent(ChannelPackareaRelationConstant.ON_PROCESS_APPROVE_REQUEST, this.channelPackareaRelationListbox, processId);
        }
    }
    

    public ChannelPackareaRelationListboxComposer getChannelPackareaRelationListbox() {
        return channelPackareaRelationListbox;
    }

    public void setChannelPackareaRelationListbox(
        ChannelPackareaRelationListboxComposer channelPackareaRelationListbox) {
        this.channelPackareaRelationListbox = channelPackareaRelationListbox;
    }
}
