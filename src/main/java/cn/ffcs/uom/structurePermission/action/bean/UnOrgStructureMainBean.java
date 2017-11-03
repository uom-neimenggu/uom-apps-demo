package cn.ffcs.uom.structurePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.structurePermission.component.UnPoliticalStructureExt;

public class UnOrgStructureMainBean {
	@Getter
	@Setter
	private Window unorgStructureMainWin;
	/**
	 * tabBox
	 */
	@Getter
	@Setter
	private Tabbox untabBox;
	/**
	 * 当前选中的tab
	 */
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private Tab unpoliticalTab;
	@Getter
	@Setter
	private Tab unagentTab;
	@Getter
	@Setter
	private Tab unsupplierTab;
	@Getter
	@Setter
	private Tab unossTab;
	@Getter
	@Setter
	private Tab untempTab;
	@Getter
	@Setter
	private Tabpanel unpoliticalTabpanel;
	@Getter
	@Setter
	private Tabpanel unagentTabpanel;
	@Getter
	@Setter
	private Tabpanel unsupplierTabpanel;
	@Getter
	@Setter
	private Tabpanel unossTabpanel;
	@Getter
	@Setter
	private Tabpanel untempTabpanel;
	@Getter
	@Setter
	private UnPoliticalStructureExt unpoliticalStructureExt;

}
