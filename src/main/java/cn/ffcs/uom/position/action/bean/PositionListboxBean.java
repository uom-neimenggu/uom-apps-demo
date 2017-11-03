package cn.ffcs.uom.position.action.bean;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位管理Bean
 * 
 * @author
 **/
public class PositionListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window positionMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox positionListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging positionListboxPaging;
	/**
	 * 岗位新增按钮.
	 */
	@Getter
	@Setter
	private Button addPositionButton;
	/**
	 * 岗位查看按钮.
	 */
	@Getter
	@Setter
	private Button viewPositionButton;
	/**
	 * 岗位编辑按钮.
	 */
	@Getter
	@Setter
	private Button editPositionButton;
	/**
	 * 岗位删除按钮.
	 */
	@Getter
	@Setter
	private Button delPositionButton;
	/**
	 * positionSearchWindowDiv.
	 */
	@Getter
	@Setter
	private Div positionSearchWindowDiv;
	/**
	 * positionWindowDiv.
	 */
	@Getter
	@Setter
	private Div positionWindowDiv;
	/**
	 * 岗位名称.
	 **/
	@Getter
	@Setter
	private Textbox positionName;
	/**
	 * 岗位编码.
	 **/
	@Getter
	@Setter
	private Textbox positionCode;
	/**
	 * 岗位类型.
	 **/
	@Getter
	@Setter
	private Listbox positionType;
	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryPosition;
	/**
	 * positionWindowDiv.
	 */
	@Getter
	@Setter
	private Div positionBandboxDiv;
	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closePosiitonButton;
	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanPositionButton;

}
