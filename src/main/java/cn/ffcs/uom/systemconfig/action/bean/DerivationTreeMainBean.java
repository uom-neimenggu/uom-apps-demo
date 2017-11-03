package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.systemconfig.action.comp.OrgTreeListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeBindingListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeOrgOrgRelaListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeOrgOrgTypeListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeStaffOrgRelaListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeStaffOrgTypeListExt;
import cn.ffcs.uom.systemconfig.action.comp.TreeStaffStaffTypeListExt;

public class DerivationTreeMainBean {
	@Getter
	@Setter
	private Window orgTreeMainWin;
	@Getter
	@Setter
	private OrgTreeListExt orgTreeListExt;
	@Getter
	@Setter
	private Tabbox tabbox;
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private TreeBindingListExt treeBindingListExt;
	@Getter
	@Setter
	private TreeOrgOrgRelaListExt treeOrgOrgRelaListExt;
	@Getter
	@Setter
	private TreeOrgOrgTypeListExt treeOrgOrgTypeListExt;
	@Getter
	@Setter
	private TreeStaffOrgTypeListExt treeStaffOrgTypeListExt;
	@Getter
	@Setter
	private TreeStaffStaffTypeListExt treeStaffStaffTypeListExt;
	@Getter
	@Setter
	private TreeStaffOrgRelaListExt treeStaffOrgRelaListExt;
}
