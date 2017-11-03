package cn.ffcs.uom.audit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class StaffAuditExtBean {
	@Getter
	@Setter
	private Panel staffAuditExtPanel;
	@Setter
	@Getter
	private Datebox updateDate;
	@Setter
	@Getter
	private Button exportStatisticsButton;
	@Setter
	@Getter
	private Button exportDetailsButton;
	@Getter
	@Setter
	private Listbox systemDomainListbox;
	@Getter
	@Setter
	private Listbox StaffAuditListbox;
	@Getter
	@Setter
	private Paging StaffAuditListboxPaging;
}
