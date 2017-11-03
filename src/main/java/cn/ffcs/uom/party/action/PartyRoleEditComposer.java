package cn.ffcs.uom.party.action;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.api.Listitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.party.action.bean.PartyRoleEditBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-25
 * @Email wangyong@ffcs.cn
 * @功能说明：
 *
 */
@Controller
@Scope("prototype")
public class PartyRoleEditComposer extends BasePortletComposer{

    private static final long serialVersionUID = 1L;
    
    /**
     * 操作类型.
     */
    private String opType  = null;// 操作类型
    
    private PartyRoleEditBean bean = new PartyRoleEditBean();
    
    private PartyManager  partyManager = (PartyManager) ApplicationContextUtil.getBean("partyManager");
    
    PartyRole partyRole ;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Components.wireVariables(comp, bean);
    }
    
    /**
     * window初始化.
     * 
     * @throws Exception    异常
     */
    public void onCreate$partyRoleEditWin() throws Exception {
        bindCombobox();
        bindBean();
        bindEvent();
    }
    
    private void bindCombobox(){
        List<NodeVo> liTp = UomClassProvider.getValuesList("PartyRole","roleType");
        ListboxUtils.rendererForEdit(bean.getRoleType(), liTp);
    }
    
    /**
     * 页面初始化.
     * @throws
     * @author
     */
    public void bindBean() {
        
        
        try {
            opType = StrUtil.strnull(arg.get("opType"));
            if (SffOrPtyCtants.ADD.equals(opType)) {
                bean.getPartyRoleEditWin().setTitle("参与人角色新增");
                Party party = (Party) arg.get("party");
                bean.getPartyName().setValue(party.getPartyName());
            }else if (SffOrPtyCtants.MOD.equals(opType)){
                bean.getPartyRoleEditWin().setTitle("参与人角色修改");
                
                Party party = (Party) arg.get("party");
                partyRole = (PartyRole)arg.get("partyRole");
                bean.getPartyName().setValue(party.getPartyName());
                PubUtil.fillBeanFromPo(partyRole, bean);
            }
        } catch (WrongValueException e) {
            e.printStackTrace();
        }
    }
    
    private void bindEvent(){
        try {
            PartyRoleEditComposer.this.bean.getPartyRoleEditWin().addEventListener("onPartyRoleChange",
                new EventListener() {
                    @SuppressWarnings("unchecked")
					public void onEvent(final Event event) throws Exception {
                        if (!StrUtil.isNullOrEmpty(event.getData())) {
                            PartyRoleEditComposer.this.arg = (HashMap) event.getData();
                            bindBean();
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onOk(){
        if (SffOrPtyCtants.ADD.equals(opType)) {
            
            Listitem litem = bean.getRoleType().getSelectedItem();
            if (null == litem) {
                ZkUtil.showError("请选择相应的参与人角色", "提示信息");
            }
            Party party = (Party) arg.get("party");
            partyRole = new PartyRole();
            PubUtil.fillPoFromBean(bean, partyRole);
            partyRole.setPartyId(party.getPartyId());
            
            partyManager.addPartyRole(partyRole);
            
        }else if(SffOrPtyCtants.MOD.equals(opType)){
            
            PubUtil.fillPoFromBean(bean, partyRole);
            partyManager.updatePartyRole(partyRole);
        }
        
        Events.postEvent(SffOrPtyCtants.ON_OK, this.self, partyRole);
        //Events.postEvent(SffOrPtyCtants.ON_OK, bean.getPartyRoleEditWin(), partyRole);
        bean.getPartyRoleEditWin().onClose();
    }
    
    public void onCancel(){
        bean.getPartyRoleEditWin().onClose();
    }
}
