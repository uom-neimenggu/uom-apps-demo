package cn.ffcs.uom.structure.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.structure.component.PoliticalStructureExt;

public class OrgStructureMainBean {
	@Getter
	@Setter
	private Window orgStructureMainWin;
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
	private Tab politicalTab;
	@Getter
	@Setter
	private Tab agentTab;
	@Getter
	@Setter
	private Tab supplierTab;
	@Getter
	@Setter
	private Tab ossTab;
	@Getter
	@Setter
	private Tab tempTab;
	@Getter
	@Setter
	private Tabpanel politicalTabpanel;
	@Getter
	@Setter
	private Tabpanel agentTabpanel;
	@Getter
	@Setter
	private Tabpanel supplierTabpanel;
	@Getter
	@Setter
	private Tabpanel ossTabpanel;
	@Getter
	@Setter
	private Tabpanel tempTabpanel;
	@Getter
	@Setter
	private PoliticalStructureExt politicalStructureExt;

}
