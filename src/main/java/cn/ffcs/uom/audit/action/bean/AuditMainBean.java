package cn.ffcs.uom.audit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.audit.component.OrgAuditExt;
import cn.ffcs.uom.audit.component.StaffAuditExt;

public class AuditMainBean {
	@Getter
	@Setter
	private Window auditMainWin;
	/**
	 * tabBox
	 */
	@Getter
	@Setter
	private Tabbox tabBox;
	/**
	 * 当前选中的tab
	 */
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private Tab staffAuditTab;
	@Getter
	@Setter
	private Tab orgAuditTab;
	@Getter
	@Setter
	private Tab tempTab;
	@Getter
	@Setter
	private Tabpanel staffAuditExtPanel;
	@Getter
	@Setter
	private Tabpanel orgAuditExtPanel;
	@Getter
	@Setter
	private StaffAuditExt staffAuditExt;
	@Getter
	@Setter
	private OrgAuditExt orgAuditExt;

}
