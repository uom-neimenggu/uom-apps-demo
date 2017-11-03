package cn.ffcs.uom.common.action.bean;

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
 * 删除原因输入
 * 
 * @版权：福富软件 版权所有 (c) 2017
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-7-7
 * @功能说明：
 *
 */
public class DelReasonInputBean{
	
    /**
	 * Window
	 */
	@Setter
	@Getter
	private Window delReasonInputWin;
    
    /**
     * 变更原因.
     */
    @Setter
    @Getter
    private Textbox reason;
    
    @Setter
    @Getter
    private Toolbar btnToolBar;
}
