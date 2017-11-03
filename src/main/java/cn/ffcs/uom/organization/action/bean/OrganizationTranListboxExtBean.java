package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class OrganizationTranListboxExtBean {

	@Getter
	@Setter
	private Button addOrganizationTranButton;

	@Getter
	@Setter
	private Button editOrganizationTranButton;

	@Getter
	@Setter
	private Button delOrganizationTranButton;

	@Getter
	@Setter
	private Button showOrganizationPathButton;

	@Getter
	@Setter
	private Listbox organizationTranListbox;

	@Getter
	@Setter
	private Paging organizationTranListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div organizationTranSearchDiv;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div tranRelaTypeDiv;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div telcomRegionDiv;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div organizationTranButtonDiv;

	/**
	 * 组织业务关系类型.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox tranRelaType;
	/**
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegion;
	/**
	 * 组织名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 * 组织编码.
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 * 业务组织名称.
	 **/
	@Getter
	@Setter
	private Textbox tranOrgName;
	/**
	 * 业务组织编码.
	 **/
	@Getter
	@Setter
	private Textbox tranOrgCode;

}
