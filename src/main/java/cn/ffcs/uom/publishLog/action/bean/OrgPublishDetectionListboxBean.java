package cn.ffcs.uom.publishLog.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

/**
 *员工组织关系Bean
 * 
 * @author
 **/
public class OrgPublishDetectionListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel orgPublishDetectionListboxComp;

	/**
	 * 组织名称
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 * 组织编码
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox orgPublishDetectionListBox;
	
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
	private Paging orgPublishDetectionListPaging;
}
