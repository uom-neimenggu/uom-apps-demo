package cn.ffcs.uom.accconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class AccConfigEditExtBean {

	@Getter
	@Setter
	private Window accEditWindow;
	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel accEditExtPanel;
	
	/**
	 * 配置名称.
	 **/
	@Getter
	@Setter
	private Textbox accName;
	/**
	 * 状态.
	 **/
	@Getter
	@Setter
	private Textbox statusCdName;
	/**
	 * 生效时间.
	 **/
	@Getter
	@Setter
	private Textbox effDateStr;
	/**
	 * 失效时间.
	 **/
	@Getter
	@Setter
	private Textbox expDateStr;
	/**
	 * 编辑按钮.
	 */
	@Getter
	@Setter
	private Button editButton;
	/**
	 * 保存按钮.
	 */
	@Getter
	@Setter
	private Button saveButton;
	/**
	 * 恢复按钮.
	 */
	@Getter
	@Setter
	private Button recoverButton;
	/**
	 * 确定按钮.
	 */
	@Getter
	@Setter
	private Button okButton;
	/**
	 * 取消按钮.
	 */
	@Getter
	@Setter
	private Button cancelButton;
}
