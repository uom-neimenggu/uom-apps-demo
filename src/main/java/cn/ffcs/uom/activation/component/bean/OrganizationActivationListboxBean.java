package cn.ffcs.uom.activation.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class OrganizationActivationListboxBean {

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox organizationActivationListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging organizationActivationListboxPaging;

	/**
	 * 组织批量激活按钮.
	 */
	@Getter
	@Setter
	private Button organizationActBatchButton;

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
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegionBandbox;

}
