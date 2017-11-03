package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationRelationRelacdBandboxExt;

public class SystemOrgTreeConfigEditBean {
	@Getter
	@Setter
	private Window systemOrgTreeConfigEditWin;
	@Getter
	@Setter
	private Listbox systemNameListbox;
	@Getter
	@Setter
	private Listbox orgTreeNameListbox;
	@Getter
	@Setter
	private Listbox generationSwitchListbox;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
