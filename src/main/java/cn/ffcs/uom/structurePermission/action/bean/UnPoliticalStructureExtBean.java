package cn.ffcs.uom.structurePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class UnPoliticalStructureExtBean {
	@Getter
	@Setter
	private Panel unpoliticalStructureExtPanel;
	/**
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegion;
	@Getter
	@Setter
	private Textbox orgId;
	@Getter
	@Setter
	private Textbox orgCode;
	@Getter
	@Setter
	private Textbox orgName;
	@Getter
	@Setter
	private Textbox orgUuId;
	@Getter
	@Setter
	private Listbox unpageListbox;
	@Getter
	@Setter
	private Listbox unorgStructureListbox;
	@Getter
	@Setter
	private Paging unorgStructureListboxPaging;
	/**
	 * 是否包含下级
	 */
	@Getter
	@Setter
	private Checkbox includeChildren;
	/**
	 * 组织
	 */
	@Getter
	@Setter
	private OrganizationBandboxExt org;

}
