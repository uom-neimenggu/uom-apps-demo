package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.position.component.CtPositionTreeBandbox;

public class StaffCtPositionEditBean {
	@Getter
	@Setter
	private Window staffCtPositionEditWindow;
	@Getter
	@Setter
	private Textbox staffName;
	
	/**
     * 岗位
     */
    @Setter
    @Getter
    private CtPositionTreeBandbox ctPosition;
    
    @Getter
	@Setter
	private Listbox ralaCd;
    
    /**
     * 组织
     */
    @Setter
    @Getter
    private Listbox organization;
    
	@Getter
	@Setter
	private Button saveBtn;
	@Getter
	@Setter
	private Button cancelBtn;
}
