package cn.ffcs.uom.staffrole.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.staffrole.action.RoleEditDiv;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeExt;

public class StaffRoleMainBean {
	@Getter
	@Setter
	private Window staffRoleMainWin;
	@Getter
	@Setter
	private RoleEditDiv roleEditDiv;
	@Getter
	@Setter
	private StaffRoleTreeExt staffRoleTreeExt;
	@Getter
	@Setter
	private Tabbox leftTabbox;
	@Getter
	@Setter
	private Tabbox roleTabBox;
	@Getter
	@Setter
	private Tabbox staffRoleTabBox;
}
