package cn.ffcs.uom.position.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.position.action.PositionEditDiv;
import cn.ffcs.uom.position.action.PositionOrganizationListboxComposer;
import cn.ffcs.uom.position.component.PositionTreeExt;

/**
 * 岗位管理Bean
 * 
 * @author
 **/

public class PositionMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window positionMainWin;
	/**
	 * positionEditDiv.
	 */
	@Getter
	@Setter
	private PositionEditDiv positionEditDiv;
	/**
	 * positionTreeExt.
	 */
	@Getter
	@Setter
	private PositionTreeExt positionTreeExt;
	/**
	 * positionTabBox.
	 */
	@Getter
	@Setter
	private Tabbox positionTabBox;
	/**
	 * positionOrganizationTabBox.
	 */
	@Getter
	@Setter
	private Tabbox positionOrganizationTabBox;
	/**
	 * positionOrganizationTab.
	 */
	@Getter
	@Setter
	private Tab positionOrganizationTab;
	/**
	 * positionOrganizationListboxComposer.
	 */
	@Getter
	@Setter
	private PositionOrganizationListboxComposer positionOrganizationListboxComposer;
}
