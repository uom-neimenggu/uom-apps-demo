package cn.ffcs.uom.publishLog.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 *员工组织关系Bean
 * 
 * @author
 **/
public class StaffPublishDetectionListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel staffPublishDetectionListboxComp;
	/**
	 * 员工姓名.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 员工账号.
	 **/
	@Getter
	@Setter
	private Textbox staffAccount;
	/**
	 * OA账号.
	 **/
	@Getter
	@Setter
	private Textbox staffCode;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox StaffPublishDetectionListBox;
	
	/**
	 * 查看按钮.
	 */
	@Getter
	@Setter
	private Button viewButton;
	
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging StaffPublishDetectionListPaging;
}
