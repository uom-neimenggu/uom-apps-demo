package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class IdentityCardConfigMainBean {
	@Getter
	@Setter
	private Window identityCardConfigMainWin;
	@Getter
	@Setter
	private Textbox identityCardName;
	@Getter
	@Setter
	private Textbox identityCardPrefix;
	@Getter
	@Setter
	private Listbox identityCardConfigListBox;
	@Getter
	@Setter
	private Paging identityCardConfigListboxPaging;
	@Getter
	@Setter
	private Button addIdentityCardConfigButton;
	@Getter
	@Setter
	private Button editIdentityCardConfigButton;
	@Getter
	@Setter
	private Button delIdentityCardConfigButton;
}
