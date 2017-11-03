package cn.ffcs.uom.group.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * 员工组织关系Bean
 * 
 * @author
 **/
public class GroupUomOrgListboxExtBean {
	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel groupUomOrgListboxExtPanel;
	/**
	 * 集团组织名称.
	 **/
	@Getter
	@Setter
	private Textbox ktext;
	/**
	 * Listbox.
	 **/
	@Getter
	@Setter
	private Listbox groupUomOrgListBox;
	/**
	 * 新增按钮.
	 */
	@Getter
	@Setter
	private Button addGroupUomOrgButton;
	/**
	 * 修改按钮.
	 */
	@Getter
	@Setter
	private Button editGroupUomOrgButton;
	/**
	 * 删除按钮.
	 */
	@Getter
	@Setter
	private Button delGroupUomOrgButton;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging groupUomOrgListPaging;
}
