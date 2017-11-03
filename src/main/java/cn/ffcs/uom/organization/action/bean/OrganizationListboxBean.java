package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

/**
 * 组织管理Bean
 * 
 * @author
 **/
public class OrganizationListboxBean {
	/**
	 * panel.
	 **/
	@Getter
	@Setter
	private Panel organizationListboxComp;
	/**
	 * Listbox.
	 **/
	@Getter
	@Setter
	private Listbox organizationListBox;
	/**
	 * 组织类型.
	 **/
	@Getter
	@Setter
	private Listbox orgType;
	/**
	 * 存在类型.
	 **/
	@Getter
	@Setter
	private Listbox existType;
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
	 * 组织导入按钮
	 */
	@Getter
	@Setter
	private Button importOrganizationButton;
	/**
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addOrganizationButton;
	/**
	 * 组织更新按钮.
	 */
	@Getter
	@Setter
	private Button updateOrganizationButton;
	/**
	 * 组织关系更新按钮.
	 */
	@Getter
	@Setter
	private Button updateOrganizationRelaButton;
	/**
	 * 组织查看按钮.
	 */
	@Getter
	@Setter
	private Button showOrganizationButton;
	/**
	 * 组织编辑按钮.
	 */
	@Getter
	@Setter
	private Button editOrganizationButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delOrganizationButton;
	/**
	 * 新增参与人按钮.
	 */
	@Getter
	@Setter
	private Button addPartyButton;

	/**
	 * tabBox.
	 */
	@Getter
	@Setter
	private Tabbox tabBox;
	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging organizationListPaging;
	/**
	 * 查询使用
	 */
	@Getter
	@Setter
	private Div organizationWindowDiv;
	/**
	 * bandbox使用
	 */
	@Getter
	@Setter
	private Div organizationBandboxDiv;
	/**
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegion;
}
