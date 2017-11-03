package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 * 
 * 导入员工结果页面
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author faq
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-7-29
 * @功能说明：
 *
 */
public class StaffImportResultBean{
	
    /**
	 * Window
	 */
	@Setter
	@Getter
	private Window staffImportResultWindow;
	
	/**
	 * 信息列表.
	 */
	@Getter
	@Setter
	private Listbox infoListbox;
    
}
