package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Toolbarbutton;

import cn.ffcs.uom.organization.action.UnitedDirectoryInfoExt;

public class UnitedDirectoryInfoEditExtBean {
	@Getter
	@Setter
	private Toolbarbutton editButton;
	@Getter
	@Setter
	private Toolbarbutton saveButton;
	@Getter
	@Setter
	private Toolbarbutton recoverButton;
	@Getter
	@Setter
	private UnitedDirectoryInfoExt unitedDirectoryInfoExt;
}
