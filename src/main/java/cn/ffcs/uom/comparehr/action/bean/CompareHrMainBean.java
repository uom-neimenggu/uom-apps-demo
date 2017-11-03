package cn.ffcs.uom.comparehr.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationRelationTreeBandboxExt;

/**
 * 人力比较Bean.
 * 
 * @author faq
 **/
public class CompareHrMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window compareMainWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel compareListboxPanel;
	/**
	 * listbox
	 */
	@Getter
	@Setter
	private Listbox compareListbox;

	/**
	 * paging
	 */
	@Getter
	@Setter
	private Paging compareListPaging;
	
	@Getter
    @Setter
    private Textbox staffCode;
    
    @Getter
    @Setter
    private Textbox staffAccount;
    
    @Getter
    @Setter
    private Textbox staffName;

    @Getter
    @Setter
    private Listbox currentStatusListbox;
	@Getter
	@Setter
	private OrganizationRelationTreeBandboxExt organizationRelationTreeBandboxExt;
	@Getter
	@Setter
	private Button reviseButton;
}
