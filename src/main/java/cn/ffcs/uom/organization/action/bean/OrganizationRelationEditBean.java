package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.organization.component.OrganizationRelationTreeBandboxExt;

/**
 *组织编辑Bean
 * 
 * @author
 **/
public class OrganizationRelationEditBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window organizationRelationEditWindow;
	
	@Getter
	@Setter
	private OrganizationBandboxExt org;
	
	@Getter
	@Setter
	private OrganizationRelationTreeBandboxExt relaOrganizaitonRelation;

	@Getter
	@Setter
	private Listbox relaCd;
	
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
