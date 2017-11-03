package cn.ffcs.uom.party.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.party.action.bean.PartyDetailBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人的查看
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-28
 * @Email wangyong@ffcs.cn
 * @功能说明：
 *
 */
@Controller
@Scope("prototype")
public class PartyDetailComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
    
    private PartyDetailBean bean = new PartyDetailBean();
    
    private PartyManager  partyManager = (PartyManager) ApplicationContextUtil.getBean("partyManager");
    
    @Resource
    private StaticParameter staticParameter;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Components.wireVariables(comp, bean);
    }
    
    /**
     * window初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$partyDetailWindow() throws Exception {
        bindCombobox();
        bindBean();
    }
    
    /**
     * 绑定下拉框.
     * .
     * 
     * @throws Exception
     * @author Wong
     *         2013-5-30 Wong
     */
    private void bindCombobox() throws Exception {
        List<NodeVo> liTp = UomClassProvider.getValuesList("Party","partyType");
        ListboxUtils.rendererForEdit(bean.getPartyType(), liTp);
        liTp = UomClassProvider.getValuesList("Individual","gender");
        ListboxUtils.rendererForEdit(bean.getGender(), liTp);
        liTp = UomClassProvider.getValuesList("PartyOrganization","orgType");
        ListboxUtils.rendererForEdit(bean.getOrgType(), liTp);
        liTp = UomClassProvider.getValuesList("Individual","politicsStatus");
        ListboxUtils.rendererForEdit(bean.getPoliticsStatus(), liTp);
        liTp = UomClassProvider.getValuesList("Individual","educationLevel");
        ListboxUtils.rendererForEdit(bean.getEducationLevel(), liTp);
        liTp = UomClassProvider.getValuesList("Individual","religion");
        ListboxUtils.rendererForEdit(bean.getReligion(), liTp);
        liTp = UomClassProvider.getValuesList("Individual","nation");
        ListboxUtils.rendererForEdit(bean.getNation(), liTp);
        liTp = UomClassProvider.getValuesList("Organization","orgScale");
        ListboxUtils.rendererForEdit(bean.getOrgScale(), liTp);
    }
    
    /**
     * 页面初始化.
     * 
     * @throws
     * @author
     */
    public void bindBean() throws Exception {
        String opType = StrUtil.strnull(arg.get("opType"));
        bean.getPartyDetailWindow().setTitle("查看参与人");
        String mg = null;
        if(SffOrPtyCtants.VIEW.equals(opType)){
            Party  party = (Party) arg.get("party");
            PubUtil.fillBeanFromPo(party, bean);
            if(SffOrPtyCtants.CONST_INDIVIDUAL.equals(party.getPartyType())){
                Individual indivi = partyManager.getIndividual(party.getPartyId());
                if (null != indivi) {
                    bean.getPel().setVisible(true);
                    bean.getOrgs().setVisible(false);
                    mg = staticParameter.handling(new String[]{"Individual","maritalStatus",indivi.getMarriageStatus()});
                    if(null!=mg){
                        bean.getMarriageStatus().setValue(mg);
                    }
                    mg = staticParameter.handling(new String[]{"Individual","nationality",indivi.getNationality()});
                    if(null!=mg){
                        bean.getNationality().setValue(mg);
                    }
                    PubUtil.fillBeanFromPo(indivi, bean);
                }
                
            }else {
                /**
                 * partyId不能为null
                 */
                if(party.getPartyId()!=null){
                    PartyOrganization partyOrg = partyManager.getPartyOrg(party
                        .getPartyId());
                    if (null != partyOrg) {
                        bean.getOrgs().setVisible(true);
                        bean.getPel().setVisible(false);
                        PubUtil.fillBeanFromPo(partyOrg, bean);
                    }
                }                
            }
            if (party.getPartyId() != null) {
                List<PartyRole> liPr = partyManager.getPartyRoleByPtId(party.getPartyId());
                mg = staticParameter.handling(new String[]{"PartyRole","roleType",liPr.get(0).getRoleType()});
                bean.getRoleType().setValue(mg);
            }
        }
    }
}
