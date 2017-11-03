package cn.ffcs.uom.organization.action.bean;

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
public class OrganizationImportBean{
	
    /**
	 * Window
	 */
	@Setter
	@Getter
	private Window organizationImportWindow;
	
	/**
	 * 导入类型
	 */
	@Setter
	@Getter
	private Listbox importType;
	
}
