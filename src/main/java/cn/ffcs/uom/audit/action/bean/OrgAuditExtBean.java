package cn.ffcs.uom.audit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class OrgAuditExtBean {
	@Getter
	@Setter
	private Panel orgAuditExtPanel;
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
	private Listbox orgAuditListbox;
	@Getter
	@Setter
	private Paging orgAuditListboxPaging;
}
