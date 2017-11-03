package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 *组织管理Bean
 * 
 * @author
 **/
public class OrganizationRelationListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel organizationRelationListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox OrganizationRelationListBox;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div organizationRelationSearchDiv;
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
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addOrganizationRelationButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delOrganizationRelationButton;
	/**
	 * 组织路径查看按钮.
	 */
	@Getter
	@Setter
	private Button showOrganizationPathButton;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging OrganizationRelationListPaging;
}
