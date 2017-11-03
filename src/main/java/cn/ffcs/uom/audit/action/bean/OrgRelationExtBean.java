package cn.ffcs.uom.audit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class OrgRelationExtBean {
	@Getter
	@Setter
	private Panel orgRelationExtPanel;
	/**
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegion;
	@Getter
	@Setter
	private Textbox orgCode;
	@Getter
	@Setter
	private Textbox orgName;
	@Getter
	@Setter
	private Listbox orgRelationListbox;
	@Getter
	@Setter
	private Paging orgRelationListboxPaging;
	/**
	 * 是否包含下级
	 */
	@Getter
	@Setter
	private Checkbox includeChildren;

}
