package cn.ffcs.uom.group.component.bean;

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
 * 组织编辑Bean
 * 
 * @author
 **/
public class GroupUomOrgEditBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window groupUomOrgEditWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

	@Getter
	@Setter
	private Listbox resType;

	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveBtn;

	/**
	 * 取消按钮 .
	 */
	@Getter
	@Setter
	private Button cancelBtn;
}
