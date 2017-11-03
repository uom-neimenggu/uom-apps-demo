package cn.ffcs.uom.group.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * 集团数据管理Bean
 * 
 * @author
 **/
public class GroupListboxExtBean {
	/**
	 * Panel
	 **/
	@Getter
	@Setter
	private Panel groupListboxExtPanel;
	/**
	 * Textbox
	 **/
	@Getter
	@Setter
	private Textbox userName;
	/**
	 * Textbox
	 **/
	@Getter
	@Setter
	private Textbox ctHrUserCode;
	/**
	 * Textbox
	 **/
	@Getter
	@Setter
	private Textbox loginName;
	/**
	 * Textbox
	 **/
	@Getter
	@Setter
	private Textbox ctIdentityNumber;
	/**
	 * Listbox
	 */
	// @Getter
	// @Setter
	// private Listbox ctStatus;
	/**
	 * Listbox
	 */
	@Getter
	@Setter
	private Listbox groupListBox;
	/**
	 * Paging
	 */
	@Getter
	@Setter
	private Paging groupListPaging;
	/**
	 * Button
	 */
	@Getter
	@Setter
	private Button proofreadButton;
	/**
	 * Button
	 */
	@Getter
	@Setter
	private Button localProofreadButton;

}
