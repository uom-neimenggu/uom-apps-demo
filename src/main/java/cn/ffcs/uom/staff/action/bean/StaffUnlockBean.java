package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class StaffUnlockBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffUnlockWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel staffUnlockListboxPanel;
	
	
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
    private Button unlockButton;
}
