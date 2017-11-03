package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class SysListEditExtBean {

	@Getter
	@Setter
	private Window sysEditWindow;
	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel sysEditExtPanel;
	/**
	 * 系统URl.
	 **/
	@Getter
	@Setter
	private Textbox sysUrl;
	/**
	 * 系统名称.
	 **/
	@Getter
	@Setter
	private Textbox sysName;
	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private Textbox clientCode;
	/**
	 * 电信区域编码.
	 **/
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegionTreeBandbox;
	/**
	 * 系统状态.
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
