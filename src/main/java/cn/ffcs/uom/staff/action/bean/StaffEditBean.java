package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.party.component.PartyBandboxExt;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.staff.component.StaffExtendAttrExt;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;

/**
 * 
 * .员工详细信息
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 *
 */
public class StaffEditBean{
	
    /**
	 * Window
	 */
	@Setter
	@Getter
	private Window staffEditComposer;
	
	/**
	 * 员工名称.
	 */
	@Setter
	@Getter
	private Textbox staffName;
    
    /**
     * 员工编码.
     */
    @Setter
    @Getter
    private Textbox staffCode;
    
    /**
     * 用工性质.
     */
    @Setter
    @Getter
    private TreeChooserBandbox workProp;
	
    /**
     * 人员属性.
     */
    @Setter
    @Getter
    private TreeChooserBandbox staffProperty;
    
    /**
     * 兼职/全职.
     */
    @Setter
    @Getter
    private Listbox partTime;    

    /**
     * 员工职位.
     */
    @Setter
    @Getter
    private Listbox staffPosition;
    
    /**
     * 职务标注.
     */
    @Setter
    @Getter
    private Textbox titleNote;
    
    /**
     * 备注.
     */
    @Setter
    @Getter
    private Textbox remark;
    
    
    /**
     * 员工描述.
     */
    @Setter
    @Getter
    private Textbox staffDesc;
    
    /**
     * 变更原因.
     */
    @Setter
    @Getter
    private Textbox reason;
    
    /**
     * 参与人空间
     */
    @Setter
    @Getter
    private PartyBandboxExt partyBandboxExt;
    
    /**
     * 人力工号.
     */
    @Setter
    @Getter
    private Textbox hrNumber;
    
    /**
     * 员工账号.
     */
    @Setter
    @Getter
    private Textbox staffAccount;
    
    @Setter
    @Getter
    private Textbox staffPassword;
    
    /**
     * 员工扩展属性
     */
    @Setter
    @Getter
    private StaffExtendAttrExt staffExtendAttrExt;
    
    @Setter
    @Getter
    private Toolbar btnToolBar;
    
    @Setter
    @Getter
    private Toolbar viewBtnTB;
    
    @Getter
    @Setter
    private StaffRoleTreeBandboxExt staffRoleBandboxExt;
    
}
