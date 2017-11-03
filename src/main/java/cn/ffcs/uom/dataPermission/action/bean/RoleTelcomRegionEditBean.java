package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class RoleTelcomRegionEditBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window roleTelcomRegionEditWindow;
	
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegionTreeBandbox;

	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveBtn;
}
