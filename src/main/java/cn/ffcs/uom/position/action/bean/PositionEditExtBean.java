package cn.ffcs.uom.position.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * 岗位编辑Bean
 * 
 * @author
 * 
 **/
public class PositionEditExtBean {
	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel positionEditExtPanel;
	/**
	 * 岗位编码.
	 **/
	@Getter
	@Setter
	private Textbox positionCode;
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private Textbox positionName;
	/**
	 * 岗位类型.
	 **/
	@Getter
	@Setter
	private Listbox positionType;
	/**
	 * 岗位状态.
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
	 * 岗位描述.
	 **/
	@Getter
	@Setter
	private Textbox positionDesc;
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

}
