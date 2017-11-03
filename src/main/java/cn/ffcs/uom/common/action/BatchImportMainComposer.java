package cn.ffcs.uom.common.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.action.bean.BatchImportMainBean;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.organization.action.OrganizationListboxComposer;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.staff.component.StaffListboxExt;
import cn.ffcs.uom.staff.component.StaffOrgTranListboxExt;
import cn.ffcs.uom.staffrole.action.StaffRoleListboxComposer;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年5月12日
 * @功能说明：批量导入集中页面
 *
 */
@Controller
@Scope("prototype")
public class BatchImportMainComposer extends BasePortletComposer implements
IPortletInfoProvider {

    private static final long serialVersionUID = 1L;
    
    private BatchImportMainBean bean = new BatchImportMainBean();
    
    private StaffListboxExt staffListboxExt = new StaffListboxExt();
    
    private StaffOrganizationListboxComposer staffOrgComp = new StaffOrganizationListboxComposer();
    
    private StaffOrgTranListboxExt staffOrgTranListboxExt = new StaffOrgTranListboxExt();
    
    private StaffRoleListboxComposer staffRoleListboxComposer = new StaffRoleListboxComposer();
    
    private OrganizationListboxComposer organizationListboxComposer = new OrganizationListboxComposer();
    
    private PartyCertificationListboxExt partyCertificationListboxExt = new PartyCertificationListboxExt();
    
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
    }
    
    /**
	 * 文件上传 导入员工
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$staffImport(ForwardEvent event) throws Exception {
		staffListboxExt.onUpload$uploadButton(event);
	}
    
	/**
	 * 下载员工导入模板
	 */
	public void onDownloadStaffTemplate() {
		staffListboxExt.onDownloadTemplate();
	}
	
	/**
	 * 导入员工组织关系
	 * @throws Exception
	 */
	public void onStaffOrganizationImport() throws Exception {
		staffOrgComp.onStaffOrganizationImport();
	}
	
	/**
	 * 文件上传 导入员工组织业务关系
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$staffOrgTranImport(ForwardEvent event) throws Exception {
		staffOrgTranListboxExt.onUpload$uploadButton(event);
	}
	
	/**
	 * 下载员工组织业务关系模板
	 */
	public void onDownloadStaffOrgTranTemplate() {
		staffOrgTranListboxExt.onDownloadTemplate();
	}
	
	/**
	 * 文件上传 导入员工角色关系
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$staffRoleImport(ForwardEvent event) throws Exception {
		staffRoleListboxComposer.onUpload$uploadButton(event);
	}
	
	/**
	 * 下载员工角色关系模板
	 */
	public void onDownloadStaffRoleTemplate() {
		staffRoleListboxComposer.onDownloadTemplate();
	}
	
	/**
	 * 导入参与人证件
	 * @throws Exception
	 */
	public void onPartyCertificationImport() throws Exception {
		partyCertificationListboxExt.onPartyCertificationImport();
	}
	
	/**
	 * 导入组织
	 * @throws Exception
	 */
	public void onOrganizationImport() throws Exception {
		organizationListboxComposer.onOrganizationImport();
	}
	
	
}
