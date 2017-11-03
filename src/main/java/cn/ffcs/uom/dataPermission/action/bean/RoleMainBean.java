package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.dataPermission.action.RoleListboxComposer;
import cn.ffcs.uom.dataPermission.action.RoleOrganizationListboxComposer;
import cn.ffcs.uom.dataPermission.action.RoleProfessionalTreeListboxComposer;
import cn.ffcs.uom.dataPermission.action.RoleTelcomRegionListboxComposer;

public class RoleMainBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window roleMainWin;
	/**
	 * 列表
	 */
	@Getter
	@Setter
	private RoleListboxComposer roleListbox;
	/**
	 * tabBox
	 */
	@Getter
	@Setter
	private Tabbox tabBox;
	/**
	 * 当前选中的tab
	 */
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private RoleTelcomRegionListboxComposer roleTelcomRegionListbox;
	@Getter
	@Setter
	private RoleOrganizationListboxComposer roleOrganizationListbox;
	@Getter
	@Setter
	private RoleProfessionalTreeListboxComposer roleProfessionalTreeListbox;
}
