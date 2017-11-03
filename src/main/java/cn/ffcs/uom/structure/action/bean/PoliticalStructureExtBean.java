package cn.ffcs.uom.structure.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class PoliticalStructureExtBean {
	@Getter
	@Setter
	private Panel politicalStructureExtPanel;
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
	private Listbox pageListbox;
	@Getter
	@Setter
	private Listbox orgStructureListbox;
	@Getter
	@Setter
	private Paging orgStructureListboxPaging;
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
