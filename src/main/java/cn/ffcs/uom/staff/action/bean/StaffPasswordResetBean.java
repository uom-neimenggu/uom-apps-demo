package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationRelationTreeBandboxExt;

public class StaffPasswordResetBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffPasswordResetWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel staffPasswordResetListboxPanel;
	
	
    /**
     * ListBox
     */
    @Getter
    @Setter
    private Listbox staffListbox;
    
    /**
     * 分页插件
     */
    @Getter
    @Setter
    private Paging staffListboxPaging;
    
   
    @Getter
    @Setter
    private Textbox staffAccount;
    
    @Getter
    @Setter
    private Button passwordResetButton;
}
