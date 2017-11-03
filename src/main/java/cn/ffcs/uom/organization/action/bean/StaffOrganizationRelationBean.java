package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * 人力比较Bean.
 * 
 * @author faq
 **/
public class StaffOrganizationRelationBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffOrganizationRelationMainWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel staffOrganizationRelationListboxPanel;
	/**
	 * listbox
	 */
	@Getter
	@Setter
	private Listbox staffOrganizationRelationListbox;

	/**
	 * paging
	 */
	@Getter
	@Setter
	private Paging staffOrganizationRelationListPaging;
	
}
