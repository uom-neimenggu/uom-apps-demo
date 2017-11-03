package cn.ffcs.uom.position.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 岗位组织管理Bean
 * 
 * @author
 **/
public class PositionOrganizationListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window positionOrganizationListboxComp;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div organizationPositionSearchDiv;
	/**
	 *组织名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 *组织编码.
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 * Listbox.
	 **/
	@Getter
	@Setter
	private Listbox positionOrganizationListBox;
	/**
	 * 岗位新增按钮.
	 */
	@Getter
	@Setter
	private Button addPositionOrganizationButton;
	/**
	 * 岗位删除按钮.
	 */
	@Getter
	@Setter
	private Button delPositionOrganizationButton;
	/**
	 * 组织路径查看按钮.
	 */
	@Getter
	@Setter
	private Button viewOrganizationPathButton;

	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging positionOrganizationListPaging;
}
