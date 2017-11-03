package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.staff.component.StaffBandboxExt;

/**
 *组织编辑Bean
 * 
 * @author
 **/
public class StaffOrganizationEditBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window staffOrganizationEditWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

	@Getter
	@Setter
	private StaffBandboxExt staffBandboxExt;

	@Getter
	@Setter
	private Listbox ralaCd;

	@Getter
	@Setter
	private Spinner staffSeq;

	@Getter
	@Setter
	private Textbox userCode;

	@Getter
	@Setter
	private Textbox note;
	
	@Getter
	@Setter
	private Textbox reason;

	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveBtn;
}
