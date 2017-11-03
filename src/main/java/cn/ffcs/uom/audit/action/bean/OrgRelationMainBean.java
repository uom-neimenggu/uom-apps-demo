package cn.ffcs.uom.audit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.audit.component.OrgRelationExt;

public class OrgRelationMainBean {
	@Getter
	@Setter
	private Window orgRelationMainWin;
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
	private Tab orgRelationTab;
	@Getter
	@Setter
	private Tab tempTab;
	@Getter
	@Setter
	private Tabpanel orgRelationExtTabpanel;
	@Getter
	@Setter
	private OrgRelationExt orgRelationExt;

}
