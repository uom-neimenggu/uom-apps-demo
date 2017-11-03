package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.UnitedDirectoryListboxComposer;

/**
 *组织管理Bean.
 * 
 * @author
 **/
public class UnitedDirectoryMainBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window unitedDirectoryMainWin;
	/**
	 * 列表
	 */
	@Getter
	@Setter
	private UnitedDirectoryListboxComposer unitedDirectoryListbox;
}
