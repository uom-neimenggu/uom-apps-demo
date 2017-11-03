package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class SystemOrgTreeConfigListExtBean {
	@Getter
	@Setter
	private Panel systemOrgTreeConfigListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemOrgTreeConfigButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemOrgTreeConfigButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemOrgTreeConfigButton;
	@Getter
	@Setter
	private Listbox systemOrgTreeConfigListBox;
	@Getter
	@Setter
	private Paging systemOrgTreeConfigListPaging;

}
