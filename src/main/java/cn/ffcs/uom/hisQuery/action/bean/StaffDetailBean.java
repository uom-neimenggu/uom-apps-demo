package cn.ffcs.uom.hisQuery.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 查看员工详细信息
 * @author yahui
 *
 */
public class StaffDetailBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window staffDetailWindow;
	/**
	 *员工编号.
	 **/
	@Getter
	@Setter
	private Textbox staffCode;
	/**
	 *员工名称.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 *员工描述.
	 **/
	@Getter
	@Setter
	private Textbox staffDesc;
	/**
	 *员工岗位.
	 **/
	@Getter
	@Setter
	private Textbox staffPosition;
	/**
	 *用工性质。
	 **/
	@Getter
	@Setter
	private Textbox wrokProp;
	/**
	 *兼职/全职.
	 **/
	@Getter
	@Setter
	private Listbox partTime;
	/**
	 *人员属性
	 **/
	@Getter
	@Setter
	private Textbox staffProperty;
	/**
	 *员工状态.
	 **/
	@Getter
	@Setter
	private Listbox staffStatusCd;
	/**
	 *HR账号.
	 */
	@Getter
	@Setter
	private Textbox HrNumber;
	/**
	 * 员工账号.
	 */
	@Getter
	@Setter
	private Textbox staffAccoubt;
	/**
	 *系统工号.
	 */
	@Getter
	@Setter
	private Textbox systemAccount;
	/**
	 *账号状态.
	 */
	@Getter
	@Setter
	private Listbox AccountStatusCd;
	/**
	 *确定按钮.
	 */
	@Getter
	@Setter
	private Button saveOrg;
}
