package cn.ffcs.uom.roleauth.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.roleauth.action.AuthorityEditDiv;
import cn.ffcs.uom.roleauth.component.AuthorityTreeExt;

public class AuthMainBean {
	@Getter
	@Setter
	private Window authMainWin;
	@Getter
	@Setter
	private AuthorityEditDiv authEditDiv;
	@Getter
	@Setter
	private AuthorityTreeExt authorityTreeExt;
	@Getter
	@Setter
	private Tabbox leftTabbox;
	@Getter
	@Setter
	private Tabbox authTabBox;
	@Getter
	@Setter
	private Tabbox roleAuthTabBox;
	
}
