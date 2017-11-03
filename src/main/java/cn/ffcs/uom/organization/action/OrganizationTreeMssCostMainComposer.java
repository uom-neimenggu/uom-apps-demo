package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.action.bean.OrganizationMssCostTreeMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.JtMssCostRelation;

@Controller
@Scope("prototype")
public class OrganizationTreeMssCostMainComposer extends BasePortletComposer
    implements IPortletInfoProvider {
    /**
	 * 
	 */
    private static final long serialVersionUID = -8064419972929637883L;
    
    /**
     * 页面bean
     */
    private OrganizationMssCostTreeMainBean bean = new OrganizationMssCostTreeMainBean();
    
    @Autowired
    @Qualifier("orgTypeManager")
    private OrgTypeManager orgTypeManager   = (OrgTypeManager) ApplicationContextUtil
                                                                 .getBean("orgTypeManager");
    
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
        this.bean.getMssCostTreeExt().setPortletInfoProvider(this);   
        /**
         * 选中组织树上的组织
         */
        this.bean.getMssCostTreeExt().addForward(
            OrganizationConstant.ON_SELECT_COST_ORGANIZATION_TREE_REQUEST, this.self,
            "onSelectMssCostTreeResponse");       
    }
    
    /**
     * window初始化.
     * 
     * @throws Exception
     *             异常
     */
    public void onCreate$organizationMssCostTreeMainWindow() throws Exception {
        initLeftTab();
        initPage();
        initLeftTabControlRightPage();
    }
    
    /**
     * 设置页面
     */
    private void initPage() throws Exception {
//        this.bean.getMssCostTreeExt().setPagePosition("mssCostTreePage");
        /**
         * 组织信息编辑页面只显示营销组织的类型
         */
        List<String> optionAttrValueList = new ArrayList<String>();
        TreeOrgTypeRule totr = new TreeOrgTypeRule();
        totr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
        List<OrgType> queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
        if (null != queryOrgTypeList && queryOrgTypeList.size() > 0) {
            for (OrgType orgType : queryOrgTypeList) {
                optionAttrValueList.add(orgType.getOrgTypeCd());
            }
        }   
    }
    
    /**
     * 20140612设置tab页用来区分不同tab页的功能权
     */
    private void initLeftTabControlRightPage() throws Exception {
        String selectedTabId = "mssCostTab";
    }
    
    /**
     * 初始化tab页权限
     * 
     * @throws SystemException
     * @throws Exception
     */
    public void initLeftTab() throws Exception {
        boolean checkCostTabResult = PlatformUtil.checkHasPermission(this, ActionKeys.JTMSS_COST_TAB);
        if (!checkCostTabResult) {
            this.bean.getTempTab().setVisible(true);
            this.bean.getTempTab().setSelected(true);
            ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
        } else {
            this.bean.getMssCostTab().setSelected(true);
        }
        bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
    }
    
    /**
     * 直接点击Tab页. .
     */
    public void onClickPartyTab(ForwardEvent event) throws Exception {
        Event origin = event.getOrigin();
        if (origin != null) {
            Component comp = origin.getTarget();
            if (comp != null && comp instanceof Tab) {
                bean.setTab((Tab) comp);
                // callPartyTab();
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
    }

    public void onSelectMssCostTreeResponse(ForwardEvent event){
        JtMssCostRelation jtMssCostRelation = (JtMssCostRelation)event.getOrigin().getData();
        String subSetName = jtMssCostRelation.getSubSetName()== null ? "" : jtMssCostRelation.getSubSetName();
        String subName = jtMssCostRelation.getSubName()== null ? "" : jtMssCostRelation.getSubName();
        this.bean.getSubSetName().setValue(subSetName);
        this.bean.getSubName().setValue(subName);
    }
    
    /**
     * 点击左边的tab
     * 
     * @param forwardEvent
     * @throws Exception
     */
    public void onClickLeftTab(ForwardEvent forwardEvent) throws Exception {
        Event event = forwardEvent.getOrigin();
        if (event != null) {
            Component component = event.getTarget();
            if (component != null && component instanceof Tab) {
                final Tab clickTab = (Tab) component;
                bean.setLeftSelectTab(clickTab);
                callLeftTab();
            }
        }
    }
    
    /**
	 * 
	 */
    public void callLeftTab() throws Exception {
        if (this.bean.getLeftSelectTab() == null) {
            bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
        }
        JtMssCostRelation jtMssCostRelation = null;
        
        if ("mssCostTab".equals(bean.getLeftSelectTab().getId())) {
            jtMssCostRelation = this.bean.getMssCostTreeExt()
                .getSelectOrganizationOrganization();
            
        }
        if (jtMssCostRelation != null && jtMssCostRelation.getSubSetName() != null) {
            // organization = organizationRelation.getOrganization();
        } else {
            // 如果选中的参与人tab要隐藏则默认选中到组织信息页面
        }
        callTab();
    }
    
    /**
     * 删除节点事件,属性tab清空
     * 
     * @throws Exception
     */
    public void onDelNodeResponse() throws Exception {
        this.callLeftTab();
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
        callLeftTab();
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
                if ("mssCostTab".equals(bean.getLeftSelectTab().getId())) {
                    /**
                     * 组织信息保存可能对组织名称进行了修改
                     */
                    Events.postEvent(OrganizationConstant.ON_SAVE_ORGANIZATION_INFO,
                        this.bean.getMssCostTreeExt(), org);
                }
            }
        }
    }
}
