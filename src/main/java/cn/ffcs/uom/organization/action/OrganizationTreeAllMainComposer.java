package cn.ffcs.uom.organization.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;
import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.action.bean.OrganizationTreeAllMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class OrganizationTreeAllMainComposer extends BasePortletComposer
    implements IPortletInfoProvider {
    /**
	 * 
	 */
    private static final long serialVersionUID = -8064419972929637883L;
    
    /**
     * 页面bean
     */
    private OrganizationTreeAllMainBean bean = new OrganizationTreeAllMainBean();
    /**
     * 选中的组织
     */
    private Organization organization;
    
    private StaffManager staffManager = (StaffManager) ApplicationContextUtil.getBean("staffManager");
    
    /**
     * 组织参与人
     */
    private Party party;
    
    /**
     * 选择组织员工返回的组织参与人
     */
    private Party returParty;
    
    /**
     * 推导树节点控制
     */
    private treeCalcAction treeCalcVo;
    
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
        this.bean.getCommonTreeExt().setPortletInfoProvider(this);
        this.bean.getOrganizationRelationListbox().setPortletInfoProvider(this);
        this.bean.getStaffOrganizationListbox().setPortletInfoProvider(this);
        this.bean.getStaffOrganizationListbox().setIsOrgTreePage(true);
        this.bean.getOrganizationRelationListbox().setIsOrgTreePage(true);
        this.bean.getPartyContactInfoListboxExt().setPortletInfoProvider(this);
        this.bean.getPartyCertificationListboxExt().setPortletInfoProvider(this);
        this.bean.getStaffPositionListboxExt().setPortletInfoProvider(this);
        
        /**
         * 选中组织树上的组织
         */
        this.bean.getCommonTreeExt().addForward(
            OrganizationConstant.ON_SELECT_AGENT_ORGANIZATION_TREE_REQUEST, this.self,
            "onSelectOrganizationRelationTreeResponse");
        /**
         * 删除节点成功事件
         */
        this.bean.getCommonTreeExt().addForward(OrganizationConstant.ON_DEL_NODE_OK, this.self,
            "onDelNodeResponse");
        /**
         * 选择代理树
         */
        this.bean.getCommonTreeExt().addForward(OrganizationConstant.ON_SELECT_TREE_TYPE,
            this.self, "onSelectTreeTypeResponse");
        
        /**
         * 组织员工信息选择事件
         */
        this.bean.getStaffOrganizationListbox().addForward(
            OrganizationConstant.ON_STAFFORGANIZATION_SELECT, this.self,
            "onSelectStaffOrganizationResponse");
    }
    
    /**
     * window初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$organizationTreeAllMainWindow() throws Exception {
        // initLeftTab();
        initPage();
        // callLeftTab();// 当只有一个代理商TAB页面时，解决用工性质无法选择代理商员工的BUG
    }
    
    /**
     * 设置页面不区分组织树TAB页
     */
    private void initPage() throws Exception {
        this.bean.getCommonTreeExt().setPagePosition("commonTreePage");
        /**
         * 未区分组织树各TAB页的权限分离
         */
        this.bean.getOrganizationRelationListbox().setPagePosition("orgTreePage");
        this.bean.getStaffOrganizationListbox().setPagePosition("orgTreePage");
        this.bean.getPartyContactInfoListboxExt().setPagePosition("orgTreePage");
        this.bean.getPartyCertificationListboxExt().setPagePosition("orgTreePage");
        this.bean.getStaffPositionListboxExt().setPagePosition("orgTreePage");
    }
    
    /**
     * 选择员工组织列表的响应处理. .
     * 
     * @param event
     * @throws Exception
     * @author faq 2013-7-25 faq
     */
    public void onSelectStaffOrganizationResponse(final ForwardEvent event) throws Exception {
        returParty = (Party) event.getOrigin().getData();
        callPartyTab();
    }
    
    public void callPartyTab() throws Exception {
        if (this.bean.getTab() == null) {
            this.bean.setTab(this.bean.getTabBoxParty().getSelectedTab());
        }
        if (returParty != null) {
            String tab = this.bean.getTab().getId();
            if ("partyContactInfoTab".equals(tab)) {
                bean.getPartyContactInfoListboxExt().setParty(returParty);
                bean.getPartyContactInfoListboxExt().init();
            }
            if ("partyCertificationTab".equals(tab)) {
                bean.getPartyCertificationListboxExt().setParty(returParty);
                bean.getPartyCertificationListboxExt().init();
            }
            if ("staffPositionTab".equals(tab)) {
                String partyRoleId = returParty.getPartyRoleId();
                if (null != partyRoleId) {
                    Staff staff = staffManager.getStaffByPartyRoleId(Long.parseLong(partyRoleId));
                    Events.postEvent(SffOrPtyCtants.ON_STAFF_POSITION_QUERY,
                        this.bean.getStaffPositionListboxExt(), staff);
                }
            }
        } else {
            bean.getPartyContactInfoListboxExt().onCleaningPartyContactInfo();
            bean.getPartyCertificationListboxExt().onCleaningPartyCertification();
            bean.getStaffPositionListboxExt().onCleanStaffPositiRespons(null);
        }
    }
    
    /**
     * 直接点击Tab页. .
     * 
     * @param event
     * @throws Exception
     * @author faq 2013-7-25 faq
     */
    public void onClickPartyTab(ForwardEvent event) throws Exception {
        Event origin = event.getOrigin();
        if (origin != null) {
            Component comp = origin.getTarget();
            if (comp != null && comp instanceof Tab) {
                bean.setTab((Tab) comp);
                callPartyTab();
            }
        }
    }
    
    /**
     * 点击tab
     * 
     * @throws Exception
     */
    public void onClickTab(ForwardEvent forwardEvent) throws Exception {
        Event event = forwardEvent.getOrigin();
        if (event != null) {
            Component component = event.getTarget();
            if (component != null && component instanceof Tab) {
                final Tab clickTab = (Tab) component;
                bean.setSelectTab(clickTab);
                callTab();
            }
        }
    }
    
    /**
     * @throws Exception
     */
    public void callTab() throws Exception {
        if (this.bean.getSelectTab() == null) {
            bean.setSelectTab(this.bean.getTabBox().getSelectedTab());
        }
        if (organization != null) {
            if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
                OrganizationRelation organizationRelation = new OrganizationRelation();
                organizationRelation.setOrgId(organization.getOrgId());
                Events.postEvent(OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
                    this.bean.getOrganizationRelationListbox(), organizationRelation);
                
            }
            if ("orgStaffTab".equals(this.bean.getSelectTab().getId())) {
                StaffOrganization staffOrganization = new StaffOrganization();
                staffOrganization.setOrgId(organization.getOrgId());
                /**
                 * 推导树组织员工使用(员工信息在treeCalcVo中)
                 */
                staffOrganization.setTreeCalcVo(treeCalcVo);
                Events.postEvent(StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
                    this.bean.getStaffOrganizationListbox(), staffOrganization);
            }
        } else {
            /**
             * 切换左边tab页的时候，未选择组织树上的组织，清理数据等操作
             */
            if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
                Events.postEvent(OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
                    this.bean.getOrganizationRelationListbox(), null);
                
            }
            if ("orgStaffTab".equals(this.bean.getSelectTab().getId())) {
                Events.postEvent(StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
                    this.bean.getStaffOrganizationListbox(), null);
            }
        }
    }
    
    /**
     * 选择组织树
     * 
     * @param event
     */
    public void onSelectOrganizationRelationTreeResponse(ForwardEvent event) throws Exception {
        OrganizationRelation organizationRelation = (OrganizationRelation) event.getOrigin()
            .getData();
        /**
         * 推导树节点数据,其他为null
         */
        treeCalcVo = organizationRelation.getTreeCalcVo();
        if (organizationRelation != null && organizationRelation.getOrgId() != null) {
            organization = organizationRelation.getOrganization();
            if (organization != null) {
                party = organization.getParty();
            }
            callTab();
        }
    }
    
    /**
     * 删除节点事件,属性tab清空
     * 
     * @throws Exception
     */
    public void onDelNodeResponse() throws Exception {
        // this.callLeftTab();
    }
    
    /**
     * 选择推导树
     * 
     * @throws Exception
     */
    public void onSelectTreeTypeResponse(ForwardEvent event) throws Exception {
        boolean isDuceTree = false;
        if (event.getOrigin().getData() != null) {
            isDuceTree = (Boolean) event.getOrigin().getData();
        }
        this.setTreeTypeToRightTab(isDuceTree);
        // callLeftTab();
    }
    
    /**
     * 设置右边属性tab是否是推导树
     */
    private void setTreeTypeToRightTab(boolean isDuceTree) {
        this.bean.getOrganizationRelationListbox().setDuceTree(isDuceTree);
        this.bean.getStaffOrganizationListbox().setDuceTree(isDuceTree);
        this.bean.getPartyCertificationListboxExt().setDuceTree(isDuceTree);
        this.bean.getPartyContactInfoListboxExt().setDuceTree(isDuceTree);
        this.bean.getStaffPositionListboxExt().setDuceTree(isDuceTree);
    }
    
    /**
     * 组织信息保存
     * 
     * @param event
     * @throws Exception
     */
    public void onSaveOrganizationInfoResponse(ForwardEvent event) throws Exception {
        if (event.getOrigin().getData() != null) {
            Organization org = (Organization) event.getOrigin().getData();
            if (org != null) {
                /**
                 * 清楚对象缓存
                 */
                org.setOrgTypeList(null);
            }
        }
    }
}
